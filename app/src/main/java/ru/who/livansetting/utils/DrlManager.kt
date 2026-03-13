package ru.who.livansetting.utils

import android.content.Context
import android.util.Log
import ru.who.livansetting.services.MainService
import com.ecarx.xui.adaptapi.car.base.ICarFunction

/**
 * Менеджер для управления дневными ходовыми огнями (ДХО)
 */
class DrlManager(private val context: Context) {
    
    companion object {
        private const val TAG = "DrlManager"
        private const val DRL_FUNCTION_ID = 537135104
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
     * Получает текущее состояние ДХО
     * @return true если ДХО включены, false если выключены, null если ошибка
     */
    fun getDrlState(): Boolean? {
        if (!ensureCarConnection()) {
            Log.e(TAG, "Failed to establish car connection for getDrlState")
            return null
        }
        
        val iCarFunction = getICarFunction()
        if (iCarFunction == null) {
            Log.e(TAG, "getDrlState iCarFunction == null")
            return null
        }
        
        return try {
            iCarFunction.getFunctionValue(DRL_FUNCTION_ID) == 1
        } catch (e: Exception) {
            Log.e(TAG, "Error getting DRL state", e)
            null
        }
    }
    
    /**
     * Включает ДХО
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun enableDrl(): Boolean {
        if (!ensureCarConnection()) {
            Log.e(TAG, "Failed to establish car connection for enableDrl")
            return false
        }
        
        val iCarFunction = getICarFunction()
        if (iCarFunction == null) {
            Log.e(TAG, "enableDrl iCarFunction == null")
            return false
        }
        
        val currentState = getDrlState()
        if (currentState == true) {
            Log.d(TAG, "DRL already enabled")
            return true
        }
        
        return try {
            val result = iCarFunction.setFunctionValue(DRL_FUNCTION_ID, 1)
            Log.i(TAG, "Enable DRL result: $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error enabling DRL", e)
            false
        }
    }
    
    /**
     * Выключает ДХО
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun disableDrl(): Boolean {
        if (!ensureCarConnection()) {
            Log.e(TAG, "Failed to establish car connection for disableDrl")
            return false
        }
        
        val iCarFunction = getICarFunction()
        if (iCarFunction == null) {
            Log.e(TAG, "disableDrl iCarFunction == null")
            return false
        }
        
        val currentState = getDrlState()
        if (currentState == false) {
            Log.d(TAG, "DRL already disabled")
            return true
        }
        
        return try {
            val result = iCarFunction.setFunctionValue(DRL_FUNCTION_ID, 0)
            Log.i(TAG, "Disable DRL result: $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error disabling DRL", e)
            false
        }
    }
    
    /**
     * Переключает состояние дневных ходовых огней (ДХО)
     */
    fun toggleDrl() {
        try {
            Log.d(TAG, "Toggling DRL state")
            
            // Убеждаемся, что подключение к автомобилю установлено
            if (!ensureCarConnection()) {
                Log.e(TAG, "Failed to establish car connection, cannot toggle DRL")
                return
            }
            
            val iCarFunction = getICarFunction()
            if (iCarFunction == null) {
                Log.e(TAG, "toggleDrl iCarFunction == null after connection attempt")
                return
            }
            
            // Проверяем, что функция DRL поддерживается
            if (!isDrlFunctionSupported(iCarFunction)) {
                Log.e(TAG, "DRL function is not supported")
                return
            }
            
            val currentState = iCarFunction.getFunctionValue(DRL_FUNCTION_ID) == 1
            val newState = !currentState
            val result = iCarFunction.setFunctionValue(DRL_FUNCTION_ID, if (newState) 1 else 0)
            Log.i(TAG, "Toggle DRL from $currentState to $newState, result=$result")
        } catch (e: Exception) {
            Log.e(TAG, "Error toggling DRL", e)
        }
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
     * Проверяет, поддерживается ли функция DRL
     */
    private fun isDrlFunctionSupported(iCarFunction: ICarFunction): Boolean {
        return try {
            val supportStatus = iCarFunction.isFunctionSupported(DRL_FUNCTION_ID)
            Log.d(TAG, "DRL function support status: $supportStatus")
            supportStatus != null
        } catch (e: Exception) {
            Log.e(TAG, "Error checking DRL function support", e)
            false
        }
    }
    
    /**
     * Устанавливает состояние ДХО
     * @param enabled true для включения, false для выключения
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun setDrlState(enabled: Boolean): Boolean {
        if (!ensureCarConnection()) {
            Log.e(TAG, "Failed to establish car connection for setDrlState")
            return false
        }
        
        val iCarFunction = getICarFunction()
        if (iCarFunction == null) {
            Log.e(TAG, "setDrlState iCarFunction == null")
            return false
        }
        
        return try {
            val result = iCarFunction.setFunctionValue(DRL_FUNCTION_ID, if (enabled) 1 else 0)
            Log.i(TAG, "setDrlState enabled=$enabled, result=$result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error setting DRL state", e)
            false
        }
    }
    
    /**
     * Получает строковое представление текущего состояния ДХО
     */
    fun getDrlStateString(): String {
        return when (getDrlState()) {
            true -> "On"
            false -> "Of"
            null -> "Unknown"
        }
    }
}
