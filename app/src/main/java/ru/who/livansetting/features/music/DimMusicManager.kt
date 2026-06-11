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
 * Источник данных — системные MediaSession (как в Lunaris): читает метаданные
 * активной медиа-сессии любого играющего приложения (Яндекс.Музыка, BT,
 * локальный плеер и т.д.) и публикует их через
 * IMediaInteraction.updatePlaybackInfo(IPlaybackInfo).
 *
 * Требует разрешения доступа к уведомлениям (NotificationListener) — оно уже
 * используется в проекте для MediaNotificationListenerService.
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

    /** Периодический пуш прогресса во время проигрывания. */
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

        if (!createMediaInteraction()) {
            Log.w(TAG, "MediaInteraction unavailable (emulator or no system access)")
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

    /** Показать приветствие на приборке (для теста вывода). */
    fun publishWelcome() {
        if (mediaInteraction == null) createMediaInteraction()
        publish(DimMusicData.welcome())
    }

    // --- DIM ---

    private fun createMediaInteraction(): Boolean {
        return try {
            val clazz = Class.forName("com.ecarx.xui.adaptapi.diminteraction.DimInteraction")
            val createMethod = clazz.getMethod("create", Context::class.java)
            val instance = createMethod.invoke(null, context) ?: run {
                Log.w(TAG, "DimInteraction.create() returned null")
                return false
            }
            dimInteraction = instance

            val mediaMethod = clazz.getMethod("getMediaInteraction")
            mediaInteraction = mediaMethod.invoke(instance)
            dimConnected = mediaInteraction != null
            dimConnected
        } catch (e: ClassNotFoundException) {
            Log.w(TAG, "eCarX DimInteraction classes not available (emulator mode)")
            false
        } catch (e: Exception) {
            Log.e(TAG, "createMediaInteraction error", e)
            false
        }
    }

    /** Прочитать активную сессию и опубликовать на приборку, если что-то изменилось. */
    private fun publishCurrent() {
        val controller = findActiveSession() ?: return
        val data = buildData(controller)

        // Дедупликация: не слать одно и то же (кроме обновления времени при игре).
        if (data.uuid == lastUuid && data.playbackStatus != DimMusicData.STATUS_PLAYING) {
            return
        }
        lastUuid = data.uuid
        publish(data)
    }

    /** Выбор активной сессии — та же логика, что в CarMediaController. */
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
            val infoProxy = DimPlaybackInfo.createProxy(data)
            val infoClass = Class.forName(
                "com.ecarx.xui.adaptapi.diminteraction.IMediaInteraction\$IPlaybackInfo"
            )
            val method = media.javaClass.getMethod("updatePlaybackInfo", infoClass)
            method.invoke(media, infoProxy)

            // Прогресс отдельным вызовом — на части прошивок время идёт через него.
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
        mediaInteraction = null
        dimInteraction = null
        dimConnected = false
    }

    companion object {
        private const val TAG = "DimMusicManager"
    }
}
