package ru.who.livansetting.core

import android.util.Log

class StubSensorService : ISensorService {

    companion object {
        private const val TAG = "StubSensorService"
    }

    override fun initialize(sensor: Any) {
        Log.d(TAG, "initialize called (emulator mode)")
    }

    override fun connect() {
        Log.d(TAG, "connect called (emulator mode)")
    }

    override fun disconnect() {
        Log.d(TAG, "disconnect called (emulator mode)")
    }

    override fun isConnected(): Boolean {
        Log.d(TAG, "isConnected called (emulator mode)")
        return false
    }

    override fun getSensorLatestValue(sensorId: Int): Int? {
        Log.d(TAG, "getSensorLatestValue called: sensorId=$sensorId (emulator mode)")
        return null
    }

    override fun setIgnitionStateChangeCallback(callback: ((Int) -> Unit)?) {
        Log.d(TAG, "setIgnitionStateChangeCallback called (emulator mode)")
    }

    override fun cleanup() {
        Log.d(TAG, "cleanup called (emulator mode)")
    }
}
