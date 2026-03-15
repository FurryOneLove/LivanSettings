package ru.who.livansetting.features.auto

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import ru.who.livansetting.core.SensorService
import ru.who.livansetting.core.CarService
import com.ecarx.xui.adaptapi.car.sensor.ISensor
import com.ecarx.xui.adaptapi.car.sensor.ISensorEvent

/**
 * Менеджер для управления автоматическим подогревом сидений
 */
class AutoWarmManager(private val context: Context) {
    
    companion object {
        private const val TAG = "AutoWarmManager"
        private const val PREFS_NAME = "livan_settings"
        private const val KEY_DRIVER_AUTO_WARM_ENABLED = "driver_auto_warm_enabled"
        private const val KEY_DRIVER_WARM_LEVEL = "driver_warm_level"
        private const val KEY_DRIVER_TIMEOUT_MINUTES = "driver_timeout_minutes"
        private const val KEY_PASSENGER_AUTO_WARM_ENABLED = "passenger_auto_warm_enabled"
        private const val KEY_PASSENGER_WARM_LEVEL = "passenger_warm_level"
        private const val KEY_PASSENGER_TIMEOUT_MINUTES = "passenger_timeout_minutes"
        
        private const val TEMPERATURE_CHECK_INTERVAL = 30000L
    }
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private var sensorService: SensorService? = null
    private var carService: CarService? = null
    private var isMonitoring = false
    private var temperatureCheckRunnable: Runnable? = null
    private val handler = android.os.Handler(android.os.Looper.getMainLooper())
    
    private var driverHeatingStartTime: Long = 0
    private var passengerHeatingStartTime: Long = 0
    private var isDriverHeatingActive = false
    private var isPassengerHeatingActive = false
    
    fun initialize(sensorService: SensorService?, carService: CarService?) {
        this.sensorService = sensorService
        this.carService = carService
    }
    
    fun startMonitoring() {
        if (isMonitoring) return
        if (sensorService == null || carService == null || !sensorService!!.isConnected()) return
        
        val driverEnabled = isDriverAutoWarmEnabled()
        val passengerEnabled = isPassengerAutoWarmEnabled()
        if (!driverEnabled && !passengerEnabled) return
        
        isMonitoring = true
        temperatureCheckRunnable = object : Runnable {
            override fun run() {
                if (isMonitoring) {
                    checkTemperatureAndControlHeating()
                    handler.postDelayed(this, TEMPERATURE_CHECK_INTERVAL)
                }
            }
        }
        handler.postDelayed(temperatureCheckRunnable!!, 5000)
    }
    
    fun stopMonitoring() {
        isMonitoring = false
        temperatureCheckRunnable?.let { handler.removeCallbacks(it) }
        temperatureCheckRunnable = null
    }
    
    private fun checkTemperatureAndControlHeating() {
        checkAutoWarmConditions()
        if (!isMonitoring) return
        
        val ignitionState = sensorService?.getSensorLatestValue(ISensor.SENSOR_TYPE_IGNITION_STATE)
        val isIgnitionOn = ignitionState == ISensorEvent.IGNITION_STATE_DRIVING || ignitionState == ISensorEvent.IGNITION_STATE_ACC
        
        checkSeatHeating(true, isIgnitionOn)
        checkSeatHeating(false, isIgnitionOn)
    }

    private fun checkSeatHeating(isDriver: Boolean, isIgnitionOn: Boolean) {
        val keyEnabled = if (isDriver) KEY_DRIVER_AUTO_WARM_ENABLED else KEY_PASSENGER_AUTO_WARM_ENABLED
        val isEnabled = sharedPreferences.getBoolean(keyEnabled, false)
        
        var isActive = if (isDriver) isDriverHeatingActive else isPassengerHeatingActive
        val startTime = if (isDriver) driverHeatingStartTime else passengerHeatingStartTime

        if (!isEnabled) {
            if (isActive) {
                if (isDriver) disableDriverSeatHeating() else disablePassengerSeatHeating()
                if (isDriver) isDriverHeatingActive = false else isPassengerHeatingActive = false
            }
            return
        }

        val warmLevel = sharedPreferences.getInt(if (isDriver) KEY_DRIVER_WARM_LEVEL else KEY_PASSENGER_WARM_LEVEL, 1)
        val timeoutMinutes = sharedPreferences.getInt(if (isDriver) KEY_DRIVER_TIMEOUT_MINUTES else KEY_PASSENGER_TIMEOUT_MINUTES, 30)

        if (isActive) {
            val duration = (System.currentTimeMillis() - startTime) / (1000 * 60)
            if (duration >= timeoutMinutes) {
                if (isDriver) disableDriverSeatHeating() else disablePassengerSeatHeating()
                if (isDriver) isDriverHeatingActive = false else isPassengerHeatingActive = false
                return
            }
        }

        if (isIgnitionOn) {
            if (!isActive) {
                if (isDriver) enableDriverSeatHeating(warmLevel) else enablePassengerSeatHeating(warmLevel)
                if (isDriver) isDriverHeatingActive = true else isPassengerHeatingActive = true
                if (isDriver) driverHeatingStartTime = System.currentTimeMillis() else passengerHeatingStartTime = System.currentTimeMillis()
            }
        } else {
            if (isActive) {
                if (isDriver) disableDriverSeatHeating() else disablePassengerSeatHeating()
                if (isDriver) isDriverHeatingActive = false else isPassengerHeatingActive = false
            }
        }
    }
    
    private fun enableDriverSeatHeating(level: Int) = carService?.setDriverSeatHeatingLevel(level)
    private fun disableDriverSeatHeating() = carService?.setDriverSeatHeatingLevel(0)
    private fun enablePassengerSeatHeating(level: Int) = carService?.setPassengerSeatHeatingLevel(level)
    private fun disablePassengerSeatHeating() = carService?.setPassengerSeatHeatingLevel(0)
    
    fun isDriverAutoWarmEnabled() = sharedPreferences.getBoolean(KEY_DRIVER_AUTO_WARM_ENABLED, false)
    fun isPassengerAutoWarmEnabled() = sharedPreferences.getBoolean(KEY_PASSENGER_AUTO_WARM_ENABLED, false)
    fun isMonitoring() = isMonitoring
    
    fun checkAutoWarmConditions() {
        if (!isDriverAutoWarmEnabled() && !isPassengerAutoWarmEnabled()) {
            if (isMonitoring) stopMonitoring()
        } else if (!isMonitoring) {
            startMonitoring()
        }
    }
    
    fun cleanup() {
        stopMonitoring()
        sensorService = null
        carService = null
        isDriverHeatingActive = false
        isPassengerHeatingActive = false
    }
}
