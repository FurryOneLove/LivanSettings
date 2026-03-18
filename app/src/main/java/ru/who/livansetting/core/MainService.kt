package ru.who.livansetting.core

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import ru.who.livansetting.data.SettingsManager
import ru.who.livansetting.data.DriveModeSelection
import ru.who.livansetting.features.keys.SimpleKeyHandler
import ru.who.livansetting.features.keys.KeyActionExecutor
import ru.who.livansetting.features.auto.AutoWarmManager
import ru.who.livansetting.features.auto.DrlManager
import ru.who.livansetting.features.auto.SeatHeatingManager
import ru.who.livansetting.ui.MainActivity

/**
 * Главный сервис приложения, объединяющий все функциональности
 */
class MainService : Service() {

    private var input: Any? = null
    private var keyCallback: Any? = null
    private var settingsManager: SettingsManager? = null
    private var simpleKeyHandler: SimpleKeyHandler? = null
    private var keyActionExecutor: KeyActionExecutor? = null

    private var i2cService: II2CService? = null
    private var carService: ICarService? = null
    private var sensorService: ISensorService? = null
    private var autoWarmManager: AutoWarmManager? = null
    private var drlManager: DrlManager? = null
    private var seatHeatingManager: SeatHeatingManager? = null

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
        private var isSystemJustBooted = true

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
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (input == null && keyCallback == null) {
            initializeKeyInput()
        }
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
        drlManager = DrlManager(this)
        seatHeatingManager = SeatHeatingManager(this)

        try {
            keyActionExecutor = KeyActionExecutor(this, drlManager!!, seatHeatingManager!!)
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
            val inputInstance = createMethod.invoke(null, this) ?: return
            input = inputInstance

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
        } catch (e: ClassNotFoundException) {
            Log.w(TAG, "eCarX input classes not available (emulator mode)", e)
        } catch (e: Exception) {
            Log.e(TAG, "Key input init error", e)
        }
    }

    private fun createKeyCallback(): Any {
        val callbackClass = Class.forName("com.ecarx.xui.adaptapi.input.IKeyCallback")
        return java.lang.reflect.Proxy.newProxyInstance(
            callbackClass.classLoader,
            arrayOf(callbackClass)
        ) { _, method, args ->
            when (method.name) {
                "onKeyPressed" -> {
                    val keyCode = args[0] as Int
                    simpleKeyHandler?.handleKeyEvent(keyCode, 1)
                    true
                }
                "onKeyReleased" -> {
                    val keyCode = args[0] as Int
                    simpleKeyHandler?.handleKeyEvent(keyCode, 0)
                    true
                }
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
        if (isSystemBootComplete && isMainServiceStarted && sensorService?.isConnected() == true) {
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

    private fun releaseAllServices() {
        simpleKeyHandler?.shutdown()
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
    }

    fun getCarService(): ICarService? = carService
    fun getI2CService(): II2CService? = i2cService
    fun getSimpleKeyHandler(): SimpleKeyHandler? = simpleKeyHandler
    fun getSettingsManager(): SettingsManager? = settingsManager
    fun getSensorService(): ISensorService? = sensorService
}
