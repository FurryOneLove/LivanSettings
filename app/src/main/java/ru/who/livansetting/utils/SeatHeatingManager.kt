package ru.who.livansetting.utils

import android.content.Context
import android.util.Log
import ru.who.livansetting.services.MainService
import com.ecarx.xui.adaptapi.car.base.ICarFunction
import com.ecarx.xui.adaptapi.car.hvac.IHvac

/**
 * Менеджер для управления подогревом сидений
 * 
 * Пример использования:
 * ```kotlin
 * val seatHeatingManager = SeatHeatingManager(context)
 * 
 * // Установить подогрев водительского сиденья на уровень 2
 * seatHeatingManager.setDriverSeatHeat(2)
 * 
 * // Установить подогрев пассажирского сиденья на уровень 1
 * seatHeatingManager.setPassengerSeatHeat(1)
 * 
 * // Установить подогрев всех сидений на уровень 3
 * seatHeatingManager.setAllSeatsHeat(3)
 * 
 * // Переключить подогрев водительского сиденья
 * seatHeatingManager.toggleDriverSeatHeat()
 * 
 * // Выключить подогрев всех сидений
 * seatHeatingManager.turnOffAllSeatsHeat()
 * 
 * // Получить текущий уровень подогрева
 * val driverLevel = seatHeatingManager.getDriverSeatHeatLevel()
 * val passengerLevel = seatHeatingManager.getPassengerSeatHeatLevel()
 * 
 * // Получить строковое представление состояния
 * val driverState = seatHeatingManager.getDriverSeatHeatStateString()
 * val passengerState = seatHeatingManager.getPassengerSeatHeatStateString()
 * ```
 */
class SeatHeatingManager(private val context: Context) {
    
    companion object {
        private const val TAG = "SeatHeatingManager"
        
        // ID функции подогрева сидений
        private const val HVAC_FUNC_SEAT_HEATING = IHvac.HVAC_FUNC_SEAT_HEATING
        
        // Зоны сидений
        private const val DRIVER_SEAT_ZONE = 1      // Водительское сиденье
        private const val PASSENGER_SEAT_ZONE = 4   // Пассажирское сиденье
        
        // Уровни подогрева сидений
        private val SEAT_HEATING_LEVELS = intArrayOf(
            0,                                    // Выключено
            IHvac.SEAT_HEATING_LEVEL_1,          // Уровень 1
            IHvac.SEAT_HEATING_LEVEL_2,          // Уровень 2
            IHvac.SEAT_HEATING_LEVEL_3,          // Уровень 3
            IHvac.SEAT_HEATING_LEVEL_AUTO        // Автоматический режим
        )
    }
    
    // Ссылка на MainService для доступа к CarService
    private val mainService: MainService? get() = MainService.getInstance()
    
    /**
     * Получает экземпляр ICarFunction для прямого доступа
     * @return ICarFunction или null если недоступен
     */
    private fun getICarFunction(): ICarFunction? {
        return mainService?.getCarService()?.getICarFunction()
    }
    
    /**
     * Создает объект Car через CarService
     * @return true если создание успешно, false в случае ошибки
     */
    fun createCar(): Boolean {
        return mainService?.getCarService()?.createCar() ?: false
    }
    
    /**
     * Подключается к автомобилю через CarService
     * @return true если подключение инициировано, false в случае ошибки
     */
    fun connectToCar(): Boolean {
        return mainService?.getCarService()?.connectToCarInterface() ?: false
    }
    
    /**
     * Проверяет, подключен ли CarService к автомобилю
     */
    fun isCarConnected(): Boolean {
        return mainService?.getCarService()?.isConnected() ?: false
    }
    
    /**
     * Убеждается, что подключение к автомобилю установлено
     * @return true если подключение успешно, false в случае ошибки
     */
    private fun ensureCarConnection(): Boolean {
        if (isCarConnected()) {
            return true
        }
        
        Log.w(TAG, "CarService is not connected to car, attempting to create and connect...")
        
        // Сначала создаем объект Car
        if (!createCar()) {
            Log.e(TAG, "Failed to create Car object")
            return false
        }
        
        // Затем подключаемся
        if (!connectToCar()) {
            Log.e(TAG, "Failed to connect to car")
            return false
        }
        
        // Дополнительная проверка после подключения
        if (!isCarConnected()) {
            Log.e(TAG, "Car connection verification failed")
            return false
        }
        
        return true
    }
    
