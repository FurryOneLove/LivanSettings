package ru.who.livansetting.services

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.ecarx.xui.adaptapi.FunctionStatus
import com.ecarx.xui.adaptapi.car.sensor.ISensor
import com.ecarx.xui.adaptapi.car.sensor.ISensorEvent
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Сервис для обработки показаний датчиков автомобиля
 * Отслеживает различные параметры и сохраняет их в системные настройки
 */
class SensorService(private val context: Context) {
    companion object {
        private const val TAG = "SensorService"
        
        // Константы для датчиков
        private const val SENSOR_TYPE_TEMPERATURE_INDOOR = ISensor.SENSOR_TYPE_TEMPERATURE_AMBIENT
        
        // Имена свойств для сохранения в Settings.Global
        private const val PROP_TEMPERATURE_INDOOR = "ru.who.livansetting.sensor.temperature_indoor"
    }
    
    private var sensor: ISensor? = null
    private var isInitialized = false
    private val isConnected = AtomicBoolean(false)
    
    // Callback для отслеживания изменений состояния зажигания
    private var ignitionStateChangeCallback: ((Int) -> Unit)? = null
    
    // Слушатель для датчиков
    private val sensorListener = object : ISensor.ISensorListener {
        override fun onSensorSupportChanged(sensorId: Int, status: FunctionStatus) {
            Log.d(TAG, "Sensor support changed: ID=$sensorId, Status=$status")
        }
        
        override fun onSensorEventChanged(sensorId: Int, event: Int) {
            Log.d(TAG, "Sensor event changed: ID=$sensorId, Event=$event")
            onSensorChanged(sensorId, event)
        }
        
        override fun onSensorValueChanged(sensorId: Int, value: Float) {
            Log.d(TAG, "Sensor value changed: ID=$sensorId, Value=$value")
            onSensorChanged(sensorId, value)
        }
    }
    
    /**
     * Инициализирует сервис датчиков
     * @param sensor экземпляр ISensor для работы с датчиками
     */
    fun initialize(sensor: ISensor) {
        this.sensor = sensor
        isInitialized = true
        Log.d(TAG, "SensorService initialized")
    }
    
