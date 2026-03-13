package ru.who.livansetting.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.ecarx.xui.adaptapi.car.base.ICarFunction
import com.ecarx.xui.adaptapi.car.vehicle.IVehicle
import ru.who.livansetting.services.MainService

/**
 * Менеджер для управления провожающим светом
 */
class WelcomeLightManager(private val context: Context) {
    
    companion object {
        private const val TAG = "WelcomeLightManager"
        // ID функции для провожающего света в автомобильной системе
        private const val WELCOME_LIGHT_FUNCTION_ID = 537134848
        // Ключи для SharedPreferences
        private const val PREFS_NAME = "welcome_light_prefs"
        private const val KEY_SAVED_VALUE = "saved_value"
        
        // Предопределенные значения для провожающего света
        private val WELCOME_LIGHT_VALUES = arrayOf(
            IVehicle.HOME_SAFE_LIGHT_VALUE_OFF,
            IVehicle.HOME_SAFE_LIGHT_VALUE_30S,
            IVehicle.HOME_SAFE_LIGHT_VALUE_60S,
            IVehicle.HOME_SAFE_LIGHT_VALUE_90S
        )
        
        // Сопоставление значений с их описанием
        private val VALUE_DESCRIPTIONS = mapOf(
            IVehicle.HOME_SAFE_LIGHT_VALUE_OFF to "Off",
            IVehicle.HOME_SAFE_LIGHT_VALUE_30S to "30s",
            IVehicle.HOME_SAFE_LIGHT_VALUE_60S to "60s",
            IVehicle.HOME_SAFE_LIGHT_VALUE_90S to "90s"
        )
    }
    
    // Ссылка на MainService для доступа к CarService
    private val mainService: MainService? get() = MainService.getInstance()
    
    // SharedPreferences для сохранения предыдущего значения длительности
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
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
     * Вспомогательный метод для получения ICarFunction с проверками
     * @return ICarFunction если все проверки пройдены, null в случае ошибки
     */
    private fun getICarFunctionWithChecks(): ICarFunction? {
        val carService = mainService?.getCarService()
        if (carService == null) {
            Log.w(TAG, "CarService not available through MainService")
            return null
        }
        
        if (!isCarConnected()) {
            Log.w(TAG, "CarService is not connected to car")
            return null
        }
        
        val iCarFunction = carService.getICarFunction()
        if (iCarFunction == null) {
            Log.e(TAG, "ICarFunction is null")
            return null
        }
        
        return iCarFunction
    }
    
    /**
     * Получает текущее значение провожающего света
     * @return значение провожающего света (константа из IVehicle), null если ошибка
     */
    fun getWelcomeLightValue(): Int? {
        val iCarFunction = getICarFunctionWithChecks() ?: return null
        
        return try {
            iCarFunction.getFunctionValue(WELCOME_LIGHT_FUNCTION_ID)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting welcome light value", e)
            null
        }
    }
    
    /**
     * Устанавливает значение провожающего света
     * @param value значение из IVehicle (HOME_SAFE_LIGHT_VALUE_*)
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun setWelcomeLightValue(value: Int): Boolean {
        if (!WELCOME_LIGHT_VALUES.contains(value)) {
            Log.w(TAG, "Invalid value: $value (must be one of ${WELCOME_LIGHT_VALUES.joinToString()})")
            return false
        }
        
        val iCarFunction = getICarFunctionWithChecks() ?: return false
        
        return try {
            val result = iCarFunction.setFunctionValue(WELCOME_LIGHT_FUNCTION_ID, value)
            Log.i(TAG, "setWelcomeLightValue value=$value (${VALUE_DESCRIPTIONS[value]}), result=$result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error setting welcome light value", e)
            false
        }
    }
    
    /**
     * Переключает состояние провожающего света циклически
     * Переходит к следующему значению в порядке: OFF -> 30s -> 60s -> 90s -> OFF
     */
    fun toggleWelcomeLight() {
        try {
            Log.d(TAG, "Toggling welcome light state")
            
            // Попытка подключения если не подключен
            if (!isCarConnected()) {
                Log.w(TAG, "CarService is not connected to car, attempting to create and connect...")
                
                // Сначала создаем объект Car
                if (!createCar()) {
                    Log.e(TAG, "Failed to create Car object, cannot toggle welcome light")
                    return
                }
                
                // Затем подключаемся
                if (!connectToCar()) {
                    Log.e(TAG, "Failed to connect to car, cannot toggle welcome light")
                    return
                }
            }
            
            val currentValue = getWelcomeLightValue()
            if (currentValue == null) {
                Log.e(TAG, "Cannot get current welcome light value")
                return
            }
            
            // Находим текущий индекс в массиве значений
            val currentIndex = WELCOME_LIGHT_VALUES.indexOf(currentValue)
            if (currentIndex == -1) {
                Log.w(TAG, "Current value $currentValue not found in predefined values, defaulting to OFF")
                val result = setWelcomeLightValue(IVehicle.HOME_SAFE_LIGHT_VALUE_OFF)
                Log.i(TAG, "Set welcome light to OFF, result: $result")
                return
            }
            
            // Переходим к следующему значению (циклически)
            val nextIndex = (currentIndex + 1) % WELCOME_LIGHT_VALUES.size
            val nextValue = WELCOME_LIGHT_VALUES[nextIndex]
            
            val result = setWelcomeLightValue(nextValue)
            Log.i(TAG, "Toggle welcome light from ${VALUE_DESCRIPTIONS[currentValue]} to ${VALUE_DESCRIPTIONS[nextValue]}, result: $result")
        } catch (e: Exception) {
            Log.e(TAG, "Error toggling welcome light", e)
        }
    }
    
