package ru.who.livansetting.services

import android.content.Context
import android.util.Log
import com.ecarx.xui.adaptapi.car.impl.CarImpl
import com.ecarx.xui.adaptapi.car.base.ICarFunction
import com.ecarx.xui.adaptapi.car.sensor.ISensor
import com.ecarx.xui.adaptapi.binder.IConnectable
import com.ecarx.xui.adaptapi.FunctionStatus
import ru.who.livansetting.utils.SeatHeatingManager

/**
 * HAL класс для создания экземпляров CarImpl
 */
object HAL {
    private const val TAG = "HAL"
    
    /**
     * Создает экземпляр CarImpl через рефлексию
     * @param context контекст приложения
     * @return экземпляр CarImpl или null в случае ошибки
     */
    fun createCar(context: Context): CarImpl? {
        return try {
            // Создание объекта CarImpl через рефлексию
            val carImplClass = CarImpl::class.java
            val constructor = carImplClass.getConstructor(Context::class.java)
            constructor.newInstance(context) as CarImpl
        } catch (e: Exception) {
            Log.e(TAG, "Error creating CarImpl", e)
            null
        }
    }
}

/**
 * Основной сервис для работы с автомобильной системой
 */
class CarService(private val context: Context) {
    companion object {
        private const val TAG = "CarService"
        private const val DRL_FUNCTION_ID = 537135104
    }
    
    var carFunction: ICarFunction? = null
        private set
    var car: CarImpl? = null
        private set
    var sensor: ISensor? = null
        private set
    
    private var seatHeatingManager: SeatHeatingManager? = null
    
    private var isConnected = false
    private var connectWatcher: IConnectable.IConnectWatcher? = null
    
    /**
     * Создает объект Car
     * @return true если создание успешно, false в случае ошибки
     */
    fun createCar(): Boolean {
        if (car != null) {
            Log.w(TAG, "Car already created")
            return true
        }
        
        car = HAL.createCar(context)
        if (car == null) {
            Log.e(TAG, "Failed to create Car object")
            return false
        }
        
        Log.d(TAG, "Car object created successfully")
        return true
    }
    
    /**
     * Подключается к автомобильному интерфейсу
     * @return true если подключение инициировано, false в случае ошибки
     */
    fun connectToCarInterface(): Boolean {
        if (isConnected) {
            Log.w(TAG, "Car already connected")
            return true
        }
        
        if (car == null) {
            Log.e(TAG, "Car object not created")
            return false
        }
        
        Log.d(TAG, "Connecting to car...")
        
        // Создаем наблюдатель подключения
        connectWatcher = object : IConnectable.IConnectWatcher {
            override fun onConnected() {
                Log.d(TAG, "Car connected successfully")
                isConnected = true
                
                // Получение ICarFunction и ISensor после подключения
                carFunction = car?.getICarFunction()
                sensor = car?.getSensorManager()
                
                if (carFunction != null) {
                    Log.d(TAG, "ICarFunction obtained successfully")
                    // Инициализируем функции автомобиля
                    initializeCarFunctions()
                } else {
                    Log.e(TAG, "Failed to get ICarFunction")
                }
                
                if (sensor != null) {
                    Log.d(TAG, "ISensor obtained successfully")
                } else {
                    Log.e(TAG, "Failed to get ISensor")
                }
            }
            
            override fun onDisConnected() {
                Log.d(TAG, "Car disconnected")
                isConnected = false
                carFunction = null
                sensor = null
            }
        }
        
        // Регистрируем наблюдателя подключения
        car?.registerConnectWatcher(connectWatcher)
        
        // Инициируем подключение
        car?.connect()
        
        return true
    }
    
