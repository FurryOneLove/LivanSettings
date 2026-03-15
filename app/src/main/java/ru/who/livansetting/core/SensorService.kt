package ru.who.livansetting.core

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.ecarx.xui.adaptapi.FunctionStatus
import com.ecarx.xui.adaptapi.car.sensor.ISensor
import com.ecarx.xui.adaptapi.car.sensor.ISensorEvent
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Сервис для обработки показаний датчиков автомобиля
 */
class SensorService(private val context: Context) {
    companion object {
        private const val TAG = "SensorService"
        private const val SENSOR_TYPE_TEMPERATURE_INDOOR = ISensor.SENSOR_TYPE_TEMPERATURE_AMBIENT
        private const val PROP_TEMPERATURE_INDOOR = "ru.who.livansetting.sensor.temperature_indoor"
    }
    
    private var sensor: ISensor? = null
    private var isInitialized = false
    private val isConnected = AtomicBoolean(false)
    private var ignitionStateChangeCallback: ((Int) -> Unit)? = null
    
    private val sensorListener = object : ISensor.ISensorListener {
        override fun onSensorSupportChanged(sensorId: Int, status: FunctionStatus) {}
        override fun onSensorEventChanged(sensorId: Int, event: Int) { onSensorChanged(sensorId, event) }
        override fun onSensorValueChanged(sensorId: Int, value: Float) { onSensorChanged(sensorId, value) }
    }
    
    fun initialize(sensor: ISensor) {
        this.sensor = sensor
        isInitialized = true
    }
    
    fun connect() {
        if (!isInitialized || sensor == null || isConnected.get()) return
        try {
            val registered = sensor?.registerListener(sensorListener, SENSOR_TYPE_TEMPERATURE_INDOOR) ?: false
            if (registered) {
                isConnected.set(true)
                loadStartupValues()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Sensor connect error", e)
        }
    }
    
    fun disconnect() {
        if (!isConnected.get()) return
        try {
            sensor?.unregisterListener(sensorListener)
            isConnected.set(false)
        } catch (e: Exception) {
            Log.e(TAG, "Sensor disconnect error", e)
        }
    }
    
    private fun onSensorChanged(sensorId: Int, value: Any) {
        when (sensorId) {
            SENSOR_TYPE_TEMPERATURE_INDOOR -> handleTemperatureIndoorChange(value)
            ISensor.SENSOR_TYPE_IGNITION_STATE -> handleIgnitionStateChange(value)
        }
    }
    
    private fun handleIgnitionStateChange(value: Any) {
        val state = when (value) {
            is Int -> value
            is Float -> value.toInt()
            else -> return
        }
        ignitionStateChangeCallback?.invoke(state)
    }
    
    private fun handleTemperatureIndoorChange(value: Any) {
        val temp = when (value) {
            is Float -> value
            is Int -> value.toFloat()
            else -> return
        }
        Settings.Global.putFloat(context.contentResolver, PROP_TEMPERATURE_INDOOR, temp)
    }
    
    private fun loadStartupValues() {
        sensor?.getSensorLatestValue(SENSOR_TYPE_TEMPERATURE_INDOOR)?.let {
            if (it is Float) Settings.Global.putFloat(context.contentResolver, PROP_TEMPERATURE_INDOOR, it)
        }
    }
    
    fun isConnected(): Boolean = isConnected.get()
    fun getSensorLatestValue(sensorId: Int): Int? {
        val value = sensor?.getSensorLatestValue(sensorId) ?: return null
        return when (value) {
            is Float -> value.toInt()
            is Int -> value
            else -> null
        }
    }
    
    fun setIgnitionStateChangeCallback(callback: ((Int) -> Unit)?) {
        ignitionStateChangeCallback = callback
    }
    
    fun cleanup() {
        disconnect()
        isInitialized = false
    }
}
