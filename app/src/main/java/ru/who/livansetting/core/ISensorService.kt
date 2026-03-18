package ru.who.livansetting.core

interface ISensorService {
    fun initialize(sensor: Any)
    fun connect()
    fun disconnect()
    fun isConnected(): Boolean
    fun getSensorLatestValue(sensorId: Int): Int?
    fun setIgnitionStateChangeCallback(callback: ((Int) -> Unit)?)
    fun cleanup()
}
