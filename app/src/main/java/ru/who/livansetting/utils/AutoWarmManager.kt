package ru.who.livansetting.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import ru.who.livansetting.services.SensorService
import ru.who.livansetting.services.CarService
import com.ecarx.xui.adaptapi.car.sensor.ISensor
import com.ecarx.xui.adaptapi.car.sensor.ISensorEvent

/**
 * Менеджер для управления автоматическим подогревом сидений
 * Проверяет температуру в салоне и включает подогрев при необходимости
 */
class AutoWarmManager(private val context: Context) {
    
    companion object {
        private const val TAG = "AutoWarmManager"
        
        // Ключи для настроек в SharedPreferences
        private const val PREFS_NAME = "livan_settings"
        private const val KEY_DRIVER_AUTO_WARM_ENABLED = "driver_auto_warm_enabled"
        // Удалено: настройка порога температуры включения
        private const val KEY_DRIVER_WARM_LEVEL = "driver_warm_level"
        private const val KEY_DRIVER_TIMEOUT_MINUTES = "driver_timeout_minutes"
        private const val KEY_PASSENGER_AUTO_WARM_ENABLED = "passenger_auto_warm_enabled"
        // Удалено: настройка порога температуры включения (пассажир)
        private const val KEY_PASSENGER_WARM_LEVEL = "passenger_warm_level"
        private const val KEY_PASSENGER_TIMEOUT_MINUTES = "passenger_timeout_minutes"
        
        // Интервал проверки температуры (в миллисекундах)
        private const val TEMPERATURE_CHECK_INTERVAL = 30000L // 30 секунд
    }
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private var sensorService: SensorService? = null
    private var carService: CarService? = null
    private var isMonitoring = false
    private var temperatureCheckRunnable: Runnable? = null
    private val handler = android.os.Handler(android.os.Looper.getMainLooper())
    
    // Время включения подогрева для отслеживания таймаутов
    private var driverHeatingStartTime: Long = 0
    private var passengerHeatingStartTime: Long = 0
    private var isDriverHeatingActive = false
    private var isPassengerHeatingActive = false
    
    /**
     * Инициализирует менеджер автоподогрева
     * @param sensorService сервис для получения температуры
     * @param carService сервис для управления подогревом сидений
     */
    fun initialize(sensorService: SensorService?, carService: CarService?) {
        this.sensorService = sensorService
        this.carService = carService
        Log.d(TAG, "AutoWarmManager initialized")
    }
    
    /**
     * Запускает мониторинг температуры и автоподогрев
     */
    fun startMonitoring() {
        if (isMonitoring) {
            Log.w(TAG, "Already monitoring temperature")
            return
        }
        
        if (sensorService == null || carService == null) {
            Log.e(TAG, "SensorService or CarService not initialized")
            return
        }
        
        if (!sensorService!!.isConnected()) {
            Log.w(TAG, "SensorService not connected, cannot start monitoring")
            return
        }
        
        // Проверяем, включен ли автоподогрев для водителя или пассажира
        val driverEnabled = isDriverAutoWarmEnabled()
        val passengerEnabled = isPassengerAutoWarmEnabled()
        
        if (!driverEnabled && !passengerEnabled) {
            Log.d(TAG, "Auto-warm not enabled for driver or passenger, skipping sensor monitoring")
            return
        }
        
        isMonitoring = true
        Log.d(TAG, "Starting temperature monitoring... (driver: $driverEnabled, passenger: $passengerEnabled)")
        
        // Создаем задачу для периодической проверки температуры
        temperatureCheckRunnable = object : Runnable {
            override fun run() {
                if (isMonitoring) {
                    checkTemperatureAndControlHeating()
                    // Планируем следующую проверку
                    handler.postDelayed(this, TEMPERATURE_CHECK_INTERVAL)
                }
            }
        }
        
        // Запускаем первую проверку через 5 секунд
        handler.postDelayed(temperatureCheckRunnable!!, 5000)
    }
    
    /**
     * Останавливает мониторинг температуры
     */
    fun stopMonitoring() {
        if (!isMonitoring) {
            Log.w(TAG, "Not monitoring temperature")
            return
        }
        
        isMonitoring = false
        temperatureCheckRunnable?.let { handler.removeCallbacks(it) }
        temperatureCheckRunnable = null
        Log.d(TAG, "Temperature monitoring stopped")
    }
    
