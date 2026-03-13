package ru.who.livansetting.services

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
import ru.who.livansetting.constants.IICKeyCodes
import ru.who.livansetting.utils.SimpleKeyHandler
import ru.who.livansetting.utils.KeyActionExecutor
import ru.who.livansetting.utils.AutoWarmManager
import ru.who.livansetting.MainActivity
import ru.who.livansetting.SettingsManager

/**
 * Главный сервис приложения, объединяющий все функциональности
 * - Обработка нажатий клавиш на руле (KeyInputService)
 * - Работа с автомобильной системой (CarService)
 * - Работа с I2C интерфейсом (I2CService)
 */
class MainService : Service() {
    
    // Компоненты для обработки клавиш
    private var input: Input? = null
    private var keyCallback: KeyCallback? = null
    private var settingsManager: SettingsManager? = null
    private var simpleKeyHandler: SimpleKeyHandler? = null
    private var keyActionExecutor: KeyActionExecutor? = null
    
    // Сервисы
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
        
    // Флаги для отслеживания состояния загрузки
    @Volatile
    private var isSystemBootComplete = false
    @Volatile
    private var isMainServiceStarted = false
    
    // Флаги для отслеживания режима вождения
    @Volatile
    private var isDriveModeSetOnBoot = false
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
            Log.d(TAG, "System boot complete flag set")
        }
        
        fun isSystemBootComplete(): Boolean = isSystemBootComplete
        
        fun isMainServiceStarted(): Boolean = isMainServiceStarted
    }
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "MainService created")
        
        // Устанавливаем статическую ссылку
        instance = this
        
        // Устанавливаем флаг запуска MainService
        isMainServiceStarted = true
        Log.d(TAG, "MainService started flag set")
        
        // Инициализируем все компоненты
        initializeComponents()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        
        // Инициализируем все сервисы
        initializeKeyInput()
        initializeI2CService()
        initializeCarService()
        // SensorService будет инициализирован после подключения CarService
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "MainService started with intent: ${intent?.action}")
        
        // Проверяем, что все компоненты инициализированы
        if (input == null || keyCallback == null) {
            Log.w(TAG, "Components not initialized, reinitializing...")
            initializeKeyInput()
        }
        
        // Дополнительная диагностика состояния сервиса
        Log.d(TAG, "Service state check:")
        Log.d(TAG, "  - input: ${input != null}")
        Log.d(TAG, "  - keyCallback: ${keyCallback != null}")
        Log.d(TAG, "  - simpleKeyHandler: ${simpleKeyHandler != null}")
        Log.d(TAG, "  - keyActionExecutor: ${keyActionExecutor != null}")
        
        // Проверяем, можно ли запустить AutoWarmManager
        checkAndStartAutoWarmManager()
        
        return START_STICKY // Перезапуск сервиса при завершении
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        Log.d(TAG, "MainService destroyed")
        releaseAllServices()
        // Очищаем статическую ссылку
        instance = null
        super.onDestroy()
    }
    
    /**
     * Инициализирует основные компоненты
     */
    private fun initializeComponents() {
        settingsManager = SettingsManager(this)
        keyActionExecutor = KeyActionExecutor(this)
        simpleKeyHandler = SimpleKeyHandler(this)
        i2cService = I2CService(this)
        carService = CarService(this)
        sensorService = SensorService(this)
        autoWarmManager = AutoWarmManager(this)
    }
    
    /**
     * Создает канал уведомлений
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Main Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Main service for handling all Livan Settings functionality"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    /**
     * Создает уведомление для foreground сервиса
     */
    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Livan Settings")
            .setContentText("Main service is running")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
    
    /**
     * Инициализирует обработку клавиш
     */
    private fun initializeKeyInput() {
        try {
            Log.d(TAG, "Attempting to create Input instance...")
            input = Input.create(this)
            if (input == null) {
                Log.w(TAG, "Failed to create Input instance - InputImpl class not available. " +
                        "This is expected if the ECARX Input API implementation is not present on this device. " +
                        "Service will continue running but key interception will not work.")
                return
            }
            
            Log.d(TAG, "Input instance created successfully")
            keyCallback = KeyCallback()
            
            // Запрашиваем перехват нужных клавиш
            val keysToIntercept = intArrayOf(
                KeyCode.KEYCODE_R_SRC,
                KeyCode.KEYCODE_R_VOLUME_UP,
                KeyCode.KEYCODE_R_VOLUME_DOWN,
                KeyCode.KEYCODE_R_MEDIA_NEXT,
                KeyCode.KEYCODE_R_MEDIA_PREVIOUS,
                KeyCode.KEYCODE_R_HOME,
                KeyCode.KEYCODE_R_VOLUME_MUTE,
                KeyCode.KEYCODE_R_CALL,
                KeyCode.KEYCODE_R_MEDIA_PLAY_PAUSE
            )
            
            val interceptedKeys = input!!.requestKeysInterception(keysToIntercept, keyCallback!!)
            Log.d(TAG, "Intercepted keys: ${interceptedKeys.contentToString()}")
            
            Log.d(TAG, "SimpleKeyHandler ready")
            
        } catch (e: Exception) {
            Log.w(TAG, "Error initializing key input: ${e.javaClass.simpleName}: ${e.message}")
            Log.w(TAG, "This is expected if the ECARX Input API implementation is not present on this device. " +
                    "Service will continue running but key interception will not work.")
        }
    }
    
    /**
     * Освобождает ресурсы обработки клавиш
     */
    private fun releaseKeyInput() {
        try {
            // Останавливаем SimpleKeyHandler
            simpleKeyHandler?.shutdown()
            Log.d(TAG, "SimpleKeyHandler stopped")
            
            if (input != null && keyCallback != null) {
                input!!.abandonKeysInterception(keyCallback!!)
                Log.d(TAG, "Key input resources released successfully")
            } else {
                Log.d(TAG, "No key input resources to release")
            }
            input = null
            keyCallback = null
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing key input", e)
        }
    }
    
    /**
     * Инициализирует I2C сервис
     */
    private fun initializeI2CService() {
        try {
            Log.d(TAG, "Initializing I2C service...")
            i2cService?.initialize()
            i2cService?.setKeyEventHandler { keyCode, isLongPress ->
                handleKeyPress(keyCode, isLongPress)
            }
            
            // Проверяем состояние инициализации через некоторое время
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                if (i2cService?.isInitialized() == true) {
                    Log.d(TAG, "I2C service initialized successfully")
                } else {
                    Log.w(TAG, "I2C service initialization pending or failed")
                }
            }, 2000) // Проверяем через 2 секунды
            
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing I2C service", e)
        }
    }
    
    /**
     * Освобождает ресурсы I2C сервиса
     */
    private fun releaseI2CService() {
        try {
            Log.d(TAG, "Releasing I2C service...")
            i2cService?.release()
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing I2C service", e)
        }
    }
    
    /**
     * Инициализирует Car сервис
     */
    private fun initializeCarService() {
        try {
            Log.d(TAG, "Initializing Car service...")
            
            // Сначала создаем объект Car
            val createResult = carService?.createCar()
            Log.d(TAG, "Car service creation result: $createResult")
            
            if (createResult == true) {
                // Затем подключаемся
                val connectionResult = carService?.connectToCarInterface()
                Log.d(TAG, "Car service connection result: $connectionResult")
                
                // Проверяем состояние подключения через некоторое время
                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                    if (carService?.isConnected() == true) {
                        Log.d(TAG, "Car service connected successfully")
                        // Инициализируем SensorService после подключения CarService
                        initializeSensorService()
                    } else {
                        Log.w(TAG, "Car service connection pending or failed, attempting retry...")
                        // Попытка повторного подключения
                        carService?.connectToCarInterface()
                    }
                }, 2000) // Проверяем через 2 секунды
            } else {
                Log.e(TAG, "Failed to create Car object, cannot initialize connection")
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing Car service", e)
        }
    }
    
    /**
     * Освобождает ресурсы Car сервиса
     */
    private fun releaseCarService() {
        try {
            Log.d(TAG, "Releasing Car service...")
            carService?.cleanup()
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing Car service", e)
        }
    }
    
    /**
     * Инициализирует Sensor сервис
     */
    private fun initializeSensorService() {
        try {
            Log.d(TAG, "Initializing Sensor service...")
            
            // Получаем экземпляр ISensor из CarService
            val currentCarService = carService
            if (currentCarService?.isConnected() == true) {
                val sensor = currentCarService.getISensor()
                if (sensor != null) {
                    sensorService?.initialize(sensor)
                    sensorService?.connect()
                    
                    // Устанавливаем callback для отслеживания изменений состояния зажигания
                    sensorService?.setIgnitionStateChangeCallback { ignitionState ->
                        handleIgnitionStateChange(ignitionState)
                    }
                    
                    Log.d(TAG, "Sensor service initialized and connected successfully")
                    
                    // Проверяем условия и запускаем AutoWarmManager если все готово
                    checkAndStartAutoWarmManager()
                } else {
                    Log.w(TAG, "ISensor not available from CarService")
                }
            } else {
                Log.w(TAG, "Car service not connected, sensor service initialization deferred")
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing Sensor service", e)
        }
    }
    
    /**
     * Освобождает ресурсы Sensor сервиса
     */
    private fun releaseSensorService() {
        try {
            Log.d(TAG, "Releasing Sensor service...")
            sensorService?.cleanup()
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing Sensor service", e)
        }
    }
    
    /**
     * Освобождает ресурсы AutoWarmManager
     */
    private fun releaseAutoWarmManager() {
        try {
            Log.d(TAG, "Releasing AutoWarmManager...")
            autoWarmManager?.cleanup()
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing AutoWarmManager", e)
        }
    }
    
    /**
     * Проверяет условия и запускает AutoWarmManager если все готово
     */
    private fun checkAndStartAutoWarmManager() {
        if (isSystemBootComplete && isMainServiceStarted && 
            sensorService?.isConnected() == true && 
            autoWarmManager?.isMonitoring() != true) {
            
            Log.d(TAG, "All conditions met, starting AutoWarmManager")
            autoWarmManager?.initialize(sensorService, carService)
            autoWarmManager?.startMonitoring()
        } else {
            Log.d(TAG, "AutoWarmManager conditions not met - system boot: $isSystemBootComplete, " +
                    "service started: $isMainServiceStarted, sensor connected: ${sensorService?.isConnected()}, " +
                    "monitoring: ${autoWarmManager?.isMonitoring()}")
        }
        
        // Устанавливаем режим вождения при запуске системы
        setDriveModeOnBoot()
    }
    
    /**
     * Обрабатывает изменения состояния зажигания
     * @param ignitionState новое состояние зажигания
     */
    private fun handleIgnitionStateChange(ignitionState: Int) {
        Log.d(TAG, "Ignition state changed to: $ignitionState")
        
        // Если система только запустилась и состояние зажигания изменилось на DRIVING
        if (isSystemJustBooted && ignitionState == ISensorEvent.IGNITION_STATE_DRIVING) {
            Log.d(TAG, "System just booted and ignition state changed to DRIVING, setting drive mode")
            setDriveModeOnBoot()
            isSystemJustBooted = false // Сбрасываем флаг после первой установки
        }
    }
    
    /**
     * Устанавливает режим вождения при запуске системы
     */
    private fun setDriveModeOnBoot() {
        if (isSystemBootComplete && isMainServiceStarted && carService?.isConnected() == true && !isDriveModeSetOnBoot) {
            val settingsManager = SettingsManager(this)
            val selectedMode = settingsManager.getDriveModeSelection()
            
            if (selectedMode != ru.who.livansetting.DriveModeSelection.NONE) {
                // Проверяем состояние зажигания
                val ignitionState = sensorService?.getSensorLatestValue(ISensor.SENSOR_TYPE_IGNITION_STATE)
                val isDrivingOrAcc = ignitionState == ISensorEvent.IGNITION_STATE_DRIVING || 
                                    ignitionState == ISensorEvent.IGNITION_STATE_ACC
                
                if (isDrivingOrAcc) {
                    val driveModeValue = when (selectedMode) {
                        ru.who.livansetting.DriveModeSelection.ADAPTIVE -> IDriveMode.DRIVE_MODE_SELECTION_ADAPTIVE
                        ru.who.livansetting.DriveModeSelection.SPORT -> IDriveMode.DRIVE_MODE_SELECTION_DYNAMIC
                        ru.who.livansetting.DriveModeSelection.COMFORT -> IDriveMode.DRIVE_MODE_SELECTION_COMFORT
                        ru.who.livansetting.DriveModeSelection.ECO -> IDriveMode.DRIVE_MODE_SELECTION_ECO
                        else -> return
                    }
                    
                    val success = carService?.setFunctionValue(
                        IDriveMode.DM_FUNC_DRIVE_MODE_SELECT,
                        driveModeValue
                    ) ?: false
                    
                    isDriveModeSetOnBoot = true
                    Log.d(TAG, "Drive mode set on boot: $selectedMode, ignition state: $ignitionState, success: $success")
                } else {
                    Log.d(TAG, "Drive mode not set - ignition state not suitable: $ignitionState")
                }
            }
        }
    }
    
    /**
     * Обрабатывает нажатие клавиши
     */
    private fun handleKeyPress(keyCode: Int, isLongPress: Boolean) {
        Log.d(TAG, "handleKeyPress: keyCode=$keyCode, isLongPress=$isLongPress")
        
        // Передаем в SimpleKeyHandler для обработки
        // isLongPress: false = нажатие, true = отпускание
        val action = if (isLongPress) 0 else 1
        simpleKeyHandler?.handleKeyEvent(keyCode, action)
    }
    
    /**
     * Обрабатывает отпускание клавиши громкости
     */
    private fun handleVolumeKeyRelease(keyCode: Int) {
        // Обработка отпускания клавиш громкости теперь в SimpleKeyHandler
        simpleKeyHandler?.handleKeyEvent(keyCode, 0) // 0 = отпускание
    }
    
    /**
     * Освобождает все сервисы
     */
    private fun releaseAllServices() {
        releaseKeyInput()
        releaseI2CService()
        releaseCarService()
        releaseSensorService()
        releaseAutoWarmManager()
    }
    
    // Публичные методы для доступа к сервисам
    
    /**
     * Получает экземпляр CarService
     */
    fun getCarService(): CarService? = carService
    
    /**
     * Получает экземпляр I2CService
     */
    fun getI2CService(): I2CService? = i2cService
    
    /**
     * Получает экземпляр SimpleKeyHandler
     */
    fun getSimpleKeyHandler(): SimpleKeyHandler? = simpleKeyHandler
    
    /**
     * Получает экземпляр SettingsManager
     */
    fun getSettingsManager(): SettingsManager? = settingsManager
    
    /**
     * Получает экземпляр SensorService
     */
    fun getSensorService(): SensorService? = sensorService
    
    /**
     * Тестирует обработку нажатий кнопок
     * @param keyCode код клавиши для тестирования
     */
    fun testButtonPress(keyCode: Int) {
        Log.d(TAG, "Testing button press for keyCode: $keyCode")
        handleKeyPress(keyCode, false) // Тестируем короткое нажатие
    }
    
    /**
     * Callback для обработки нажатий клавиш
     */
    private inner class KeyCallback : IKeyCallback {
        override fun onKeyPressed(keyCode: Int): Boolean {
            Log.d(TAG, "KeyCallback.onKeyPressed: keyCode=$keyCode")
            
            // Передаем событие нажатия в SimpleKeyHandler
            simpleKeyHandler?.handleKeyEvent(keyCode, 1) // 1 = нажатие
            
            return true // Перехватываем событие
        }
        
        override fun onKeyReleased(keyCode: Int): Boolean {
            Log.d(TAG, "KeyCallback.onKeyReleased: keyCode=$keyCode")
            
            // Передаем событие отпускания в SimpleKeyHandler
            simpleKeyHandler?.handleKeyEvent(keyCode, 0) // 0 = отпускание
            
            return true // Перехватываем событие
        }
    }
}
