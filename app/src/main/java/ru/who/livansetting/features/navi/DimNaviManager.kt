package ru.who.livansetting.features.navi

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import ru.who.livansetting.data.SettingsManager

/**
 * Мост между навигатором и приборкой (DIM).
 *
 * Отвечает за:
 *  - создание [com.ecarx.xui.adaptapi.diminteraction.DimInteraction] и получение naviInteraction;
 *  - регистрацию [YandexNaviReceiver] для приёма данных от Яндекс.Навигатора;
 *  - трансляцию состояния навигации в вызовы naviInteraction.updateNavigationInfo(...).
 *
 * Все обращения к eCarX-классам выполнены через рефлексию, чтобы приложение
 * собиралось и запускалось в эмуляторе (там этих классов нет) — по тому же
 * принципу, что и остальные сервисы в проекте.
 */
class DimNaviManager(private val context: Context) : YandexNaviReceiver.Listener {

    private var dimInteraction: Any? = null
    private var naviInteraction: Any? = null
    private var dimConnected = false

    private var receiver: YandexNaviReceiver? = null
    private var settingsManager: SettingsManager = SettingsManager(context)

    /** Последнее состояние от навигатора, чтобы пере-публиковать при необходимости. */
    @Volatile
    private var latestState: NaviState? = null

    @Volatile
    private var enabled = false

    /** Время последнего принятого broadcast от источника (мс). 0 — данных не было. */
    @Volatile
    private var lastBroadcastTs = 0L

    /**
     * Запускает модуль навигации на приборке, если он включён в настройках.
     * Безопасно вызывать повторно.
     */
    fun start() {
        if (!settingsManager.isDimNaviEnabled()) {
            Log.d(TAG, "DIM navi disabled in settings — not starting")
            return
        }
        if (enabled) return
        enabled = true

        if (!createDimInteraction()) {
            Log.w(TAG, "DimInteraction unavailable (emulator or no system access)")
            // Ресивер всё равно регистрируем — данные будут логироваться,
            // публикация просто не пройдёт, пока нет приборки.
        }
        registerReceiver()
        Log.i(TAG, "DimNaviManager started")
    }

    /** Полностью останавливает модуль и снимает маршрут с приборки. */
    fun stop() {
        if (!enabled) return
        enabled = false
        pushDisabledRoute()
        unregisterReceiver()
        latestState = null
        Log.i(TAG, "DimNaviManager stopped")
    }

    /** Перечитать настройку вкл/выкл и применить. Вызывается из UI. */
    fun applyEnabledState() {
        if (settingsManager.isDimNaviEnabled()) start() else stop()
    }

    fun isEnabled(): Boolean = enabled

    // --- Тестовый маршрут ---

    private var testRoute: NaviTestRoute? = null

    /**
     * Запускает/останавливает проигрывание тестового маршрута на приборке.
     * Работает независимо от настройки приёма от Яндекса: при запуске сам
     * поднимает DimInteraction, если он ещё не создан.
     */
    fun toggleTestRoute(): Boolean {
        val current = testRoute
        if (current != null && current.isRunning()) {
            current.stop()
            return false
        }
        if (naviInteraction == null) {
            createDimInteraction()
        }
        val route = NaviTestRoute(this)
        testRoute = route
        route.start()
        return true
    }

    fun isTestRouteRunning(): Boolean = testRoute?.isRunning() == true

    /** Публикация одного тестового шага (минуя проверку enabled). */
    fun publishTest(state: NaviState) {
        publish(state)
    }

    /** Снять тестовый маршрут с приборки. */
    fun publishTestFinished() {
        pushDisabledRoute()
    }

    // --- YandexNaviReceiver.Listener ---

    override fun onNaviStateUpdated(state: NaviState) {
        lastBroadcastTs = System.currentTimeMillis()
        latestState = state
        publish(state)
    }

    override fun onRouteFinished() {
        lastBroadcastTs = System.currentTimeMillis()
        pushDisabledRoute()
    }

    /**
     * Статус источника данных (Xposed-патчера Яндекса).
     * true — broadcast приходил недавно (источник работает),
     * false — данных не было или давно (патчер не установлен/не активен/Яндекс не запущен).
     */
    fun isSourceActive(): Boolean {
        val ts = lastBroadcastTs
        return ts > 0 && (System.currentTimeMillis() - ts) < SOURCE_ACTIVE_WINDOW_MS
    }

    // --- DIM ---

