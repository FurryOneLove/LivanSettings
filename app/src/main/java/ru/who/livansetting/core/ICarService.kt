package ru.who.livansetting.core

interface ICarService {
    fun createCar(): Boolean
    fun connectToCarInterface()
    fun isConnected(): Boolean
    fun getISensor(): Any?
    fun getICarFunction(): Any?
    fun getFunctionValue(id: Int): Int?
    fun setFunctionValue(id: Int, value: Int): Boolean
    fun setDriverSeatHeatingLevel(level: Int): Boolean
    fun setPassengerSeatHeatingLevel(level: Int): Boolean
    fun cleanup()
}