    /**
     * Инициализация функций автомобиля
     */
    private fun initializeCarFunctions() {
        if (carFunction == null) {
            Log.e(TAG, "ICarFunction is null")
            return
        }
        
        // Регистрация наблюдателя изменений функций
        carFunction?.registerFunctionValueWatcher(object : ICarFunction.IFunctionValueWatcher {
            override fun onFunctionValueChanged(functionId: Int, zone: Int, value: Int) {
                Log.d(TAG, "Function changed: ID=$functionId, Zone=$zone, Value=$value")
            }
            
            override fun onFunctionChanged(functionId: Int) {
                Log.d(TAG, "Function changed: ID=$functionId")
            }
            
            override fun onCustomizeFunctionValueChanged(functionId: Int, zone: Int, value: Float) {
                Log.d(TAG, "Customize function changed: ID=$functionId, Zone=$zone, Value=$value")
            }
            
            override fun onSupportedFunctionStatusChanged(functionId: Int, zone: Int, status: FunctionStatus) {
                Log.d(TAG, "Supported function status changed: ID=$functionId, Zone=$zone, Status=$status")
            }
            
            override fun onSupportedFunctionValueChanged(functionId: Int, values: IntArray) {
                Log.d(TAG, "Supported function values changed: ID=$functionId, Values=${values.contentToString()}")
            }
        })
        
        // Проверяем поддержку DRL функции
        val supportedStatus = carFunction?.isFunctionSupported(DRL_FUNCTION_ID)
        Log.d(TAG, "DRL function support status: $supportedStatus")
    }
    
    /**
     * Вызывается при успешном подключении к автомобилю
     */
    fun onConnected() {
        Log.d(TAG, "Car is connected")
        carFunction = car?.getICarFunction()
        sensor = car?.getSensorManager()
        isConnected = true
        
        // Инициализируем SeatHeatingManager
        seatHeatingManager = SeatHeatingManager(context)
        Log.d(TAG, "SeatHeatingManager initialized")
    }
    
    /**
     * Вызывается при отключении от автомобиля
     */
    fun onDisconnected() {
        Log.d(TAG, "Car is disconnected")
        carFunction = null
        sensor = null
        isConnected = false
    }
    
    /**
     * Проверяет, подключен ли сервис к автомобилю
     */
    fun isConnected(): Boolean {
        return isConnected && carFunction != null
    }
    
    /**
     * Получает экземпляр ICarFunction для прямого доступа
     * @return ICarFunction или null если не подключен
     */
    fun getICarFunction(): ICarFunction? {
        return carFunction
    }
    
    /**
     * Получает экземпляр ISensor для прямого доступа
     * @return ISensor или null если не подключен
     */
    fun getISensor(): ISensor? {
        return sensor
    }
    
    
    /**
     * Получает слушатель сенсоров для регистрации
     * @return ISensor.ISensorListener или null если не подключен
     */
    fun getSensorListener(): ISensor.ISensorListener? {
        // Возвращаем простой слушатель для демонстрации
        return object : ISensor.ISensorListener {
            override fun onSensorSupportChanged(sensorId: Int, status: FunctionStatus) {
                Log.d(TAG, "Sensor support changed: ID=$sensorId, Status=$status")
            }
            
            override fun onSensorEventChanged(sensorId: Int, event: Int) {
                Log.d(TAG, "Sensor event changed: ID=$sensorId, Event=$event")
            }
            
            override fun onSensorValueChanged(sensorId: Int, value: Float) {
                Log.d(TAG, "Sensor value changed: ID=$sensorId, Value=$value")
            }
        }
    }
    
    /**
     * Отключается от автомобиля
     */
    fun disconnect() {
        if (car != null && connectWatcher != null) {
            car?.unregisterConnectWatcher()
        }
        onDisconnected()
    }
    
    /**
     * Проверяет, поддерживается ли функция с указанным ID
     * @param functionId ID функции для проверки
     * @return true если функция поддерживается, false в противном случае
     */
    fun isFunctionSupported(functionId: Int): Boolean {
        if (!isConnected() || carFunction == null) {
            Log.w(TAG, "Car not connected or ICarFunction is null")
            return false
        }
        
        return try {
            val result = carFunction?.isFunctionSupported(functionId)
            val isSupported = when (result) {
                is Boolean -> result
                is FunctionStatus -> result == FunctionStatus.active
                else -> false
            }
            Log.d(TAG, "isFunctionSupported($functionId) = $isSupported")
            isSupported
        } catch (e: Exception) {
            Log.e(TAG, "Error checking function support for ID: $functionId", e)
            false
        }
    }
    
