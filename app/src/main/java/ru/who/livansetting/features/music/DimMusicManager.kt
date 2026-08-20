package ru.who.livansetting.features.music

import android.content.ComponentName
import android.content.Context
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.os.Handler
import android.os.Looper
import java.lang.reflect.Method
import ru.who.livansetting.core.MediaNotificationListenerService
import ru.who.livansetting.data.SettingsManager

/**
 * Транслирует информацию о текущем треке на приборку (DIM).
 *
 * Источник данных — системные MediaSession: читает метаданные активной
 * медиа-сессии любого играющего приложения (Яндекс.Музыка, BT, локальный
 * плеер) и публикует их через IMediaInteraction.updatePlaybackInfo(IPlaybackInfo).
 *
 * Как устроено подключение (по разбору AdapterAPIImpl и по логам с машины):
 *  - DimInteraction.create() строит DimInteractionImpl, тот в конструкторе
 *    создаёт VehicleSignalManager и android.car.Car, но НЕ подключается;
 *  - connect() обязателен: без него Car.isConnected() == false,
 *    getCarPropertyManager() возвращает null, и sendBytesToMcuByPropID
 *    молча возвращает false, ничего не логируя;
 *  - подключение асинхронное, и до его завершения нельзя звать
 *    getMediaInteraction(): конструктор MediaInteractionImpl вызывает
 *    registerMusicETC() -> CarPropertyManager.registerListener() и падает с NPE;
 *  - поэтому: connect() -> ждём onConnected() -> getMediaInteraction().
 *    Плюс страховка: publishCurrent() на каждом тике пробует ensureDim() заново,
 *    так что даже если колбэк не придёт, привязка случится, как только
 *    car service поднимется.
 *
 * Доступ к уведомлениям приложение выдаёт себе само через root при старте.
 *
 * Все обращения к eCarX-классам — через рефлексию, чтобы проект собирался и
 * работал в эмуляторе.
 */
class DimMusicManager(private val context: Context) {

    private var dimInteraction: Any? = null
    private var mediaInteraction: Any? = null

    /** Подключился ли car service. Влияет только на логи и принудительный ресенд. */
    @Volatile
    private var carConnected = false

    private var updatePlaybackInfoMethod: Method? = null
    private var updateSourceTypeMethod: Method? = null

    private val settings = SettingsManager(context)
    private val handler = Handler(Looper.getMainLooper())

    private val mediaSessionManager =
        context.getSystemService(Context.MEDIA_SESSION_SERVICE) as? MediaSessionManager

    @Volatile
    private var enabled = false

    /** Подпись последних отправленных данных — чтобы не гнать одно и то же в CAN. */
    private var lastSignature: String = ""
    private var lastSentAt: Long = 0L

    /** true, если приборке уже сказали, что источника нет. */
    private var clearedOnDim = true

    /** Чтобы не спамить в лог каждую секунду в эмуляторе. */
    private var unavailableLogged = false

    // Бегущая строка для полей, которые не влезают в окно приборки.
    private val titleMarquee = DimMarquee(DimMusicData.TITLE_MAX)
    private val artistMarquee = DimMarquee(DimMusicData.ARTIST_MAX)
    private val albumMarquee = DimMarquee(DimMusicData.ALBUM_MAX)

    /** connect() и регистрация watcher'а делаются один раз. */
    private var connectRequested = false

    private val periodicRunnable = object : Runnable {
        override fun run() {
            if (!enabled) return
            publishCurrent()
            handler.postDelayed(this, TICK_MS)
        }
    }

    fun start() {
        FileLog.init(context)
        if (!settings.isDimMusicEnabled()) {
            FileLog.d(TAG, "DIM music disabled in settings — not starting")
            return
        }
        if (enabled) return
        enabled = true

        try {
            NotificationAccessHelper.grant(context)
        } catch (e: Exception) {
            FileLog.w(TAG, "notification access grant error", e)
        }

        ensureDim()
        handler.post(periodicRunnable)
        FileLog.i(TAG, "DimMusicManager started")
    }

    fun stop() {
        if (!enabled) return
        enabled = false
        handler.removeCallbacks(periodicRunnable)
        FileLog.i(TAG, "DimMusicManager stopped")
    }

    fun applyEnabledState() {
        if (settings.isDimMusicEnabled()) start() else stop()
    }

    fun isEnabled(): Boolean = enabled

    // ---------- подключение ----------