    /**
     * Проверяет, включен ли провожающий свет
     * @return true если включен (не OFF), false если выключен, null если ошибка
     */
    fun isWelcomeLightEnabled(): Boolean? {
        val value = getWelcomeLightValue()
        return if (value != null) value != IVehicle.HOME_SAFE_LIGHT_VALUE_OFF else null
    }
    
    /**
     * Получает строковое представление текущего состояния провожающего света
     * @param showDuration true для отображения времени в секундах, false для простого On/Off
     */
    fun getWelcomeLightStateString(showDuration: Boolean = true): String {
        val value = getWelcomeLightValue()
        return when {
            value == null -> "Unknown"
            value == IVehicle.HOME_SAFE_LIGHT_VALUE_OFF -> "Off"
            showDuration -> VALUE_DESCRIPTIONS[value] ?: "On"
            else -> "On"
        }
    }
    
    /**
     * Устанавливает состояние провожающего света (включен/выключен)
     * @param enabled true для включения на 30 секунд по умолчанию, false для выключения
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun setWelcomeLightEnabled(enabled: Boolean): Boolean {
        return setWelcomeLightValue(if (enabled) IVehicle.HOME_SAFE_LIGHT_VALUE_30S else IVehicle.HOME_SAFE_LIGHT_VALUE_OFF)
    }
    
    /**
     * Сохраняет текущее значение провожающего света
     * @param value значение для сохранения (константа из IVehicle)
     */
    private fun saveWelcomeLightValue(value: Int) {
        prefs.edit().putInt(KEY_SAVED_VALUE, value).apply()
        Log.d(TAG, "Saved welcome light value: $value (${VALUE_DESCRIPTIONS[value]})")
    }
    
    /**
     * Получает сохраненное значение провожающего света
     * @return сохраненное значение или 30 секунд по умолчанию
     */
    private fun getSavedWelcomeLightValue(): Int {
        return prefs.getInt(KEY_SAVED_VALUE, IVehicle.HOME_SAFE_LIGHT_VALUE_30S) // 30 секунд по умолчанию
    }
    
    /**
     * Выключает провожающий свет с сохранением текущего значения
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun disableWelcomeLightWithSave(): Boolean {
        val currentValue = getWelcomeLightValue()
        if (currentValue != null && currentValue != IVehicle.HOME_SAFE_LIGHT_VALUE_OFF) {
            // Сохраняем текущее значение только если оно не OFF
            saveWelcomeLightValue(currentValue)
            Log.d(TAG, "Saving current value before disabling: $currentValue (${VALUE_DESCRIPTIONS[currentValue]})")
        }
        
        val result = setWelcomeLightValue(IVehicle.HOME_SAFE_LIGHT_VALUE_OFF)
        Log.i(TAG, "Disabled welcome light, saved value: $currentValue, result: $result")
        return result
    }
    
    /**
     * Включает провожающий свет с восстановлением сохраненного значения
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun enableWelcomeLightWithRestore(): Boolean {
        val savedValue = getSavedWelcomeLightValue()
        val result = setWelcomeLightValue(savedValue)
        Log.i(TAG, "Enabled welcome light with restored value: $savedValue (${VALUE_DESCRIPTIONS[savedValue]}), result: $result")
        return result
    }
    
    /**
     * Устанавливает провожающий свет на 30 секунд
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun setWelcomeLight30s(): Boolean {
        return setWelcomeLightValue(IVehicle.HOME_SAFE_LIGHT_VALUE_30S)
    }
    
    /**
     * Устанавливает провожающий свет на 60 секунд
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun setWelcomeLight60s(): Boolean {
        return setWelcomeLightValue(IVehicle.HOME_SAFE_LIGHT_VALUE_60S)
    }
    
    /**
     * Устанавливает провожающий свет на 90 секунд
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun setWelcomeLight90s(): Boolean {
        return setWelcomeLightValue(IVehicle.HOME_SAFE_LIGHT_VALUE_90S)
    }
    
    /**
     * Выключает провожающий свет
     * @return true если команда выполнена успешно, false в случае ошибки
     */
    fun setWelcomeLightOff(): Boolean {
        return setWelcomeLightValue(IVehicle.HOME_SAFE_LIGHT_VALUE_OFF)
    }
    
    /**
     * Получает все доступные значения провожающего света
     * @return массив доступных значений
     */
    fun getAvailableValues(): Array<Int> {
        return WELCOME_LIGHT_VALUES.copyOf()
    }
    
    /**
     * Получает описание значения провожающего света
     * @param value значение из IVehicle
     * @return описание значения или "Unknown"
     */
    fun getValueDescription(value: Int): String {
        return VALUE_DESCRIPTIONS[value] ?: "Unknown"
    }
}