    /**
     * Получает значение функции с указанным ID
     * @param functionId ID функции
     * @return значение функции или null в случае ошибки
     */
    fun getFunctionValue(functionId: Int): Int? {
        if (!isConnected() || carFunction == null) {
            Log.w(TAG, "Car not connected or ICarFunction is null")
            return null
        }
        
        return try {
            val result = carFunction?.getFunctionValue(functionId)
            Log.d(TAG, "getFunctionValue($functionId) = $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error getting function value for ID: $functionId", e)
            null
        }
    }
    
    /**
     * Устанавливает значение функции с указанным ID
     * @param functionId ID функции
     * @param value новое значение
     * @return true если операция успешна, false в противном случае
     */
    fun setFunctionValue(functionId: Int, value: Int): Boolean {
        if (!isConnected() || carFunction == null) {
            Log.w(TAG, "Car not connected or ICarFunction is null")
            return false
        }
        
        return try {
            val result = carFunction?.setFunctionValue(functionId, value) ?: false
            Log.d(TAG, "setFunctionValue($functionId, $value) = $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error setting function value for ID: $functionId, value: $value", e)
            false
        }
    }
    
    /**
     * Устанавливает уровень подогрева сиденья водителя
     * @param level уровень подогрева (0 = выключено, 1 = уровень 1, 2 = уровень 2)
     * @return true если операция успешна, false в противном случае
     */
    fun setDriverSeatHeatingLevel(level: Int): Boolean {
        if (!isConnected() || seatHeatingManager == null) {
            Log.w(TAG, "Car not connected or SeatHeatingManager not initialized")
            return false
        }
        
        return try {
            val result = seatHeatingManager?.setDriverSeatHeat(level) ?: false
            Log.d(TAG, "setDriverSeatHeatingLevel($level) = $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error setting driver seat heating level: $level", e)
            false
        }
    }
    
    /**
     * Устанавливает уровень подогрева сиденья пассажира
     * @param level уровень подогрева (0 = выключено, 1 = уровень 1, 2 = уровень 2)
     * @return true если операция успешна, false в противном случае
     */
    fun setPassengerSeatHeatingLevel(level: Int): Boolean {
        if (!isConnected() || seatHeatingManager == null) {
            Log.w(TAG, "Car not connected or SeatHeatingManager not initialized")
            return false
        }
        
        return try {
            val result = seatHeatingManager?.setPassengerSeatHeat(level) ?: false
            Log.d(TAG, "setPassengerSeatHeatingLevel($level) = $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error setting passenger seat heating level: $level", e)
            false
        }
    }
    
    /**
     * Получает текущий уровень подогрева сиденья водителя
     * @return уровень подогрева или null если недоступно
     */
    fun getDriverSeatHeatingLevel(): Int? {
        if (!isConnected() || seatHeatingManager == null) {
            Log.w(TAG, "Car not connected or SeatHeatingManager not initialized")
            return null
        }
        
        return try {
            val level = seatHeatingManager?.getDriverSeatHeatLevel()
            Log.d(TAG, "getDriverSeatHeatingLevel() = $level")
            level
        } catch (e: Exception) {
            Log.e(TAG, "Error getting driver seat heating level", e)
            null
        }
    }
    
    /**
     * Получает текущий уровень подогрева сиденья пассажира
     * @return уровень подогрева или null если недоступно
     */
    fun getPassengerSeatHeatingLevel(): Int? {
        if (!isConnected() || seatHeatingManager == null) {
            Log.w(TAG, "Car not connected or SeatHeatingManager not initialized")
            return null
        }
        
        return try {
            val level = seatHeatingManager?.getPassengerSeatHeatLevel()
            Log.d(TAG, "getPassengerSeatHeatingLevel() = $level")
            level
        } catch (e: Exception) {
            Log.e(TAG, "Error getting passenger seat heating level", e)
            null
        }
    }
    
    /**
     * Очищает ресурсы сервиса
     */
    fun cleanup() {
        disconnect()
        carFunction = null
        sensor = null
        car = null
        seatHeatingManager = null
        isConnected = false
        connectWatcher = null
        Log.d(TAG, "CarService cleaned up")
    }
}