    private fun createDimInteraction(): Boolean {
        return try {
            val clazz = Class.forName("com.ecarx.xui.adaptapi.diminteraction.DimInteraction")
            val createMethod = clazz.getMethod("create", Context::class.java)
            val instance = createMethod.invoke(null, context)
            if (instance == null) {
                Log.w(TAG, "DimInteraction.create() returned null")
                return false
            }
            dimInteraction = instance

            val naviMethod = clazz.getMethod("getNaviInteraction")
            naviInteraction = naviMethod.invoke(instance)
            dimConnected = naviInteraction != null

            // Включить режим синхронизации навигации со щитком (см. реализацию Lunaris).
            try {
                setProp("sys.ecarx.navsyncscreen.status", "1")
            } catch (e: Exception) {
                Log.w(TAG, "navsyncscreen setprop failed", e)
            }

            dimConnected
        } catch (e: ClassNotFoundException) {
            Log.w(TAG, "eCarX DimInteraction classes not available (emulator mode)")
            false
        } catch (e: Exception) {
            Log.e(TAG, "createDimInteraction error", e)
            false
        }
    }

    /**
     * Публикует одно обновление навигации на приборку.
     * naviInteraction.updateNavigationInfo(INavigationInfo) вызывается через рефлексию,
     * а сам объект INavigationInfo создаётся динамическим Proxy в [DimNavigationRouteInfo].
     */
    private fun publish(state: NaviState) {
        val navi = naviInteraction ?: run {
            Log.d(TAG, "publish skipped: naviInteraction == null")
            return
        }
        if (!dimConnected) return

        try {
            val data = DimNaviData.fromState(state, settingsManager)
            val infoProxy = DimNavigationRouteInfo.createProxy(data)

            val infoClass = Class.forName(
                "com.ecarx.xui.adaptapi.diminteraction.INaviInteraction\$INavigationInfo"
            )
            val method = navi.javaClass.getMethod("updateNavigationInfo", infoClass)
            method.invoke(navi, infoProxy)

            Log.d(TAG, "published: $data")
        } catch (e: Exception) {
            Log.e(TAG, "publish error", e)
        }
    }

    /** Снять маршрут (status = UNNAVI). */
    private fun pushDisabledRoute() {
        val navi = naviInteraction ?: return
        if (!dimConnected) return
        try {
            val data = DimNaviData(status = STATUS_UNNAVI)
            val infoProxy = DimNavigationRouteInfo.createProxy(data)
            val infoClass = Class.forName(
                "com.ecarx.xui.adaptapi.diminteraction.INaviInteraction\$INavigationInfo"
            )
            val method = navi.javaClass.getMethod("updateNavigationInfo", infoClass)
            method.invoke(navi, infoProxy)
        } catch (e: Exception) {
            Log.e(TAG, "pushDisabledRoute error", e)
        }
    }

    // --- Receiver ---

    private fun registerReceiver() {
        if (receiver != null) return
        val r = YandexNaviReceiver(this)
        val filter = IntentFilter().apply {
            addAction(YandexNaviReceiver.ACTION_MANEUVER_INFO_UPDATED)
            addAction(YandexNaviReceiver.ACTION_ROUTE_INFO_UPDATED)
        }
        // Флаг exported/not-exported обязателен с Android 14 (API 34), но
        // сама константа доступна с API 33. На более старых версиях
        // используем перегрузку без флага. Яндекс шлёт широковещательные
        // интенты, поэтому ресивер должен быть EXPORTED.
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            context.registerReceiver(r, filter, Context.RECEIVER_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            context.registerReceiver(r, filter)
        }
        receiver = r
    }

    private fun unregisterReceiver() {
        receiver?.let {
            try {
                context.unregisterReceiver(it)
            } catch (e: Exception) {
                Log.w(TAG, "unregisterReceiver error", e)
            }
        }
        receiver = null
    }

    private fun setProp(key: String, value: String) {
        // android.os.SystemProperties скрыт; вызываем через рефлексию.
        val sp = Class.forName("android.os.SystemProperties")
        val set = sp.getMethod("set", String::class.java, String::class.java)
        set.invoke(null, key, value)
    }

    fun cleanup() {
        testRoute?.stop()
        testRoute = null
        stop()
        naviInteraction = null
        dimInteraction = null
        dimConnected = false
    }

    companion object {
        private const val TAG = "DimNaviManager"
        const val STATUS_UNNAVI = 0
        const val STATUS_START = 2

        /** Окно, в течение которого источник считается активным после последнего broadcast. */
        private const val SOURCE_ACTIVE_WINDOW_MS = 20_000L
    }
}
