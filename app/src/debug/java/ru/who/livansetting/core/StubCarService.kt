package ru.who.livansetting.core

import android.util.Log

class StubCarService : ICarService {

    companion object {
        private const val TAG = "StubCarService"
    }

    private val functionValues = java.util.concurrent.ConcurrentHashMap<Int, Int>()

    override fun createCar(): Boolean {
        Log.d(TAG, "createCar called (emulator mode)")
        return true
    }

    override fun connectToCarInterface() {
        Log.d(TAG, "connectToCarInterface called (emulator mode)")
    }

    override fun isConnected(): Boolean {
        Log.d(TAG, "isConnected called (emulator mode)")
        return true
    }

    override fun getISensor(): Any? {
        Log.d(TAG, "getISensor called (emulator mode)")
        return null
    }

    override fun getICarFunction(): Any? {
        Log.d(TAG, "getICarFunction called (emulator mode)")
        return null
    }

    override fun getFunctionValue(id: Int): Int? {
        val value = functionValues[id]
        Log.d(TAG, "getFunctionValue called: id=$id value=$value (emulator mode)")
        return value
    }

    override fun setFunctionValue(id: Int, value: Int): Boolean {
        functionValues[id] = value
        Log.d(TAG, "setFunctionValue called: id=$id value=$value (emulator mode)")
        return true
    }

    override fun setDriverSeatHeatingLevel(level: Int): Boolean {
        Log.d(TAG, "setDriverSeatHeatingLevel called: level=$level (emulator mode)")
        return false
    }

    override fun setPassengerSeatHeatingLevel(level: Int): Boolean {
        Log.d(TAG, "setPassengerSeatHeatingLevel called: level=$level (emulator mode)")
        return false
    }

    override fun cleanup() {
        Log.d(TAG, "cleanup called (emulator mode)")
    }
}
