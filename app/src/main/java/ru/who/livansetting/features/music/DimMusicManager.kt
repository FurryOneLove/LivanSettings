package ru.who.livansetting.features.music

import android.content.ComponentName
import android.content.Context
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.os.Handler
import android.os.Looper
import android.util.Log
import ru.who.livansetting.core.MediaNotificationListenerService
import ru.who.livansetting.data.SettingsManager

/**
 * Транслирует информацию о текущем треке на приборку (DIM).
 *
 * Источник данных — системные MediaSession: читает метаданные активной
 * медиа-сессии любого играющего приложения (Яндекс.Музыка, BT, локальный
 * плеер) и публикует их через IMediaInteraction.updatePlaybackInfo(IPlaybackInfo).
 *
 * Подключение к приборке — как в системном DimInteractionHelper:
 * если DimInteraction реализует IConnectable, нужно сначала connect() и дождаться
 * onConnected(), только потом getMediaInteraction(). Прямой вызов на таких
 * прошивках бросает исключение.
 *
 * Доступ к уведомлениям приложение выдаёт себе само через root при старте.
 *
 * Все обращения к eCarX-классам — через рефлексию, чтобы проект собирался и
 * работал в эмуляторе.
 */
class DimMusicManager(private val context: Context) {

    private var dimInteraction: Any? = null
    private var mediaInteraction: Any? = null
    private var dimConnected = false

    private val settings = SettingsManager(context)
    private val handler = Handler(Looper.getMainLooper())

    private val mediaSessionManager =
        context.getSystemService(Context.MEDIA_SESSION_SERVICE) as? MediaSessionManager

    @Volatile
    private var enabled = false

    private var lastUuid: String = ""
    private var lastSourceType: Int = -1

    private val periodicRunnable = object : Runnable {
        override fun run() {
            if (!enabled) return
            publishCurrent()
            handler.postDelayed(this, 1000)
        }
    }

    fun start() {
        if (!settings.isDimMusicEnabled()) {
            Log.d(TAG, "DIM music disabled in settings — not starting")
            return
        }
        if (enabled) return
        enabled = true

        try {
            NotificationAccessHelper.grant(context)
        } catch (e: Exception) {
            Log.w(TAG, "notification access grant error", e)
        }

        if (!createMediaInteraction()) {
            Log.w(TAG, "MediaInteraction not ready yet (connecting or unavailable)")
        }
        handler.post(periodicRunnable)
        Log.i(TAG, "DimMusicManager started")
    }

    fun stop() {
        if (!enabled) return
        enabled = false
        handler.removeCallbacks(periodicRunnable)
        Log.i(TAG, "DimMusicManager stopped")
    }

    fun applyEnabledState() {
        if (settings.isDimMusicEnabled()) start() else stop()
    }

    fun isEnabled(): Boolean = enabled

    private fun createMediaInteraction(): Boolean {
        if (dimConnected && mediaInteraction != null) return true
        return try {
            val clazz = Class.forName("com.ecarx.xui.adaptapi.diminteraction.DimInteraction")
            val instance = dimInteraction ?: run {
                val createMethod = clazz.getMethod("create", Context::class.java)
                createMethod.invoke(null, context)
            }
            if (instance == null) {
                Log.w(TAG, "DimInteraction.create() returned null")
                return false
            }
            dimInteraction = instance

            val isConnectable = isIConnectable(instance)
            if (android.os.Build.VERSION.SDK_INT >= 26 && isConnectable) {
                tryConnectViaIConnectable(instance, clazz)
                if (mediaInteraction == null) {
                    return false
                }
                dimConnected = true
                return true
            }

            bindMediaInteraction(instance, clazz)
            dimConnected
        } catch (e: ClassNotFoundException) {
            Log.w(TAG, "eCarX DimInteraction classes not available (emulator mode)")
            false
        } catch (e: Exception) {
            Log.e(TAG, "createMediaInteraction error", e)
            false
        }
    }

    private fun isIConnectable(instance: Any): Boolean {
        return try {
            Class.forName("com.ecarx.xui.adaptapi.binder.IConnectable").isInstance(instance)
        } catch (e: Exception) {
            false
        }
    }

    private fun bindMediaInteraction(instance: Any, clazz: Class<*>) {
        val mediaMethod = clazz.getMethod("getMediaInteraction")
        mediaInteraction = mediaMethod.invoke(instance)
        dimConnected = mediaInteraction != null
    }

