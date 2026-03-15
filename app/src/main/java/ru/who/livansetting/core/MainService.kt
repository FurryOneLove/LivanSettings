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
import com.ecarx.xui.adaptapi.input.Input
import com.ecarx.xui.adaptapi.input.IKeyCallback
import com.ecarx.xui.adaptapi.input.KeyCode
import com.ecarx.xui.adaptapi.car.vehicle.IDriveMode
import com.ecarx.xui.adaptapi.car.sensor.ISensor
import com.ecarx.xui.adaptapi.car.sensor.ISensorEvent
import ru.who.livansetting.data.SettingsManager
import ru.who.livansetting.data.DriveModeSelection
import ru.who.livansetting.features.keys.SimpleKeyHandler
import ru.who.livansetting.features.keys.KeyActionExecutor
import ru.who.livansetting.features.auto.AutoWarmManager
import ru.who.livansetting.ui.MainActivity

/**
 * Главный сервис приложения, объединяющий все функциональности
 */
class MainService : Service() {
    
    private var input: Input? = null
    private var keyCallback: KeyCallback? = null
    private var settingsManager: SettingsManager? = null
    private var simpleKeyHandler: SimpleKeyHandler? = null
    private var keyActionExecutor: KeyActionExecutor? = null
    
    private var i2cService: I2CService? = null
    private var carService: CarService? = null
    private var sensorService: SensorService? = null
    private var autoWarmManager: AutoWarmManager? = null
    
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
        if (input == null || keyCallback == null) {
            initializeKeyInput()
        }
        checkAndStartAutoWarmManager()
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        releaseAllServices()
        instance = null
        super.onDestroy()
    }
    
    private fun initializeComponents() {
        settingsManager = SettingsManager(this)
        keyActionExecutor = KeyActionExecutor(this)
        simpleKeyHandler = SimpleKeyHandler(this, keyActionExecutor!!)
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
            input = Input.create(this) ?: return
            keyCallback = KeyCallback()
            
            val keysToIntercept = intArrayOf(
                KeyCode.KEYCODE_R_SRC, KeyCode.KEYCODE_R_VOLUME_UP, KeyCode.KEYCODE_R_VOLUME_DOWN,
                KeyCode.KEYCODE_R_MEDIA_NEXT, KeyCode.KEYCODE_R_MEDIA_PREVIOUS, KeyCode.KEYCODE_R_HOME,
                KeyCode.KEYCODE_R_VOLUME_MUTE, KeyCode.KEYCODE_R_CALL, KeyCode.KEYCODE_R_MEDIA_PLAY_PAUSE
            )
            input!!.requestKeysInterception(keysToIntercept, keyCallback!!)
        } catch (e: Exception) {
            Log.e(TAG, "Key input init error", e)
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
                if (carService?.isConnected() == true) initializeSensorService()
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
        if (isSystemJustBooted && ignitionState == ISensorEvent.IGNITION_STATE_DRIVING) {
            setDriveModeOnBoot()
            isSystemJustBooted = false
        }
    }
    
    private fun setDriveModeOnBoot() {
        if (!isSystemBootComplete || carService?.isConnected() != true) return
        
        val selectedMode = settingsManager?.getDriveModeSelection() ?: DriveModeSelection.NONE
        if (selectedMode == DriveModeSelection.NONE) return

        val ignitionState = sensorService?.getSensorLatestValue(ISensor.SENSOR_TYPE_IGNITION_STATE)
        if (ignitionState == ISensorEvent.IGNITION_STATE_DRIVING || ignitionState == ISensorEvent.IGNITION_STATE_ACC) {
            val driveModeValue = when (selectedMode) {
                DriveModeSelection.ADAPTIVE -> IDriveMode.DRIVE_MODE_SELECTION_ADAPTIVE
                DriveModeSelection.SPORT -> IDriveMode.DRIVE_MODE_SELECTION_DYNAMIC
                DriveModeSelection.COMFORT -> IDriveMode.DRIVE_MODE_SELECTION_COMFORT
                DriveModeSelection.ECO -> IDriveMode.DRIVE_MODE_SELECTION_ECO
                else -> return
            }
            carService?.setFunctionValue(IDriveMode.DM_FUNC_DRIVE_MODE_SELECT, driveModeValue)
        }
    }
    
    private fun releaseAllServices() {
        simpleKeyHandler?.shutdown()
        input?.abandonKeysInterception(keyCallback!!)
        i2cService?.release()
        carService?.cleanup()
        sensorService?.cleanup()
        autoWarmManager?.cleanup()
    }
    
    fun getCarService(): CarService? = carService
    fun getI2CService(): I2CService? = i2cService
    fun getSimpleKeyHandler(): SimpleKeyHandler? = simpleKeyHandler
    fun getSettingsManager(): SettingsManager? = settingsManager
    fun getSensorService(): SensorService? = sensorService
    
    private inner class KeyCallback : IKeyCallback {
        override fun onKeyPressed(keyCode: Int): Boolean {
            simpleKeyHandler?.handleKeyEvent(keyCode, 1)
            return true
        }
        override fun onKeyReleased(keyCode: Int): Boolean {
            simpleKeyHandler?.handleKeyEvent(keyCode, 0)
            return true
        }
    }
}
