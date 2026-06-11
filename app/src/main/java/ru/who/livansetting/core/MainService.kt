package ru.who.livansetting.core

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import ru.who.livansetting.data.MigrationManager
import ru.who.livansetting.data.SettingsManager
import ru.who.livansetting.data.DriveModeSelection
import ru.who.livansetting.features.keys.SimpleKeyHandler
import ru.who.livansetting.features.keys.KeyActionExecutor
import ru.who.livansetting.features.auto.AutoWarmManager
import ru.who.livansetting.features.auto.DrlManager
import ru.who.livansetting.features.auto.SeatHeatingManager
import ru.who.livansetting.features.navi.DimNaviManager
import ru.who.livansetting.features.music.DimMusicManager
import ru.who.livansetting.ui.MainActivity
import ru.who.livansetting.utils.VolumeController

/**
 * Главный сервис приложения, объединяющий все функциональности
 */
class MainService : Service() {

    private var input: Any? = null
    private var keyCallback: Any? = null
    private var settingsManager: SettingsManager? = null
    private var simpleKeyHandler: SimpleKeyHandler? = null
    private var keyActionExecutor: KeyActionExecutor? = null
    private var volumeController: VolumeController? = null

    private var i2cService: II2CService? = null
    private var carService: ICarService? = null
    private var sensorService: ISensorService? = null
    private var autoWarmManager: AutoWarmManager? = null
    private var drlManager: DrlManager? = null
    private var seatHeatingManager: SeatHeatingManager? = null
    private var dimNaviManager: DimNaviManager? = null
    private var dimMusicManager: DimMusicManager? = null

    private val mainHandler = Handler(Looper.getMainLooper())
    private var inputRetryCount = 0
    private var displayOffReceiver: BroadcastReceiver? = null

    companion object {
        private const val TAG = "MainService"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "main_service_channel"

        @Volatile
        private var instance: MainService? = null

        @Volatile
        private var isSystemBootComplete = false
        @Volatile
        private var isMainServiceStarted = false
        @Volatile
        private var isSystemJustBooted = false

        fun getInstance(): MainService? = instance

        fun startService(context: Context) {
            val intent = Intent(context, MainService::class.java)
            context.startForegroundService(intent)
        }

        fun setSystemBootComplete() {
            isSystemBootComplete = true
            isSystemJustBooted = true
        }

        fun isSystemBootComplete(): Boolean = isSystemBootComplete
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        isMainServiceStarted = true

        initializeComponents()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())