    /**
     * Проверяет температуру и управляет подогревом сидений
     */
    private fun checkTemperatureAndControlHeating() {
        try {
            // Сначала проверяем, нужно ли продолжать мониторинг
            checkAutoWarmConditions()
            
            // Если мониторинг остановлен, выходим
            if (!isMonitoring) {
                return
            }
            
            // Получаем текущее состояние зажигания
            val ignitionState = sensorService?.getSensorLatestValue(ISensor.SENSOR_TYPE_IGNITION_STATE)
            val isIgnitionOn = ignitionState == ISensorEvent.IGNITION_STATE_DRIVING ||
                    ignitionState == ISensorEvent.IGNITION_STATE_ACC
            Log.d(TAG, "Ignition state: $ignitionState, isOn: $isIgnitionOn")
            
            // Проверяем настройки водителя
            checkDriverSeatHeating(isIgnitionOn)
            
            // Проверяем настройки пассажира
            checkPassengerSeatHeating(isIgnitionOn)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error checking temperature and controlling heating", e)
        }
    }
    
    /**
     * Проверяет и управляет подогревом сиденья водителя
     * @param currentTemperature текущая температура в салоне
     */
    private fun checkDriverSeatHeating(isIgnitionOn: Boolean) {
        val isEnabled = sharedPreferences.getBoolean(KEY_DRIVER_AUTO_WARM_ENABLED, false)
        if (!isEnabled) {
            // Если автоподогрев отключен, выключаем подогрев если он активен
            if (isDriverHeatingActive) {
                disableDriverSeatHeating()
                isDriverHeatingActive = false
            }
            return
        }
        
        val warmLevel = sharedPreferences.getInt(KEY_DRIVER_WARM_LEVEL, 1)
        val timeoutMinutes = sharedPreferences.getInt(KEY_DRIVER_TIMEOUT_MINUTES, 30)
        
        Log.d(TAG, "Driver seat auto-warm: enabled=$isEnabled, ignitionOn=$isIgnitionOn, level=$warmLevel, timeout=${timeoutMinutes}min")
        
        // Проверяем таймаут если подогрев активен
        if (isDriverHeatingActive) {
            val currentTime = System.currentTimeMillis()
            val heatingDuration = (currentTime - driverHeatingStartTime) / (1000 * 60) // в минутах
            
            if (heatingDuration >= timeoutMinutes) {
                Log.d(TAG, "Driver seat heating timeout reached (${heatingDuration}min >= ${timeoutMinutes}min), disabling")
                disableDriverSeatHeating()
                isDriverHeatingActive = false
                return
            }
        }
        
        if (isIgnitionOn) {
            if (!isDriverHeatingActive) {
                Log.d(TAG, "Ignition ON, enabling driver seat heating")
                enableDriverSeatHeating(warmLevel)
                isDriverHeatingActive = true
                driverHeatingStartTime = System.currentTimeMillis()
            }
        } else {
            if (isDriverHeatingActive) {
                Log.d(TAG, "Ignition OFF, disabling driver seat heating")
                disableDriverSeatHeating()
                isDriverHeatingActive = false
            }
        }
    }
    
    /**
     * Проверяет и управляет подогревом сиденья пассажира
     * @param currentTemperature текущая температура в салоне
     */
    private fun checkPassengerSeatHeating(isIgnitionOn: Boolean) {
        val isEnabled = sharedPreferences.getBoolean(KEY_PASSENGER_AUTO_WARM_ENABLED, false)
        if (!isEnabled) {
            // Если автоподогрев отключен, выключаем подогрев если он активен
            if (isPassengerHeatingActive) {
                disablePassengerSeatHeating()
                isPassengerHeatingActive = false
            }
            return
        }
        
        val warmLevel = sharedPreferences.getInt(KEY_PASSENGER_WARM_LEVEL, 1)
        val timeoutMinutes = sharedPreferences.getInt(KEY_PASSENGER_TIMEOUT_MINUTES, 30)
        
        Log.d(TAG, "Passenger seat auto-warm: enabled=$isEnabled, ignitionOn=$isIgnitionOn, level=$warmLevel, timeout=${timeoutMinutes}min")
        
        // Проверяем таймаут если подогрев активен
        if (isPassengerHeatingActive) {
            val currentTime = System.currentTimeMillis()
            val heatingDuration = (currentTime - passengerHeatingStartTime) / (1000 * 60) // в минутах
            
            if (heatingDuration >= timeoutMinutes) {
                Log.d(TAG, "Passenger seat heating timeout reached (${heatingDuration}min >= ${timeoutMinutes}min), disabling")
                disablePassengerSeatHeating()
                isPassengerHeatingActive = false
                return
            }
        }
        
        if (isIgnitionOn) {
            if (!isPassengerHeatingActive) {
                Log.d(TAG, "Ignition ON, enabling passenger seat heating")
                enablePassengerSeatHeating(warmLevel)
                isPassengerHeatingActive = true
                passengerHeatingStartTime = System.currentTimeMillis()
            }
        } else {
            if (isPassengerHeatingActive) {
                Log.d(TAG, "Ignition OFF, disabling passenger seat heating")
                disablePassengerSeatHeating()
                isPassengerHeatingActive = false
            }
        }
    }
    
