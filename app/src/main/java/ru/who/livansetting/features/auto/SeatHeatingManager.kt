package ru.who.livansetting.features.auto

import android.content.Context
import android.util.Log
import ru.who.livansetting.core.MainService

/**
 * Менеджер для управления подогревом сидений
 */
class SeatHeatingManager(private val context: Context) {
    companion object {
        private const val TAG = "SeatHeatingManager"
        
        // Function IDs для подогрева сидений (на основе анализа трафика)
        private const val FUNC_ID_DRIVER_SEAT_HEAT = 554700033
        private const val FUNC_ID_PASSENGER_SEAT_HEAT = 554700034
        
        const val HEAT_LEVEL_OFF = 0
        const val HEAT_LEVEL_1 = 1
        const val HEAT_LEVEL_2 = 2
        const val HEAT_LEVEL_3 = 3

        private const val FUNC_ID_SEAT_RANGE_START = 0x21100C00
        private const val FUNC_ID_SEAT_RANGE_END = 0x21100FFF
    }

    private fun getCarService() = MainService.getInstance()?.getCarService()

    fun toggleDriverSeatHeat() {
        val carService = getCarService() ?: return
        val rawValue = carService.getFunctionValue(FUNC_ID_DRIVER_SEAT_HEAT)
        if (rawValue == null) {
            Log.e(TAG, "Driver seat heat: funcId=0x${FUNC_ID_DRIVER_SEAT_HEAT.toString(16)} not supported — run logAvailableSeatFunctions() to find correct ID")
            return
        }
        val nextLevel = (rawValue + 1) % 4
        carService.setFunctionValue(FUNC_ID_DRIVER_SEAT_HEAT, nextLevel)
        Log.d(TAG, "Driver seat heat toggled to: $nextLevel")
    }

    fun togglePassengerSeatHeat() {
        val carService = getCarService() ?: return
        val rawValue = carService.getFunctionValue(FUNC_ID_PASSENGER_SEAT_HEAT)
        if (rawValue == null) {
            Log.e(TAG, "Passenger seat heat: funcId=0x${FUNC_ID_PASSENGER_SEAT_HEAT.toString(16)} not supported — run logAvailableSeatFunctions() to find correct ID")
            return
        }
        val nextLevel = (rawValue + 1) % 4
        carService.setFunctionValue(FUNC_ID_PASSENGER_SEAT_HEAT, nextLevel)
        Log.d(TAG, "Passenger seat heat toggled to: $nextLevel")
    }

    fun setDriverSeatHeat(level: Int) = getCarService()?.setFunctionValue(FUNC_ID_DRIVER_SEAT_HEAT, level) ?: false
    fun setPassengerSeatHeat(level: Int) = getCarService()?.setFunctionValue(FUNC_ID_PASSENGER_SEAT_HEAT, level) ?: false
}