    /**
     * Создаёт DimInteraction, инициирует connect() и сразу берёт
     * IMediaInteraction. Повторные вызовы безопасны.
     */
    private fun ensureDim(): Boolean {
        if (mediaInteraction != null) return true
        return try {
            val clazz = Class.forName("com.ecarx.xui.adaptapi.diminteraction.DimInteraction")

            val instance = dimInteraction ?: clazz
                .getMethod("create", Context::class.java)
                .invoke(null, context)

            if (instance == null) {
                FileLog.w(TAG, "DimInteraction.create() returned null")
                return false
            }
            dimInteraction = instance

            // connect() обязателен — иначе CarPropertyManager будет null.
            registerWatcherAndConnect(instance)

            // ВАЖНО: getMediaInteraction() дёргать до подключения нельзя.
            // Конструктор MediaInteractionImpl вызывает registerMusicETC(), а тот —
            // CarPropertyManager.registerListener() на ещё пустой ссылке -> NPE.
            // Подключение асинхронное, поэтому ждём onConnected и пробуем на следующем тике.
            if (connectRequested && !carConnected) {
                FileLog.d(TAG, "waiting for car service before getMediaInteraction()")
                return false
            }

            mediaInteraction = clazz.getMethod("getMediaInteraction").invoke(instance)
            if (mediaInteraction == null) {
                FileLog.w(TAG, "getMediaInteraction() returned null")
                return false
            }

            cacheMethods()
            declareSourceTypeList()
            FileLog.i(TAG, "MediaInteraction ready (carConnected=$carConnected)")
            true
        } catch (e: ClassNotFoundException) {
            if (!unavailableLogged) {
                unavailableLogged = true
                FileLog.w(TAG, "eCarX DimInteraction classes not available (emulator mode)")
            }
            false
        } catch (e: Exception) {
            FileLog.e(TAG, "ensureDim error", e)
            false
        }
    }

    private fun registerWatcherAndConnect(instance: Any) {
        if (connectRequested) return
        try {
            val connectableClass = Class.forName("com.ecarx.xui.adaptapi.binder.IConnectable")
            if (!connectableClass.isInstance(instance)) {
                FileLog.w(TAG, "DimInteraction is not IConnectable — connect() пропущен")
                return
            }

            val watcherClass =
                Class.forName("com.ecarx.xui.adaptapi.binder.IConnectable\$IConnectWatcher")
            val watcher = java.lang.reflect.Proxy.newProxyInstance(
                watcherClass.classLoader,
                arrayOf(watcherClass)
            ) { _, method, _ ->
                when (method.name) {
                    "onConnected" -> {
                        carConnected = true
                        // Заставляем отправить заново: до подключения всё уходило в никуда.
                        lastSignature = ""
                        clearedOnDim = false
                        FileLog.i(TAG, "car service connected — will resend")
                        // Привязываемся сразу, не дожидаясь очередного тика.
                        handler.post { if (enabled) ensureDim() }
                    }
                    "onDisConnected" -> {
                        carConnected = false
                        FileLog.w(TAG, "car service disconnected")
                    }
                }
                null
            }

            connectableClass.getMethod("registerConnectWatcher", watcherClass)
                .invoke(instance, watcher)
            connectableClass.getMethod("connect").invoke(instance)
            connectRequested = true
            FileLog.i(TAG, "IConnectable.connect() called")
        } catch (e: Exception) {
            FileLog.w(TAG, "connect() failed", e)
        }
    }

    private fun cacheMethods() {
        val media = mediaInteraction ?: return
        updatePlaybackInfoMethod = media.javaClass.methods.firstOrNull {
            it.name == "updatePlaybackInfo" && it.parameterTypes.size == 1
        }
        updateSourceTypeMethod = media.javaClass.methods.firstOrNull {
            it.name == "updateCurrentSourceType" && it.parameterTypes.size == 1
        }
        if (updatePlaybackInfoMethod == null) {
            FileLog.w(TAG, "updatePlaybackInfo не найден в ${media.javaClass.name}")
        }
    }

    /**
     * Сообщает приборке, какие типы источников мы умеем отдавать.
     * Значение попадает в битовую маску кадра 0x07:
     * 1->0x04, 2->0x40, 3->0x01, 4->0x02, 6->0x10, 7->0x08, 8->0x20;
     * типы 0 и 5 в маске не представлены (дадут 0).
     * Объявляем ровно тот тип, который реально публикуем.
     */
    private fun declareSourceTypeList() {
        val media = mediaInteraction ?: return
        try {
            media.javaClass
                .getMethod("updateMediaSourceTypeList", IntArray::class.java)
                .invoke(media, intArrayOf(DimMusicData.SOURCE_TYPE_ONLINE))
            FileLog.i(TAG, "source type list declared: [${DimMusicData.SOURCE_TYPE_ONLINE}]")
        } catch (e: NoSuchMethodException) {
            FileLog.w(TAG, "updateMediaSourceTypeList not available on this firmware")
        } catch (e: Exception) {
            FileLog.w(TAG, "declareSourceTypeList failed", e)
        }
    }

    // ---------- публикация ----------