    /**
     * Включает подогрев сиденья водителя
     * @param level уровень подогрева (1 или 2)
     */
    private fun enableDriverSeatHeating(level: Int) {
        try {
            carService?.let { car ->
                when (level) {
                    1 -> {
                        car.setDriverSeatHeatingLevel(1)
                        Log.d(TAG, "Driver seat heating enabled at level 1")
                    }
                    2 -> {
                        car.setDriverSeatHeatingLevel(2)
                        Log.d(TAG, "Driver seat heating enabled at level 2")
                    }
                    else -> {
                        Log.w(TAG, "Invalid driver seat heating level: $level")
                    }
                }
            } ?: Log.w(TAG, "CarService not available for driver seat heating")
        } catch (e: Exception) {
            Log.e(TAG, "Error enabling driver seat heating", e)
        }
    }
    
    /**
     * Выключает подогрев сиденья водителя
     */
    private fun disableDriverSeatHeating() {
        try {
            carService?.setDriverSeatHeatingLevel(0)
            Log.d(TAG, "Driver seat heating disabled")
        } catch (e: Exception) {
            Log.e(TAG, "Error disabling driver seat heating", e)
        }
    }
    
    /**
     * Включает подогрев сиденья пассажира
     * @param level уровень подогрева (1 или 2)
     */
    private fun enablePassengerSeatHeating(level: Int) {
        try {
            carService?.let { car ->
                when (level) {
                    1 -> {
                        car.setPassengerSeatHeatingLevel(1)
                        Log.d(TAG, "Passenger seat heating enabled at level 1")
                    }
                    2 -> {
                        car.setPassengerSeatHeatingLevel(2)
                        Log.d(TAG, "Passenger seat heating enabled at level 2")
                    }
                    else -> {
                        Log.w(TAG, "Invalid passenger seat heating level: $level")
                    }
                }
            } ?: Log.w(TAG, "CarService not available for passenger seat heating")
        } catch (e: Exception) {
            Log.e(TAG, "Error enabling passenger seat heating", e)
        }
    }
    
    /**
     * Выключает подогрев сиденья пассажира
     */
    private fun disablePassengerSeatHeating() {
        try {
            carService?.setPassengerSeatHeatingLevel(0)
            Log.d(TAG, "Passenger seat heating disabled")
        } catch (e: Exception) {
            Log.e(TAG, "Error disabling passenger seat heating", e)
        }
    }
    
    /**
     * Проверяет, включен ли автоподогрев для водителя
     * @return true если включен, false в противном случае
     */
    fun isDriverAutoWarmEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_DRIVER_AUTO_WARM_ENABLED, false)
    }
    
    /**
     * Проверяет, включен ли автоподогрев для пассажира
     * @return true если включен, false в противном случае
     */
    fun isPassengerAutoWarmEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_PASSENGER_AUTO_WARM_ENABLED, false)
    }
    
    /**
     * Проверяет, активен ли мониторинг температуры
     * @return true если активен, false в противном случае
     */
    fun isMonitoring(): Boolean {
        return isMonitoring
    }
    
    /**
     * Проверяет условия автоподогрева и останавливает мониторинг если они не выполняются
     */
    fun checkAutoWarmConditions() {
        val driverEnabled = isDriverAutoWarmEnabled()
        val passengerEnabled = isPassengerAutoWarmEnabled()
        
        if (!driverEnabled && !passengerEnabled) {
            if (isMonitoring) {
                Log.d(TAG, "Auto-warm conditions no longer met, stopping sensor monitoring")
                stopMonitoring()
            }
        } else {
            if (!isMonitoring) {
                Log.d(TAG, "Auto-warm conditions met, starting sensor monitoring")
                startMonitoring()
            }
        }
    }
    
    /**
     * Очищает ресурсы менеджера
     */
    fun cleanup() {
        stopMonitoring()
        sensorService = null
        carService = null
        isDriverHeatingActive = false
        isPassengerHeatingActive = false
        driverHeatingStartTime = 0
        passengerHeatingStartTime = 0
        Log.d(TAG, "AutoWarmManager cleaned up")
    }
}