    /**
     * Подключается к датчикам и начинает отслеживание
     */
    fun connect() {
        if (!isInitialized || sensor == null) {
            Log.e(TAG, "SensorService not initialized")
            return
        }
        
        if (isConnected.get()) {
            Log.w(TAG, "Already connected to sensors")
            return
        }
        
        Log.d(TAG, "Connecting to sensors...")
        
        try {
            // Регистрируем слушатель для датчика температуры в салоне
            val registered = sensor?.registerListener(sensorListener, SENSOR_TYPE_TEMPERATURE_INDOOR)
            
            if (registered == true) {
                isConnected.set(true)
                Log.d(TAG, "Successfully connected to temperature sensor")
                
                // Загружаем начальные значения
                loadStartupValues()
            } else {
                Log.w(TAG, "Failed to register sensor listener")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error connecting to sensors", e)
        }
    }
    
    /**
     * Отключается от датчиков
     */
    fun disconnect() {
        if (!isConnected.get()) {
            Log.w(TAG, "Not connected to sensors")
            return
        }
        
        Log.d(TAG, "Disconnecting from sensors...")
        
        try {
            sensor?.unregisterListener(sensorListener)
            isConnected.set(false)
            Log.d(TAG, "Successfully disconnected from sensors")
        } catch (e: Exception) {
            Log.e(TAG, "Error disconnecting from sensors", e)
        }
    }
    
    /**
     * Обрабатывает изменения показаний датчиков
     * @param sensorId ID датчика
     * @param value новое значение
     */
    private fun onSensorChanged(sensorId: Int, value: Any) {
        when (sensorId) {
            SENSOR_TYPE_TEMPERATURE_INDOOR -> {
                handleTemperatureIndoorChange(value)
            }
            ISensor.SENSOR_TYPE_IGNITION_STATE -> {
                handleIgnitionStateChange(value)
            }
            else -> {
                Log.d(TAG, "Unknown sensor ID: $sensorId")
            }
        }
    }
    
    /**
     * Обрабатывает изменения состояния зажигания
     * @param value новое значение состояния зажигания
     */
    private fun handleIgnitionStateChange(value: Any) {
        try {
            val ignitionState = when (value) {
                is Int -> value
                is Float -> value.toInt()
                is Double -> value.toInt()
                else -> {
                    Log.w(TAG, "Unexpected ignition state value type: ${value.javaClass.simpleName}")
                    return
                }
            }
            
            Log.d(TAG, "Ignition state changed: $ignitionState")
            
            // Вызываем callback если он установлен
            ignitionStateChangeCallback?.invoke(ignitionState)
        } catch (e: Exception) {
            Log.e(TAG, "Error handling ignition state change", e)
        }
    }
    
    /**
     * Обрабатывает изменения температуры в салоне
     * @param value новое значение температуры
     */
    private fun handleTemperatureIndoorChange(value: Any) {
        try {
            val temperature = when (value) {
                is Float -> value
                is Int -> value.toFloat()
                is Double -> value.toFloat()
                else -> {
                    Log.w(TAG, "Unexpected temperature value type: ${value.javaClass.simpleName}")
                    return
                }
            }
            
            Log.d(TAG, "Temperature indoor changed: $temperature°C")
            
            // Сохраняем значение в системные настройки
            writeSettingsGlobal(PROP_TEMPERATURE_INDOOR, temperature)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error handling temperature change", e)
        }
    }
    
    /**
     * Загружает начальные значения датчиков
     */
    private fun loadStartupValues() {
        if (sensor == null) {
            Log.e(TAG, "Sensor is null, cannot load startup values")
            return
        }
        
        Log.d(TAG, "Loading startup values...")
        
        try {
            // Получаем текущее значение температуры в салоне
            val temperature = sensor?.getSensorLatestValue(SENSOR_TYPE_TEMPERATURE_INDOOR)
            if (temperature != null) {
                Log.d(TAG, "Startup temperature indoor: $temperature°C")
                writeSettingsGlobal(PROP_TEMPERATURE_INDOOR, temperature)
            } else {
                Log.w(TAG, "Could not get startup temperature value")
            }
            
            Log.d(TAG, "Startup values loaded successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading startup values", e)
        }
    }
    
    /**
     * Сохраняет значение в системные настройки
     * @param key ключ настройки
     * @param value значение для сохранения
     */
    private fun writeSettingsGlobal(key: String, value: Any) {
        try {
            Log.d(TAG, "Writing to settings: $key = $value")
            
            when (value) {
                is Int -> {
                    Settings.Global.putInt(context.contentResolver, key, value)
                }
                is Float -> {
                    Settings.Global.putFloat(context.contentResolver, key, value)
                }
                is String -> {
                    Settings.Global.putString(context.contentResolver, key, value)
                }
                is Boolean -> {
                    Settings.Global.putInt(context.contentResolver, key, if (value) 1 else 0)
                }
                else -> {
                    Log.e(TAG, "Unknown value type for key $key: ${value.javaClass.simpleName}")
                }
            }
            
            Log.d(TAG, "Successfully wrote to settings: $key = $value")
        } catch (e: Exception) {
            Log.e(TAG, "Error writing to settings: $key = $value", e)
        }
    }
    
    /**
     * Проверяет, подключен ли сервис к датчикам
     * @return true если подключен, false в противном случае
     */
    fun isConnected(): Boolean {
        return isConnected.get()
    }
    
    /**
     * Проверяет, инициализирован ли сервис
     * @return true если инициализирован, false в противном случае
     */
    fun isInitialized(): Boolean {
        return isInitialized
    }
    
    /**
     * Получает текущее значение температуры в салоне
     * @return значение температуры или null если недоступно
     */
    fun getTemperatureIndoor(): Float? {
        if (!isConnected.get() || sensor == null) {
            Log.w(TAG, "Not connected to sensors")
            return null
        }
        
        return try {
            sensor?.getSensorLatestValue(SENSOR_TYPE_TEMPERATURE_INDOOR)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting temperature indoor", e)
            null
        }
    }
    
    /**
     * Получает текущее значение датчика
     * @param sensorId ID датчика
     * @return значение датчика или null если недоступно
     */
    fun getSensorLatestValue(sensorId: Int): Int? {
        if (!isConnected.get() || sensor == null) {
            Log.w(TAG, "Not connected to sensors")
            return null
        }
        
        return try {
            val value = sensor?.getSensorLatestValue(sensorId)
            when (value) {
                is Float -> value.toInt()
                is Int -> value
                is Double -> value.toInt()
                else -> null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting sensor value for ID: $sensorId", e)
            null
        }
    }
    
    /**
     * Устанавливает callback для отслеживания изменений состояния зажигания
     * @param callback функция, которая будет вызвана при изменении состояния зажигания
     */
    fun setIgnitionStateChangeCallback(callback: ((Int) -> Unit)?) {
        ignitionStateChangeCallback = callback
    }
    
    /**
     * Очищает ресурсы сервиса
     */
    fun cleanup() {
        disconnect()
        sensor = null
        isInitialized = false
        isConnected.set(false)
        ignitionStateChangeCallback = null
        Log.d(TAG, "SensorService cleaned up")
    }
}
