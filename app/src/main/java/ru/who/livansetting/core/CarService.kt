package ru.who.livansetting.core

import android.content.Context
import android.util.Log
import com.ecarx.xui.adaptapi.car.impl.CarImpl
import com.ecarx.xui.adaptapi.car.base.ICarFunction
import com.ecarx.xui.adaptapi.car.sensor.ISensor
import com.ecarx.xui.adaptapi.binder.IConnectable
import com.ecarx.xui.adaptapi.FunctionStatus
import ru.who.livansetting.features.auto.SeatHeatingManager

class CarService(private val context: Context) {
    private var car: CarImpl? = null
    private var carFunction: ICarFunction? = null
    private var sensor: ISensor? = null
    private var isConnected = false
    private var seatHeatingManager: SeatHeatingManager? = null

    fun createCar(): Boolean {
        return try {
            val constructor = CarImpl::class.java.getConstructor(Context::class.java)
            car = constructor.newInstance(context) as CarImpl
            true
        } catch (e: Exception) {
            false
        }
    }

    fun connectToCarInterface() {
        car?.registerConnectWatcher(object : IConnectable.IConnectWatcher {
            override fun onConnected() {
                isConnected = true
                carFunction = car?.getICarFunction()
                sensor = car?.getSensorManager()
                seatHeatingManager = SeatHeatingManager(context)
            }
            override fun onDisConnected() {
                isConnected = false
            }
        })
        car?.connect()
    }

    fun isConnected() = isConnected
    fun getICarFunction() = carFunction
    fun getISensor() = sensor
    
    fun getFunctionValue(id: Int): Int? = carFunction?.getFunctionValue(id)
    fun setFunctionValue(id: Int, value: Int) = carFunction?.setFunctionValue(id, value) ?: false

    fun setDriverSeatHeatingLevel(level: Int) = seatHeatingManager?.setDriverSeatHeat(level) ?: false
    fun setPassengerSeatHeatingLevel(level: Int) = seatHeatingManager?.setPassengerSeatHeat(level) ?: false

    fun cleanup() {
        car?.unregisterConnectWatcher()
        isConnected = false
    }
}