        initializeKeyInput()
        initializeI2CService()
        initializeCarService()
        registerDisplayOffReceiver()
        initializeDimNaviManager()
        initializeDimMusicManager()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        checkAndStartAutoWarmManager()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        releaseAllServices()
        instance = null
        isMainServiceStarted = false
        super.onDestroy()
    }

    private fun initializeComponents() {
        settingsManager = SettingsManager(this)
        MigrationManager.runIfNeeded(settingsManager!!)
        drlManager = DrlManager(this)
        seatHeatingManager = SeatHeatingManager(this)
        volumeController = VolumeController(this)

        try {
            keyActionExecutor = KeyActionExecutor(this, drlManager!!, seatHeatingManager!!, settingsManager!!, volumeController!!)
            simpleKeyHandler = SimpleKeyHandler(this, keyActionExecutor!!)
        } catch (e: Throwable) {
            Log.e(TAG, "KeyActionExecutor init error (eCarX classes unavailable)", e)
        }

        i2cService = I2CService(this)
        carService = CarService(this)
        sensorService = SensorService(this)
        autoWarmManager = AutoWarmManager(this)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Main Service", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Livan Settings")
            .setContentText("Service is running")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    private fun initializeKeyInput() {
        try {
            val inputClass = Class.forName("com.ecarx.xui.adaptapi.input.Input")
            val createMethod = inputClass.getMethod("create", Context::class.java)
            val inputInstance = createMethod.invoke(null, this)
            if (inputInstance == null) {
                inputRetryCount++
                if (inputRetryCount <= 10) {
                    Log.w(TAG, "Input.create() returned null, retry $inputRetryCount/10 in 2s")
                    mainHandler.postDelayed({ initializeKeyInput() }, 2000)
                }
                return
            }
            input = inputInstance
            inputRetryCount = 0

            val callbackClass = Class.forName("com.ecarx.xui.adaptapi.input.IKeyCallback")
            val keyCallbackInstance = createKeyCallback()
            keyCallback = keyCallbackInstance

            val keyCodeClass = Class.forName("com.ecarx.xui.adaptapi.input.KeyCode")
            val keysToIntercept = intArrayOf(
                keyCodeClass.getField("KEYCODE_R_SRC").getInt(null),
                keyCodeClass.getField("KEYCODE_R_VOLUME_UP").getInt(null),
                keyCodeClass.getField("KEYCODE_R_VOLUME_DOWN").getInt(null),
                keyCodeClass.getField("KEYCODE_R_MEDIA_NEXT").getInt(null),
                keyCodeClass.getField("KEYCODE_R_MEDIA_PREVIOUS").getInt(null),
                keyCodeClass.getField("KEYCODE_R_HOME").getInt(null),
                keyCodeClass.getField("KEYCODE_R_VOLUME_MUTE").getInt(null),
                keyCodeClass.getField("KEYCODE_R_CALL").getInt(null),
                keyCodeClass.getField("KEYCODE_R_MEDIA_PLAY_PAUSE").getInt(null)
            )

            val requestMethod = inputClass.getMethod("requestKeysInterception", IntArray::class.java, callbackClass)
            requestMethod.invoke(inputInstance, keysToIntercept, keyCallbackInstance)
            Log.i(TAG, "Key input interception registered successfully")
        } catch (e: ClassNotFoundException) {
            Log.w(TAG, "eCarX input classes not available (emulator mode)", e)
        } catch (e: Exception) {
            inputRetryCount++
            if (inputRetryCount <= 10) {
                Log.e(TAG, "Key input init error, retry $inputRetryCount/10 in 2s", e)
                mainHandler.postDelayed({ initializeKeyInput() }, 2000)
            }
        }
    }

    private fun createKeyCallback(): Any {
        val callbackClass = Class.forName("com.ecarx.xui.adaptapi.input.IKeyCallback")
        return java.lang.reflect.Proxy.newProxyInstance(
            callbackClass.classLoader,
            arrayOf(callbackClass)
        ) { proxy, method, args ->
            when (method.name) {
                "onKeyPressed" -> {
                    val keyCode = args[0] as Int
                    simpleKeyHandler?.handleKeyEvent(keyCode, 1)
                    java.lang.Boolean.TRUE
                }
                "onKeyReleased" -> {
                    val keyCode = args[0] as Int
                    simpleKeyHandler?.handleKeyEvent(keyCode, 0)
                    java.lang.Boolean.TRUE
                }
                "hashCode" -> System.identityHashCode(proxy)
                "equals" -> proxy === args?.get(0)
                "toString" -> "IKeyCallback@${Integer.toHexString(System.identityHashCode(proxy))}"
                else -> null
            }
        }
    }

    private fun initializeI2CService() {
        i2cService?.initialize()
        i2cService?.setKeyEventHandler { keyCode, isRelease ->
            simpleKeyHandler?.handleKeyEvent(keyCode, if (isRelease) 0 else 1)
        }
    }

    private fun initializeCarService() {
        if (carService?.createCar() == true) {
            carService?.connectToCarInterface()
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                if (carService?.isConnected() == true) {
                    initializeSensorService()
                } else {
                    Log.e(TAG, "CarService not connected after 2s — SensorService and AutoWarmManager will not start")
                }
            }, 2000)
        }
    }

    private fun initializeSensorService() {
        val sensor = carService?.getISensor() ?: return
        sensorService?.initialize(sensor)
        sensorService?.connect()
        sensorService?.setIgnitionStateChangeCallback { handleIgnitionStateChange(it) }
        checkAndStartAutoWarmManager()
    }

    private fun checkAndStartAutoWarmManager() {
        if (sensorService?.isConnected() == true) {
            autoWarmManager?.initialize(sensorService, carService)
            autoWarmManager?.startMonitoring()
        }
        setDriveModeOnBoot()
    }

    private fun handleIgnitionStateChange(ignitionState: Int) {
        try {
            val ignitionStateDriving = Class.forName("com.ecarx.xui.adaptapi.car.sensor.ISensorEvent")
                .getField("IGNITION_STATE_DRIVING").getInt(null)
            if (isSystemJustBooted && ignitionState == ignitionStateDriving) {
                setDriveModeOnBoot()
                isSystemJustBooted = false
            }
        } catch (e: Exception) {
            Log.e(TAG, "handleIgnitionStateChange error (eCarX classes unavailable)", e)
        }
    }

    private fun setDriveModeOnBoot() {
        if (!isSystemBootComplete || carService?.isConnected() != true) return

        val selectedMode = settingsManager?.getDriveModeSelection() ?: DriveModeSelection.NONE
        if (selectedMode == DriveModeSelection.NONE) return

        try {
            val sensorClass = Class.forName("com.ecarx.xui.adaptapi.car.sensor.ISensor")
            val sensorEventClass = Class.forName("com.ecarx.xui.adaptapi.car.sensor.ISensorEvent")
            val driveModeClass = Class.forName("com.ecarx.xui.adaptapi.car.vehicle.IDriveMode")

            val sensorTypeIgnitionState = sensorClass.getField("SENSOR_TYPE_IGNITION_STATE").getInt(null)
            val ignitionStateDriving = sensorEventClass.getField("IGNITION_STATE_DRIVING").getInt(null)
            val ignitionStateAcc = sensorEventClass.getField("IGNITION_STATE_ACC").getInt(null)

            val ignitionState = sensorService?.getSensorLatestValue(sensorTypeIgnitionState)
            if (ignitionState == ignitionStateDriving || ignitionState == ignitionStateAcc) {
                val driveModeValue = when (selectedMode) {
                    DriveModeSelection.ADAPTIVE -> driveModeClass.getField("DRIVE_MODE_SELECTION_ADAPTIVE").getInt(null)
                    DriveModeSelection.SPORT -> driveModeClass.getField("DRIVE_MODE_SELECTION_DYNAMIC").getInt(null)
                    DriveModeSelection.COMFORT -> driveModeClass.getField("DRIVE_MODE_SELECTION_COMFORT").getInt(null)
                    DriveModeSelection.ECO -> driveModeClass.getField("DRIVE_MODE_SELECTION_ECO").getInt(null)
                    else -> return
                }
                val dmFuncDriveModeSelect = driveModeClass.getField("DM_FUNC_DRIVE_MODE_SELECT").getInt(null)
                carService?.setFunctionValue(dmFuncDriveModeSelect, driveModeValue)
            }
        } catch (e: Exception) {
            Log.e(TAG, "setDriveModeOnBoot error (eCarX classes unavailable)", e)
        }
    }

    private fun initializeDimNaviManager() {
        try {
            dimNaviManager = DimNaviManager(this)
            dimNaviManager?.start()
        } catch (e: Throwable) {
            Log.e(TAG, "DimNaviManager init error", e)
        }
    }

    /** Перечитать настройку вкл/выкл навигации на приборке (вызывается из UI). */
    fun refreshDimNavi() {
        dimNaviManager?.applyEnabledState()
    }

    private fun initializeDimMusicManager() {
        try {
            dimMusicManager = DimMusicManager(this)
            dimMusicManager?.start()
        } catch (e: Throwable) {
            Log.e(TAG, "DimMusicManager init error", e)
        }
    }

    /** Перечитать настройку вкл/выкл музыки на приборке (вызывается из UI). */
    fun refreshDimMusic() {
        dimMusicManager?.applyEnabledState()
    }

    private fun registerDisplayOffReceiver() {
        val filter = IntentFilter().apply {
            addAction("ecarx.intent.action.carsignal.DISPLAY_OFF")
        }
        displayOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == "ecarx.intent.action.carsignal.DISPLAY_OFF") {
                    Log.i(TAG, "DISPLAY_OFF received, cleaning up pending key events")
                    handleDisplayOff()
                }
            }
        }
        registerReceiver(displayOffReceiver, filter)
    }

    private fun handleDisplayOff() {
        simpleKeyHandler?.cancelAllPending()
        volumeController?.stopAll()
    }

    private fun releaseAllServices() {
        displayOffReceiver?.let { unregisterReceiver(it) }
        displayOffReceiver = null
        mainHandler.removeCallbacksAndMessages(null)
        simpleKeyHandler?.shutdown()
        volumeController?.shutdown()
        try {
            if (input != null && keyCallback != null) {
                val inputClass = Class.forName("com.ecarx.xui.adaptapi.input.Input")
                val callbackClass = Class.forName("com.ecarx.xui.adaptapi.input.IKeyCallback")
                val abandonMethod = inputClass.getMethod("abandonKeysInterception", callbackClass)
                abandonMethod.invoke(input, keyCallback)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Key input release error", e)
        }
        i2cService?.release()
        carService?.cleanup()
        sensorService?.cleanup()
        autoWarmManager?.cleanup()
        dimNaviManager?.cleanup()
        dimMusicManager?.cleanup()
    }

    fun getCarService(): ICarService? = carService
    fun getI2CService(): II2CService? = i2cService
    fun getSimpleKeyHandler(): SimpleKeyHandler? = simpleKeyHandler
    fun getSettingsManager(): SettingsManager? = settingsManager
    fun getSensorService(): ISensorService? = sensorService
    fun getDimNaviManager(): DimNaviManager? = dimNaviManager
    fun getDimMusicManager(): DimMusicManager? = dimMusicManager
}