    /**
     * Проверяет, поддерживается ли функция подогрева сидений
     */
    private fun isSeatHeatingFunctionSupported(iCarFunction: ICarFunction): Boolean {
        return try {
            val supportStatus = iCarFunction.isFunctionSupported(HVAC_FUNC_SEAT_HEATING)
            Log.d(TAG, "Seat heating function support status: $supportStatus")
            supportStatus != null
        } catch (e: Exception) {
            Log.e(TAG, "Error checking seat heating function support", e)
            false
        }
    }
    
    /**
     * Устанавливает уровень подогрева водительского сиденья
     * @param heatLevel уровень подогрева (0-4, где 0 = выключено, 4 = автоматический режим)
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun setDriverSeatHeat(heatLevel: Int): Boolean {
        Log.d(TAG, "setDriverSeatHeat heatLevel: $heatLevel")
        
        if (!ensureCarConnection()) {
            Log.e(TAG, "Failed to establish car connection for setDriverSeatHeat")
            return false
        }
        
        val iCarFunction = getICarFunction()
        if (iCarFunction == null) {
            Log.e(TAG, "setDriverSeatHeat iCarFunction == null")
            return false
        }
        
        if (!isSeatHeatingFunctionSupported(iCarFunction)) {
            Log.e(TAG, "Seat heating function is not supported")
            return false
        }
        
        if (heatLevel < 0 || heatLevel >= SEAT_HEATING_LEVELS.size) {
            Log.e(TAG, "Invalid heat level: $heatLevel (must be 0-${SEAT_HEATING_LEVELS.size - 1})")
            return false
        }
        
        return try {
            val result = iCarFunction.setFunctionValue(HVAC_FUNC_SEAT_HEATING, DRIVER_SEAT_ZONE, SEAT_HEATING_LEVELS[heatLevel])
            Log.i(TAG, "setDriverSeatHeat level=$heatLevel, result=$result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error setting driver seat heat", e)
            false
        }
    }
    
    /**
     * Устанавливает уровень подогрева пассажирского сиденья
     * @param heatLevel уровень подогрева (0-4, где 0 = выключено, 4 = автоматический режим)
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun setPassengerSeatHeat(heatLevel: Int): Boolean {
        Log.d(TAG, "setPassengerSeatHeat heatLevel: $heatLevel")
        
        if (!ensureCarConnection()) {
            Log.e(TAG, "Failed to establish car connection for setPassengerSeatHeat")
            return false
        }
        
        val iCarFunction = getICarFunction()
        if (iCarFunction == null) {
            Log.e(TAG, "setPassengerSeatHeat iCarFunction == null")
            return false
        }
        
        if (!isSeatHeatingFunctionSupported(iCarFunction)) {
            Log.e(TAG, "Seat heating function is not supported")
            return false
        }
        
        if (heatLevel < 0 || heatLevel >= SEAT_HEATING_LEVELS.size) {
            Log.e(TAG, "Invalid heat level: $heatLevel (must be 0-${SEAT_HEATING_LEVELS.size - 1})")
            return false
        }
        
        return try {
            val result = iCarFunction.setFunctionValue(HVAC_FUNC_SEAT_HEATING, PASSENGER_SEAT_ZONE, SEAT_HEATING_LEVELS[heatLevel])
            Log.i(TAG, "setPassengerSeatHeat level=$heatLevel, result=$result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error setting passenger seat heat", e)
            false
        }
    }
    
    /**
     * Устанавливает уровень подогрева для всех сидений одновременно
     * @param heatLevel уровень подогрева (0-4, где 0 = выключено, 4 = автоматический режим)
     * @return true если команды выполнены успешно, false в случае ошибки
     */
    fun setAllSeatsHeat(heatLevel: Int): Boolean {
        Log.d(TAG, "setAllSeatsHeat heatLevel: $heatLevel")
        
        if (heatLevel < 0 || heatLevel >= SEAT_HEATING_LEVELS.size) {
            Log.e(TAG, "Invalid heat level: $heatLevel (must be 0-${SEAT_HEATING_LEVELS.size - 1})")
            return false
        }
        
        val driverResult = setDriverSeatHeat(heatLevel)
        val passengerResult = setPassengerSeatHeat(heatLevel)
        
        val allSuccess = driverResult && passengerResult
        Log.i(TAG, "setAllSeatsHeat level=$heatLevel, driverResult=$driverResult, passengerResult=$passengerResult, allSuccess=$allSuccess")
        
        return allSuccess
    }
    