    private fun tryConnectViaIConnectable(instance: Any, dimClazz: Class<*>): Boolean {
        return try {
            if (android.os.Build.VERSION.SDK_INT < 26) return false
            val connectableClass =
                Class.forName("com.ecarx.xui.adaptapi.binder.IConnectable")
            if (!connectableClass.isInstance(instance)) return false

            val watcherClass = Class.forName(
                "com.ecarx.xui.adaptapi.binder.IConnectable\$IConnectWatcher"
            )
            val watcher = java.lang.reflect.Proxy.newProxyInstance(
                watcherClass.classLoader,
                arrayOf(watcherClass)
            ) { _, method, _ ->
                when (method.name) {
                    "onConnected" -> {
                        try {
                            bindMediaInteraction(instance, dimClazz)
                            Log.i(TAG, "IConnectable onConnected — media bound")
                        } catch (e: Exception) {
                            Log.e(TAG, "bind after onConnected failed", e)
                        }
                        null
                    }
                    "onDisConnected" -> {
                        dimConnected = false
                        null
                    }
                    else -> null
                }
            }

            val registerMethod =
                connectableClass.getMethod("registerConnectWatcher", watcherClass)
            registerMethod.invoke(instance, watcher)
            val connectMethod = connectableClass.getMethod("connect")
            connectMethod.invoke(instance)
            Log.i(TAG, "IConnectable.connect() called")
            true
        } catch (e: ClassNotFoundException) {
            false
        } catch (e: Exception) {
            Log.w(TAG, "IConnectable path failed", e)
            false
        }
    }

    private fun publishCurrent() {
        if (!dimConnected || mediaInteraction == null) {
            createMediaInteraction()
        }

        val controller = findActiveSession() ?: return
        val data = buildData(controller)

        if (data.uuid == lastUuid && data.playbackStatus != DimMusicData.STATUS_PLAYING) {
            return
        }
        lastUuid = data.uuid
        publish(data)
    }

    private fun findActiveSession(): MediaController? {
        if (MediaNotificationListenerService.getInstance() == null) {
            Log.d(TAG, "NotificationListener not running")
            return null
        }
        return try {
            val component = ComponentName(context, MediaNotificationListenerService::class.java)
            val sessions = mediaSessionManager?.getActiveSessions(component) ?: return null
            if (sessions.isEmpty()) return null

            sessions.find { it.playbackState?.state == PlaybackState.STATE_PLAYING }
                ?: sessions.find { it.playbackState?.state == PlaybackState.STATE_PAUSED }
                ?: sessions.firstOrNull {
                    val pkg = it.packageName.lowercase()
                    !pkg.contains("radio") && !pkg.contains("tuner") && !pkg.contains("fm")
                }
                ?: sessions[0]
        } catch (e: SecurityException) {
            Log.w(TAG, "No access to media sessions (notification permission?)", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "findActiveSession error", e)
            null
        }
    }

    private fun buildData(controller: MediaController): DimMusicData {
        val data = DimMusicData()
        val meta: MediaMetadata? = controller.metadata
        val state: PlaybackState? = controller.playbackState

        if (meta != null) {
            data.title = meta.getString(MediaMetadata.METADATA_KEY_TITLE) ?: ""
            data.artist = meta.getString(MediaMetadata.METADATA_KEY_ARTIST) ?: ""
            data.album = meta.getString(MediaMetadata.METADATA_KEY_ALBUM) ?: ""
            data.durationMs = meta.getLong(MediaMetadata.METADATA_KEY_DURATION)
        }
        if (state != null) {
            data.positionMs = state.position
            data.playbackStatus = if (state.state == PlaybackState.STATE_PLAYING)
                DimMusicData.STATUS_PLAYING else DimMusicData.STATUS_PAUSED
        }
        data.sourceType = DimMusicData.SOURCE_TYPE_ONLINE
        data.uuid = "${controller.packageName}|${data.title}|${data.artist}|${data.playbackStatus}"
        return data
    }

    private fun publish(data: DimMusicData) {
        val media = mediaInteraction ?: return
        if (!dimConnected) return
        try {
            if (lastSourceType != data.sourceType) {
                try {
                    val stMethod = media.javaClass.getMethod(
                        "updateCurrentSourceType",
                        Int::class.javaPrimitiveType
                    )
                    stMethod.invoke(media, data.sourceType)
                    lastSourceType = data.sourceType
                } catch (_: NoSuchMethodException) {
                }
            }

            val infoProxy = DimPlaybackInfo.createProxy(data)
            val infoClass = Class.forName(
                "com.ecarx.xui.adaptapi.diminteraction.IMediaInteraction\$IPlaybackInfo"
            )
            val method = media.javaClass.getMethod("updatePlaybackInfo", infoClass)
            method.invoke(media, infoProxy)

            try {
                val progMethod = media.javaClass.getMethod(
                    "updateCurrentProgress",
                    Long::class.javaPrimitiveType
                )
                progMethod.invoke(media, data.positionMs)
            } catch (_: NoSuchMethodException) {
            }

            Log.d(TAG, "published music: ${data.limitedTitle()} / ${data.limitedArtist()}")
        } catch (e: Exception) {
            Log.e(TAG, "publish error", e)
        }
    }

    fun cleanup() {
        stop()
        try {
            val inst = dimInteraction
            if (inst != null) {
                val connectableClass =
                    Class.forName("com.ecarx.xui.adaptapi.binder.IConnectable")
                if (connectableClass.isInstance(inst)) {
                    connectableClass.getMethod("unregisterConnectWatcher").invoke(inst)
                }
            }
        } catch (_: Exception) {
        }
        mediaInteraction = null
        dimInteraction = null
        dimConnected = false
        lastSourceType = -1
    }

    companion object {
        private const val TAG = "DimMusicManager"
    }
}