    private fun publishCurrent() {
        if (mediaInteraction == null && !ensureDim()) return

        val controller = findActiveSession()

        if (controller == null) {
            clearDim()
            return
        }

        val raw = buildData(controller)
        val signature = raw.signature()
        val now = android.os.SystemClock.elapsedRealtime()
        val changed = signature != lastSignature

        titleMarquee.setText(raw.title)
        artistMarquee.setText(raw.artist)
        albumMarquee.setText(raw.album)
        if (changed) {
            titleMarquee.rewind()
            artistMarquee.rewind()
            albumMarquee.rewind()
        }

        // Крутим только пока играет. На паузе замираем: иначе при длительной
        // остановке кадры уходили бы в CAN бесконечно без всякой пользы.
        val animating = raw.playbackStatus == DimMusicData.STATUS_PLAYING &&
            (titleMarquee.scrolling || artistMarquee.scrolling || albumMarquee.scrolling)

        val keepAliveDue = now - lastSentAt >= KEEP_ALIVE_MS
        if (!changed && !animating && !keepAliveDue) return

        // Шаг бегущей строки делаем только когда содержимое не менялось:
        // новый трек всегда показываем с начала.
        if (!changed && animating) {
            titleMarquee.advance()
            artistMarquee.advance()
            albumMarquee.advance()
        }

        val data = raw.copy(
            title = titleMarquee.current(),
            artist = artistMarquee.current(),
            album = albumMarquee.current()
        )

        if (publish(data)) {
            lastSignature = signature
            lastSentAt = now
            clearedOnDim = false
            if (changed) {
                FileLog.d(TAG, "published: ${data.limitedTitle()} / ${data.limitedArtist()}")
            }
        }
    }

    /**
     * Гасит блок музыки на приборке. Работает только через
     * updateCurrentSourceType(-1): все остальные значения этот метод игнорирует
     * (в прошивке стоит `if (sourceType != -1) return`).
     */
    private fun clearDim() {
        if (clearedOnDim) return
        val media = mediaInteraction ?: return
        try {
            updateSourceTypeMethod?.invoke(media, DimMusicData.SOURCE_TYPE_DISCONNECT)
            clearedOnDim = true
            lastSignature = ""
            lastSentAt = 0L
            titleMarquee.rewind()
            artistMarquee.rewind()
            albumMarquee.rewind()
            FileLog.d(TAG, "no active session — source cleared (-1)")
        } catch (e: Exception) {
            FileLog.w(TAG, "clearDim failed", e)
        }
    }

    private fun findActiveSession(): MediaController? {
        if (MediaNotificationListenerService.getInstance() == null) {
            FileLog.d(TAG, "NotificationListener not running")
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
            FileLog.w(TAG, "No access to media sessions (notification permission?)", e)
            null
        } catch (e: Exception) {
            FileLog.e(TAG, "findActiveSession error", e)
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
        // Любой тип, кроме радийных (3/4/0x21/0x22), уходит в музыкальную ветку.
        // 6 (ONLINE) — верно по смыслу и вызывает updateMediaPlayInfo один раз,
        // тогда как 0/1/7 из-за fall-through в switch отправляют кадр дважды.
        data.sourceType = DimMusicData.SOURCE_TYPE_ONLINE
        data.uuid = "${controller.packageName}|${data.title}|${data.artist}"
        return data
    }

    private fun publish(data: DimMusicData): Boolean {
        val media = mediaInteraction ?: return false
        val method = updatePlaybackInfoMethod ?: return false
        return try {
            method.invoke(media, DimPlaybackInfo.createProxy(data))
            true
        } catch (e: Exception) {
            FileLog.e(TAG, "publish error", e)
            false
        }
    }

    // ---------- завершение ----------

    fun cleanup() {
        stop()
        try {
            val inst = dimInteraction
            if (inst != null) {
                val connectableClass =
                    Class.forName("com.ecarx.xui.adaptapi.binder.IConnectable")
                if (connectableClass.isInstance(inst)) {
                    connectableClass.getMethod("disconnect").invoke(inst)
                    connectableClass.getMethod("unregisterConnectWatcher").invoke(inst)
                }
            }
        } catch (_: Exception) {
        }
        mediaInteraction = null
        dimInteraction = null
        updatePlaybackInfoMethod = null
        updateSourceTypeMethod = null
        carConnected = false
        connectRequested = false
        unavailableLogged = false
        lastSignature = ""
        lastSentAt = 0L
        clearedOnDim = true
    }

    companion object {
        private const val TAG = "DimMusicManager"

        /**
         * Период опроса MediaSession. Он же — шаг бегущей строки:
         * один символ за такт. 700 мс — примерно 1.4 символа в секунду,
         * читаемо и не заваливает шину кадрами.
         */
        private const val TICK_MS = 700L

        /**
         * Повторная отправка неизменившихся данных. Нужна, чтобы приборка
         * подхватила состояние после своей перезагрузки или позднего
         * подключения car service. Чаще слать смысла нет — это трафик на CAN.
         */
        private const val KEEP_ALIVE_MS = 10_000L
    }
}