    /**
     * Получает текущий уровень подогрева водительского сиденья
     * @return уровень подогрева или null в случае ошибки
     */
    fun getDriverSeatHeatLevel(): Int? {
        if (!ensureCarConnection()) {
            Log.e(TAG, "Failed to establish car connection for getDriverSeatHeatLevel")
            return null
        }
        
        val iCarFunction = getICarFunction()
        if (iCarFunction == null) {
            Log.e(TAG, "getDriverSeatHeatLevel iCarFunction == null")
            return null
        }
        
        return try {
            val value = iCarFunction.getFunctionValue(HVAC_FUNC_SEAT_HEATING, DRIVER_SEAT_ZONE)
            val level = SEAT_HEATING_LEVELS.indexOf(value)
            Log.d(TAG, "getDriverSeatHeatLevel value=$value, level=$level")
            if (level >= 0) level else null
        } catch (e: Exception) {
            Log.e(TAG, "Error getting driver seat heat level", e)
            null
        }
    }
    
    /**
     * Получает текущий уровень подогрева пассажирского сиденья
     * @return уровень подогрева или null в случае ошибки
     */
    fun getPassengerSeatHeatLevel(): Int? {
        if (!ensureCarConnection()) {
            Log.e(TAG, "Failed to establish car connection for getPassengerSeatHeatLevel")
            return null
        }
        
        val iCarFunction = getICarFunction()
        if (iCarFunction == null) {
            Log.e(TAG, "getPassengerSeatHeatLevel iCarFunction == null")
            return null
        }
        
        return try {
            val value = iCarFunction.getFunctionValue(HVAC_FUNC_SEAT_HEATING, PASSENGER_SEAT_ZONE)
            val level = SEAT_HEATING_LEVELS.indexOf(value)
            Log.d(TAG, "getPassengerSeatHeatLevel value=$value, level=$level")
            if (level >= 0) level else null
        } catch (e: Exception) {
            Log.e(TAG, "Error getting passenger seat heat level", e)
            null
        }
    }
    
    /**
     * Переключает подогрев водительского сиденья (включает/выключает)
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun toggleDriverSeatHeat(): Boolean {
        Log.d(TAG, "Toggling driver seat heat")
        
        val currentLevel = getDriverSeatHeatLevel()
        if (currentLevel == null) {
            Log.e(TAG, "Cannot get current driver seat heat level")
            return false
        }
        
        val newLevel = if (currentLevel == 0) 1 else 0 // Переключаем между выключено и уровень 1
        return setDriverSeatHeat(newLevel)
    }
    
    /**
     * Переключает подогрев пассажирского сиденья (включает/выключает)
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun togglePassengerSeatHeat(): Boolean {
        Log.d(TAG, "Toggling passenger seat heat")
        
        val currentLevel = getPassengerSeatHeatLevel()
        if (currentLevel == null) {
            Log.e(TAG, "Cannot get current passenger seat heat level")
            return false
        }
        
        val newLevel = if (currentLevel == 0) 1 else 0 // Переключаем между выключено и уровень 1
        return setPassengerSeatHeat(newLevel)
    }
    
    /**
     * Переключает подогрев всех сидений (включает/выключает)
     * @return true если команды выполнены успешно, false в случае ошибки
     */
    fun toggleAllSeatsHeat(): Boolean {
        Log.d(TAG, "Toggling all seats heat")
        
        val driverLevel = getDriverSeatHeatLevel()
        val passengerLevel = getPassengerSeatHeatLevel()
        
        if (driverLevel == null || passengerLevel == null) {
            Log.e(TAG, "Cannot get current seat heat levels")
            return false
        }
        
        // Если хотя бы одно сиденье включено, выключаем все, иначе включаем все на уровень 1
        val newLevel = if (driverLevel > 0 || passengerLevel > 0) 0 else 1
        return setAllSeatsHeat(newLevel)
    }
    
    /**
     * Выключает подогрев всех сидений
     * @return true если команды выполнены успешно, false в случае ошибки
     */
    fun turnOffAllSeatsHeat(): Boolean {
        Log.d(TAG, "Turning off all seats heat")
        return setAllSeatsHeat(0)
    }
    
    /**
     * Получает строковое представление уровня подогрева
     * @param level уровень подогрева (0-4)
     * @return строковое представление уровня
     */
    fun getHeatLevelString(level: Int): String {
        return when (level) {
            0 -> "Off"
            1 -> "Level 1"
            2 -> "Level 2"
            3 -> "Level 3"
            4 -> "Auto"
            else -> "Unknown"
        }
    }
    
    /**
     * Получает строковое представление текущего состояния подогрева водительского сиденья
     */
    fun getDriverSeatHeatStateString(): String {
        val level = getDriverSeatHeatLevel()
        return if (level != null) getHeatLevelString(level) else "Unknown"
    }
    
    /**
     * Получает строковое представление текущего состояния подогрева пассажирского сиденья
     */
    fun getPassengerSeatHeatStateString(): String {
        val level = getPassengerSeatHeatLevel()
        return if (level != null) getHeatLevelString(level) else "Unknown"
    }
}
