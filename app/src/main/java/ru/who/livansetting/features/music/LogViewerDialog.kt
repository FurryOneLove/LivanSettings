package ru.who.livansetting.features.music

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.media.MediaMetadata
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window
import android.widget.Button
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import ru.who.livansetting.core.MediaNotificationListenerService
import ru.who.livansetting.data.SettingsManager

/**
 * Экран лога прямо в приложении — читать в машине, без ноутбука и adb.
 *
 * Сделан диалогом специально: правки в AndroidManifest.xml не нужны,
 * достаточно положить этот один файл в проект.
 *
 * Открыть из раздела «Музыка»:
 *     LogViewerDialog.show(context)
 */
class LogViewerDialog(private val host: Activity) : Dialog(host) {

    private lateinit var text: TextView
    private lateinit var scroll: ScrollView
    private lateinit var status: TextView

    private val handler = Handler(Looper.getMainLooper())
    private var lastRevision = -1L
    private var autoScroll = true

    private val ticker = object : Runnable {
        override fun run() {
            refresh()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        FileLog.init(host.applicationContext)

        val root = LinearLayout(host).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.BLACK)
        }

        status = TextView(host).apply {
            setTextColor(Color.parseColor("#1EFD00"))
            textSize = 12f
            typeface = Typeface.MONOSPACE
            setPadding(dp(8), dp(6), dp(8), dp(2))
        }
        root.addView(status, LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))

        val bar = LinearLayout(host).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.START
            setPadding(dp(4), 0, dp(4), 0)
        }
        bar.addView(button("Тест") { runSelfTest() })
        bar.addView(button("API") { dumpApi() })
        bar.addView(button("Колбэк") { hookCallback() })
        bar.addView(button("Перебор") { sweepSourceTypes() })
        bar.addView(button("Держать 6") { holdSourceType(6) })
        bar.addView(button("ECU") { dumpEcu() })
        bar.addView(button("Функции") { dumpCarFunctions() })
        bar.addView(button("Показ 2") { setPresentation(2) })
        bar.addView(button("Геттеры") { toggleVerbose() })
        bar.addView(button("Копировать") { copyToClipboard() })
        bar.addView(button("В файл") { saveToFile() })
        bar.addView(button("Очистить") { FileLog.clear(); lastRevision = -1; refresh() })
        bar.addView(button("Вниз") { autoScroll = true; scrollToBottom() })
        bar.addView(button("Закрыть") { dismiss() })

        // кнопок много — даём горизонтальную прокрутку, чтобы влезли на любой экран
        val barScroll = HorizontalScrollView(host).apply {
            isHorizontalScrollBarEnabled = false
            addView(bar)
        }
        root.addView(barScroll, LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))

        text = TextView(host).apply {
            setTextColor(Color.parseColor("#D0D0D0"))
            textSize = 11f
            typeface = Typeface.MONOSPACE
            setTextIsSelectable(true)
            setPadding(dp(8), dp(4), dp(8), dp(16))
        }
        scroll = ScrollView(host).apply {
            isFillViewport = true
            addView(text, FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
            setOnTouchListener { _, _ -> autoScroll = false; false }
        }
        root.addView(
            scroll,
            LinearLayout.LayoutParams(MATCH_PARENT, 0).apply { weight = 1f }
        )

        setContentView(root)
        window?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        window?.setLayout(MATCH_PARENT, MATCH_PARENT)
        setCanceledOnTouchOutside(false)
    }

    override fun onStart() {
        super.onStart()
        lastRevision = -1
        handler.post(ticker)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(ticker)
    }

    // ---------- отрисовка ----------

    private fun refresh() {
        val rev = FileLog.revision
        if (rev == lastRevision) return
        lastRevision = rev
        text.text = FileLog.tail(400)
        status.text = "строк: ${FileLog.size()}   ${FileLog.path()}"
        if (autoScroll) scrollToBottom()
    }

    private fun scrollToBottom() {
        scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN) }
    }

    // ---------- самодиагностика ----------

    /**
     * Прогоняет всю цепочку и пишет результат в лог.
     * Первая же строка с [x] показывает, где обрыв.
     */
    private fun runSelfTest() {
        FileLog.raw("")
        FileLog.raw("========== САМОДИАГНОСТИКА ==========")
        FileLog.raw("пакет: ${host.packageName}")

        // 1. настройка в приложении
        val enabledInApp = try {
            SettingsManager(host).isDimMusicEnabled()
        } catch (e: Exception) {
            FileLog.raw("[x] SettingsManager: ${e.javaClass.simpleName} ${e.message}")
            false
        }
        mark(enabledInApp, "1. музыка на приборку включена в настройках приложения")

        // 2. доступ к уведомлениям
        val listeners = try {
            Settings.Secure.getString(host.contentResolver, "enabled_notification_listeners") ?: ""
        } catch (e: Exception) {
            ""
        }
        val granted = listeners.contains(host.packageName)
        mark(granted, "2. доступ к уведомлениям выдан")
        if (!granted) FileLog.raw("    enabled_notification_listeners = ${listeners.take(300)}")

        // 3. сервис слушателя реально запущен
        val svc = try {
            MediaNotificationListenerService.getInstance()
        } catch (e: Exception) {
            null
        }
        mark(svc != null, "3. MediaNotificationListenerService запущен")

        // 4. видим ли активные медиа-сессии
        try {
            val msm = host.getSystemService(Context.MEDIA_SESSION_SERVICE) as? MediaSessionManager
            val comp = ComponentName(host, MediaNotificationListenerService::class.java)
            val sessions = msm?.getActiveSessions(comp).orEmpty()
            mark(sessions.isNotEmpty(), "4. активных медиа-сессий: ${sessions.size}")
            sessions.forEach { c ->
                val st = when (c.playbackState?.state) {
                    PlaybackState.STATE_PLAYING -> "PLAYING"
                    PlaybackState.STATE_PAUSED -> "PAUSED"
                    null -> "нет состояния"
                    else -> "state=${c.playbackState?.state}"
                }
                val title = c.metadata?.getString(MediaMetadata.METADATA_KEY_TITLE)
                FileLog.raw("    ${c.packageName}  $st  «${title ?: "-"}»")
            }
        } catch (e: SecurityException) {
            FileLog.raw("[x] 4. нет прав на медиа-сессии: ${e.message}")
        } catch (e: Exception) {
            FileLog.raw("[x] 4. ошибка сессий: ${e.javaClass.simpleName} ${e.message}")
        }

        // 5. классы eCarX
        val dimClass = try {
            Class.forName("com.ecarx.xui.adaptapi.diminteraction.DimInteraction")
        } catch (e: Throwable) {
            null
        }
        mark(dimClass != null, "5. класс DimInteraction найден в прошивке")

        // 6-7. создаётся ли объект и есть ли MediaInteraction
        if (dimClass != null) {
            try {
                val inst = dimClass.getMethod("create", Context::class.java)
                    .invoke(null, host.applicationContext)
                mark(inst != null, "6. DimInteraction.create() вернул объект")
                if (inst != null) {
                    val connectable = try {
                        Class.forName("com.ecarx.xui.adaptapi.binder.IConnectable").isInstance(inst)
                    } catch (e: Throwable) {
                        false
                    }
                    FileLog.raw("    IConnectable: $connectable")
                    val media = try {
                        dimClass.getMethod("getMediaInteraction").invoke(inst)
                    } catch (e: Exception) {
                        FileLog.raw("    getMediaInteraction бросил ${e.cause?.javaClass?.simpleName ?: e.javaClass.simpleName}: ${e.cause?.message ?: e.message}")
                        null
                    }
                    mark(media != null, "7. getMediaInteraction() вернул объект")
                    if (media != null) {
                        FileLog.raw("    класс: ${media.javaClass.name}")
                        val names = media.javaClass.methods
                            .map { it.name }
                            .distinct()
                            .sorted()
                            .joinToString(", ")
                        FileLog.raw("    методы: $names")
                    }
                }
            } catch (e: Exception) {
                FileLog.raw("[x] 6. create() бросил ${e.cause?.javaClass?.simpleName ?: e.javaClass.simpleName}: ${e.cause?.message ?: e.message}")
            }
        }

        FileLog.raw("========== КОНЕЦ ==========")
        lastRevision = -1
        autoScroll = true
        refresh()
    }

    private fun mark(ok: Boolean, label: String) {
        FileLog.raw(if (ok) "[v] $label" else "[x] $label")
    }

    // ---------- разбор API прошивки ----------

    /**
     * Выгружает реальные константы и сигнатуры eCarX-классов этой прошивки.
     * Нужен, чтобы понять, какой SOURCE_TYPE прошивка вообще принимает
     * и что именно DIM будет спрашивать у нашего объекта IPlaybackInfo.
     */
    private fun dumpApi() {
        FileLog.raw("")
        FileLog.raw("========== API ПРОШИВКИ ==========")

        val iface = loadClass("com.ecarx.xui.adaptapi.diminteraction.IMediaInteraction")
        if (iface == null) {
            FileLog.raw("[x] IMediaInteraction не найден")
        } else {
            dumpConstants(iface, "IMediaInteraction")
            iface.declaredClasses.forEach { inner ->
                dumpConstants(inner, "IMediaInteraction.${inner.simpleName}")
            }
        }

        // реальный объект — берём тем же путём, что и рабочий код
        val impl = try {
            val dim = loadClass("com.ecarx.xui.adaptapi.diminteraction.DimInteraction")
            val inst = dim?.getMethod("create", Context::class.java)
                ?.invoke(null, host.applicationContext)
            if (inst != null) dim?.getMethod("getMediaInteraction")?.invoke(inst) else null
        } catch (e: Exception) {
            FileLog.raw("[x] объект MediaInteraction не получен: ${e.cause?.message ?: e.message}")
            null
        }

        if (impl != null) {
            FileLog.raw("--- сигнатуры ${impl.javaClass.name} ---")
            impl.javaClass.methods
                .filter { it.declaringClass != Any::class.java }
                .sortedBy { it.name }
                .forEach { m ->
                    FileLog.raw("    ${m.name}(${m.parameterTypes.joinToString { it.simpleName }}) : ${m.returnType.simpleName}")
                }

            // что DIM будет спрашивать у нашего объекта
            impl.javaClass.methods.firstOrNull { it.name == "updatePlaybackInfo" }
                ?.parameterTypes?.firstOrNull()
                ?.let { dumpInterface(it, "IPlaybackInfo — это DIM вызовет у нас") }

            // интерфейс обратной связи
            impl.javaClass.methods.firstOrNull { it.name == "setMediaInteractionCallback" }
                ?.parameterTypes?.firstOrNull()
                ?.let { dumpInterface(it, "Callback — это DIM пришлёт нам") }
        }

        FileLog.raw("========== КОНЕЦ API ==========")
        lastRevision = -1
        autoScroll = true
        refresh()
    }

    // ---------- активные пробы ----------

    /** Достаёт живой MediaInteraction тем же путём, что и рабочий код. */
    private fun obtainMedia(): Any? = try {
        val dim = loadClass("com.ecarx.xui.adaptapi.diminteraction.DimInteraction")
        val inst = dim?.getMethod("create", Context::class.java)
            ?.invoke(null, host.applicationContext)
        if (inst != null) dim?.getMethod("getMediaInteraction")?.invoke(inst) else null
    } catch (e: Exception) {
        FileLog.raw("[x] MediaInteraction недоступен: ${e.cause?.message ?: e.message}")
        null
    }

    /**
     * Вешает свой колбэк на DIM. Часть прошивок игнорирует источник,
     * который не зарегистрировал обратную связь. Заодно видно, что
     * приборка нам присылает.
     */
    private fun hookCallback() {
        val media = obtainMedia() ?: return
        val m = media.javaClass.methods.firstOrNull { it.name == "setMediaInteractionCallback" }
        if (m == null) {
            FileLog.raw("[x] setMediaInteractionCallback отсутствует")
            return
        }
        try {
            val cbClass = m.parameterTypes[0]
            val cb = java.lang.reflect.Proxy.newProxyInstance(
                cbClass.classLoader,
                arrayOf(cbClass)
            ) { _, method, args ->
                val a = args?.joinToString { it?.toString()?.take(40) ?: "null" } ?: ""
                FileLog.raw("CB <- ${method.name}($a)")
                when (method.returnType) {
                    Int::class.javaPrimitiveType -> 0
                    Long::class.javaPrimitiveType -> 0L
                    Boolean::class.javaPrimitiveType -> true
                    else -> null
                }
            }
            m.invoke(media, cb)
            FileLog.raw("[v] колбэк зарегистрирован (${cbClass.name})")
        } catch (e: Exception) {
            FileLog.raw("[x] колбэк не встал: ${e.cause?.message ?: e.message}")
        }
        lastRevision = -1; refresh()
    }

    /**
     * Долбит updateCurrentSourceType(n) два раза в секунду в течение 30 секунд.
     * Если блок музыки на приборке при этом появляется, значит наше значение
     * кто-то затирает — скорее всего штатный ecarx.xsf.mediacenter.
     */
    private fun holdSourceType(type: Int) {
        val media = obtainMedia() ?: return
        val setType = media.javaClass.methods.firstOrNull { it.name == "updateCurrentSourceType" }
        if (setType == null) {
            FileLog.raw("[x] updateCurrentSourceType отсутствует")
            return
        }
        FileLog.raw("")
        FileLog.raw(">>> УДЕРЖАНИЕ типа $type, 30 секунд. Смотри на приборку.")
        var left = 60
        val step = object : Runnable {
            override fun run() {
                if (left-- <= 0) {
                    FileLog.raw(">>> удержание закончено")
                    lastRevision = -1; refresh()
                    return
                }
                try {
                    setType.invoke(media, type)
                } catch (e: Exception) {
                    FileLog.raw("    ошибка: ${e.cause?.message ?: e.message}")
                    return
                }
                handler.postDelayed(this, 500)
            }
        }
        handler.post(step)
        lastRevision = -1; refresh()
    }

    /** Логировать каждый вызов геттера от приборки. */
    private fun toggleVerbose() {
        DimPlaybackInfo.verbose = !DimPlaybackInfo.verbose
        FileLog.raw(
            if (DimPlaybackInfo.verbose)
                ">>> лог геттеров ВКЛ — если строк «PI ->» не будет, приборка нас не опрашивает"
            else ">>> лог геттеров ВЫКЛ"
        )
        lastRevision = -1; refresh()
    }

    /**
     * Перебирает типы источника 0…31 по два раза в секунду и на каждом шаге
     * отправляет заметный тестовый трек. Смотри на приборку: на каком номере
     * появится блок музыки — тот тип она и ждёт.
     */
    private fun sweepSourceTypes() {
        val media = obtainMedia() ?: return
        val setType = media.javaClass.methods.firstOrNull { it.name == "updateCurrentSourceType" }
        val push = media.javaClass.methods.firstOrNull { it.name == "updatePlaybackInfo" }
        if (setType == null || push == null) {
            FileLog.raw("[x] нужных методов нет: setType=$setType push=$push")
            return
        }

        FileLog.raw("")
        FileLog.raw(">>> ПЕРЕБОР ИСТОЧНИКОВ 0…31, по 2 секунды. Смотри на приборку!")
        FileLog.raw(">>> Запомни номер, на котором появится блок музыки.")

        var n = 0
        val step = object : Runnable {
            override fun run() {
                if (n > 31) {
                    FileLog.raw(">>> перебор завершён")
                    lastRevision = -1; refresh()
                    return
                }
                try {
                    setType.invoke(media, n)
                    val data = DimMusicData(
                        title = "ТИП $n",
                        artist = "проверка источника",
                        album = "",
                        durationMs = 240000L,
                        positionMs = 30000L,
                        playbackStatus = DimMusicData.STATUS_PLAYING,
                        sourceType = n,
                        uuid = "sweep$n"
                    )
                    push.invoke(media, DimPlaybackInfo.createProxy(data))
                    FileLog.raw("    источник $n — отправлено")
                } catch (e: Exception) {
                    FileLog.raw("    источник $n — ошибка: ${e.cause?.message ?: e.message}")
                }
                n++
                lastRevision = -1; refresh()
                handler.postDelayed(this, 2000)
            }
        }
        handler.post(step)
    }

    // ---------- конфигурация машины ----------

    /**
     * Выгружает всё, что можно узнать про конфигурацию: системные свойства
     * и VehicleSignalManager, через который DIM пишет сигналы в шину.
     * Нужно, чтобы понять, не выключена ли поддержка медиа на приборке
     * на уровне кодирования блоков.
     */
    private fun dumpEcu() {
        FileLog.raw("")
        FileLog.raw("========== КОНФИГУРАЦИЯ ==========")

        FileLog.raw("--- системные свойства ---")
        try {
            val filter = listOf(
                "dim", "cluster", "ic_", "media", "config", "vehicle",
                "ecarx", "geely", "carinfo", "variant", "model"
            )
            Runtime.getRuntime().exec("getprop").inputStream
                .bufferedReader().forEachLine { line ->
                    val l = line.lowercase()
                    if (filter.any { l.contains(it) }) FileLog.raw("    $line")
                }
        } catch (e: Exception) {
            FileLog.raw("    getprop недоступен: ${e.message}")
        }

        // VehicleSignalManager вытаскиваем из сигнатуры getInstance у самого DIM
        val impl = obtainMedia()
        val vsmClass = impl?.javaClass?.methods
            ?.firstOrNull { it.name == "getInstance" }
            ?.parameterTypes?.getOrNull(1)

        if (vsmClass != null) {
            FileLog.raw("--- ${vsmClass.name} ---")
            dumpConstants(vsmClass, vsmClass.simpleName)
            val inst = tryCreate(vsmClass)
            if (inst == null) {
                FileLog.raw("    экземпляр не создать — только сигнатуры:")
                vsmClass.methods
                    .filter { it.parameterTypes.isEmpty() && it.declaringClass != Any::class.java }
                    .sortedBy { it.name }
                    .forEach { FileLog.raw("    ${it.returnType.simpleName} ${it.name}()") }
            } else {
                invokeNoArgGetters(inst)
            }
        }

        // возможные классы конфигурации автомобиля
        listOf(
            "com.ecarx.xui.adaptapi.car.Car",
            "com.ecarx.xui.adaptapi.car.CarInfo",
            "com.ecarx.xui.adaptapi.car.ICarInfo",
            "com.ecarx.xui.adaptapi.car.CarConfig",
            "com.ecarx.xui.adaptapi.car.vehicle.VehicleConfig",
            "com.ecarx.xui.adaptapi.diminteraction.DimInteraction"
        ).forEach { n ->
            val c = loadClass(n) ?: return@forEach
            FileLog.raw("--- найден $n ---")
            dumpConstants(c, c.simpleName)
            val inst = tryCreate(c)
            if (inst != null) invokeNoArgGetters(inst)
        }

        FileLog.raw("========== КОНЕЦ КОНФИГУРАЦИИ ==========")
        lastRevision = -1
        autoScroll = true
        refresh()
    }

    // ---------- флаги функций автомобиля ----------

    /**
     * Опрашивает ICarFunction по всем известным прошивке идентификаторам функций.
     * Именно здесь видно, включена ли в кодировке поддержка вывода медиа
     * на приборку. Car сначала подключаем — без connect() всё отдаёт -1.
     */
    private fun dumpCarFunctions() {
        val carClass = loadClass("com.ecarx.xui.adaptapi.car.Car")
        if (carClass == null) {
            FileLog.raw("[x] класс Car не найден")
            return
        }
        val car = tryCreate(carClass)
        if (car == null) {
            FileLog.raw("[x] Car не создался")
            return
        }

        FileLog.raw("")
        FileLog.raw("========== ФУНКЦИИ АВТОМОБИЛЯ ==========")
        try {
            carClass.getMethod("connect").invoke(car)
            FileLog.raw("connect() вызван, ждём подключения…")
        } catch (e: Exception) {
            FileLog.raw("connect() не удался: ${e.cause?.message ?: e.message}")
        }

        // даём сервису секунду и продолжаем
        handler.postDelayed({ dumpCarFunctionsStage2(carClass, car) }, 1200)
    }

    private fun dumpCarFunctionsStage2(carClass: Class<*>, car: Any) {
        val connected = try {
            carClass.getMethod("isConnected").invoke(car)
        } catch (e: Exception) {
            "?"
        }
        FileLog.raw("isConnected() = $connected")

        val fn = try {
            carClass.getMethod("getICarFunction").invoke(car)
        } catch (e: Exception) {
            null
        }
        if (fn == null) {
            FileLog.raw("[x] ICarFunction недоступен")
            finishBlock()
            return
        }

        // интерфейс с константами функций
        val iface = fn.javaClass.interfaces.firstOrNull { it.simpleName.contains("CarFunction") }
            ?: fn.javaClass
        FileLog.raw("--- ${iface.name} ---")

        val consts = try {
            iface.fields.filter {
                java.lang.reflect.Modifier.isStatic(it.modifiers) &&
                    it.type == Int::class.javaPrimitiveType
            }
        } catch (e: Throwable) {
            emptyList()
        }
        FileLog.raw("идентификаторов функций: ${consts.size}")

        val probes = fn.javaClass.methods.filter {
            it.parameterTypes.size == 1 &&
                it.parameterTypes[0] == Int::class.javaPrimitiveType &&
                (it.name.startsWith("is") || it.name.startsWith("get")) &&
                it.returnType != Void.TYPE
        }
        FileLog.raw("методы опроса: ${probes.joinToString { "${it.returnType.simpleName} ${it.name}(int)" }}")

        consts.sortedBy { it.name }.forEach { f ->
            val id = try {
                f.getInt(null)
            } catch (e: Throwable) {
                return@forEach
            }
            val parts = probes.mapNotNull { m ->
                try {
                    val r = m.invoke(fn, id)
                    val s = when (r) {
                        is IntArray -> r.joinToString(",", "[", "]")
                        null -> "null"
                        else -> r.toString()
                    }
                    // не засоряем: пропускаем очевидные «нет данных»
                    if (s == "false" || s == "-1" || s == "null") null else "${m.name}=$s"
                } catch (e: Throwable) {
                    null
                }
            }
            if (parts.isNotEmpty()) FileLog.raw("    ${f.name} ($id): ${parts.joinToString(" ")}")
        }

        // сведения об автомобиле
        try {
            val info = carClass.getMethod("getCarInfoManager").invoke(car)
            if (info != null) {
                FileLog.raw("--- CarInfo ---")
                invokeNoArgGetters(info)
            }
        } catch (_: Exception) {
        }

        finishBlock()
    }

    /**
     * Пытается включить показ на приборке. getShowPresentationOption() вернул 0,
     * хотя в прошивке определены только 1 (NAVI_ROUTE), 2 (ALWAYS) и 3 (NEVER).
     */
    private fun setPresentation(value: Int) {
        val dim = loadClass("com.ecarx.xui.adaptapi.diminteraction.DimInteraction")
        val inst = dim?.let { tryCreate(it) }
        if (dim == null || inst == null) {
            FileLog.raw("[x] DimInteraction недоступен")
            return
        }
        FileLog.raw("")
        FileLog.raw("--- показ на приборке ---")
        val before = try {
            dim.getMethod("getShowPresentationOption").invoke(inst)
        } catch (e: Exception) {
            "?"
        }
        FileLog.raw("    было: $before")

        val setter = dim.methods.firstOrNull {
            it.name.contains("ShowPresentation", true) && it.parameterTypes.size == 1
        }
        if (setter == null) {
            FileLog.raw("    [x] сеттера нет — значение задаётся только конфигурацией")
        } else {
            try {
                setter.invoke(inst, value)
                val after = dim.getMethod("getShowPresentationOption").invoke(inst)
                FileLog.raw("    ${setter.name}($value) вызван, стало: $after")
            } catch (e: Exception) {
                FileLog.raw("    [x] ${setter.name} упал: ${e.cause?.message ?: e.message}")
            }
        }
        finishBlock()
    }

    private fun finishBlock() {
        FileLog.raw("========== КОНЕЦ ==========")
        lastRevision = -1
        autoScroll = true
        refresh()
    }

    /** Пробует получить экземпляр статическими create/getInstance. */
    private fun tryCreate(cls: Class<*>): Any? {
        val ctx = host.applicationContext
        val candidates = listOf(
            { cls.getMethod("create", Context::class.java).invoke(null, ctx) },
            { cls.getMethod("getInstance", Context::class.java).invoke(null, ctx) },
            { cls.getMethod("getInstance").invoke(null) }
        )
        for (c in candidates) {
            try {
                val r = c()
                if (r != null) return r
            } catch (_: Throwable) {
            }
        }
        return null
    }

    /** Дёргает все безаргументные геттеры и печатает результат. */
    private fun invokeNoArgGetters(inst: Any) {
        inst.javaClass.methods
            .filter {
                it.parameterTypes.isEmpty() &&
                    it.declaringClass != Any::class.java &&
                    (it.name.startsWith("get") || it.name.startsWith("is")) &&
                    it.returnType != Void.TYPE
            }
            .sortedBy { it.name }
            .forEach { m ->
                val v = try {
                    val r = m.invoke(inst)
                    when (r) {
                        is IntArray -> r.joinToString(",", "[", "]")
                        null -> "null"
                        else -> r.toString().take(80)
                    }
                } catch (e: Throwable) {
                    "ошибка: ${e.cause?.javaClass?.simpleName ?: e.javaClass.simpleName}"
                }
                FileLog.raw("    ${m.name}() = $v")
            }
    }

    private fun loadClass(name: String): Class<*>? = try {
        Class.forName(name)
    } catch (e: Throwable) {
        null
    }

    private fun dumpConstants(cls: Class<*>, label: String) {
        val fields = try {
            cls.fields.filter {
                java.lang.reflect.Modifier.isStatic(it.modifiers) &&
                    (it.type == Int::class.javaPrimitiveType || it.type == String::class.java)
            }
        } catch (e: Throwable) {
            emptyList()
        }
        if (fields.isEmpty()) return
        FileLog.raw("--- константы $label ---")
        fields.sortedBy { it.name }.forEach { f ->
            val v = try {
                f.get(null)
            } catch (e: Throwable) {
                "?"
            }
            FileLog.raw("    ${f.name} = $v")
        }
    }

    private fun dumpInterface(cls: Class<*>, label: String) {
        FileLog.raw("--- $label ---")
        FileLog.raw("    ${cls.name}")
        try {
            cls.methods.sortedBy { it.name }.forEach { m ->
                FileLog.raw("    ${m.returnType.simpleName} ${m.name}(${m.parameterTypes.joinToString { it.simpleName }})")
            }
        } catch (e: Throwable) {
            FileLog.raw("    ошибка разбора: ${e.message}")
        }
        dumpConstants(cls, label)
    }

    // ---------- кнопки ----------

    private fun copyToClipboard() {
        try {
            val cm = host.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText("livan-dim log", FileLog.dump()))
            toast("Лог скопирован в буфер обмена")
        } catch (e: Exception) {
            toast("Не удалось скопировать: ${e.message}")
        }
    }

    private fun saveToFile() {
        val p = FileLog.flushToFile(host.applicationContext)
        toast(if (p != null) "Сохранено: $p" else "Не удалось записать файл")
    }

    private fun toast(s: String) = Toast.makeText(host, s, Toast.LENGTH_LONG).show()

    // ---------- мелочи ----------

    private fun button(label: String, onClick: () -> Unit): Button =
        Button(host).apply {
            text = label
            textSize = 12f
            isAllCaps = false
            setPadding(dp(10), dp(4), dp(10), dp(4))
            setOnClickListener { onClick() }
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                .apply { marginEnd = dp(4) }
        }

    private fun dp(v: Int): Int = (v * host.resources.displayMetrics.density).toInt()

    companion object {

        /** Открыть лог. Context может быть Activity, Fragment-контекстом или LocalContext из Compose. */
        fun show(context: Context) {
            val act = findActivity(context)
            if (act == null) {
                Toast.makeText(
                    context,
                    "Нужен контекст Activity, чтобы показать лог",
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            try {
                LogViewerDialog(act).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Не открылось: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        private fun findActivity(context: Context): Activity? {
            var c: Context? = context
            while (c is ContextWrapper) {
                if (c is Activity) return c
                c = c.baseContext
            }
            return null
        }
    }
}
