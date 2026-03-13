package ru.who.livansetting.services

import android.content.Context
import android.util.Log
import com.ecarx.xui.adaptapi.policy.II2cCommunication
import com.ecarx.xui.adaptapi.policy.I2cCallback
import com.ecarx.xui.adaptapi.policy.I2cReadyCallback
import com.ecarx.xui.adaptapi.policy.Policy
import ru.who.livansetting.constants.IICKeyCodes
import ru.who.livansetting.utils.BuildPropUtils

/**
 * Сервис для работы с I2C интерфейсом
 * Обрабатывает данные от I2C и конвертирует их в события клавиш
 */
class I2CService(private val context: Context) {
    
    companion object {
        private const val TAG = "I2CService"
    }
    
    private var policy: Policy? = null
    private var i2cCommunication: II2cCommunication? = null
    private var i2cCallback: I2CCallback? = null
    private var i2cReadyCallback: I2CReadyCallback? = null
    private var keyEventHandler: ((Int, Boolean) -> Unit)? = null
    private var isInitialized = false
    
    // Для детекции длительного нажатия I2C клавиш
    private val keyPressTimes = mutableMapOf<Int, Long>()
    private val longPressThreshold = 500L // 500ms для длительного нажатия
    private val keyPressHandlers = mutableMapOf<Int, android.os.Handler>()
    
    /**
     * Инициализирует I2C сервис
     */
    fun initialize() {
        try {
            Log.d(TAG, "Initializing I2C service...")
            
            // Создаем Policy экземпляр
            policy = Policy.create(context)
            if (policy == null) {
                Log.w(TAG, "Failed to create Policy instance - Policy API not available. " +
                        "This is expected if the ECARX Policy API implementation is not present on this device.")
                return
            }
            Log.d(TAG, "Policy instance created successfully")
            
            // Получаем I2C интерфейс через Policy
            i2cCommunication = policy?.i2cCommunication
            if (i2cCommunication == null) {
                Log.w(TAG, "I2C communication interface not available")
                return
            }
            Log.d(TAG, "I2C communication interface obtained successfully")
            
            // Создаем callback для готовности I2C
            i2cReadyCallback = I2CReadyCallback()
            
            // Устанавливаем callback для уведомления о готовности
            i2cCommunication!!.setOnReadyCallback(i2cReadyCallback)
            
            // Проверяем текущее состояние готовности
            if (i2cCommunication!!.isReady()) {
                Log.d(TAG, "I2C communication interface is already ready")
                onI2CReady()
            } else {
                Log.d(TAG, "I2C communication interface is not ready, waiting for ready callback...")
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing I2C service", e)
        }
    }
    
    /**
     * Вызывается когда I2C интерфейс готов к работе
     */
    private fun onI2CReady() {
        try {
            Log.d(TAG, "I2C communication interface is ready, registering listener...")
            
            // Создаем callback для I2C
            i2cCallback = I2CCallback()
            
            // Регистрируем слушатель I2C
            val success = i2cCommunication!!.registerI2cListener(i2cCallback!!)
            if (success) {
                Log.d(TAG, "I2C listener registered successfully")
                isInitialized = true
            } else {
                Log.w(TAG, "Failed to register I2C listener")
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error registering I2C listener", e)
        }
    }
    
    /**
     * Освобождает ресурсы I2C сервиса
     */
    fun release() {
        try {
            if (i2cCommunication != null && i2cCallback != null) {
                // Отменяем регистрацию слушателя I2C
                // Примечание: В интерфейсе II2cCommunication нет метода unregisterI2cListener
                // Возможно, это делается автоматически при уничтожении объекта
                Log.d(TAG, "I2C listener unregistered")
            }
            
            // Очищаем ресурсы детекции длительного нажатия
            keyPressHandlers.values.forEach { handler ->
                handler.removeCallbacksAndMessages(null)
            }
            keyPressHandlers.clear()
            keyPressTimes.clear()
            
            i2cCommunication = null
            i2cCallback = null
            i2cReadyCallback = null
            policy = null
            keyEventHandler = null
            isInitialized = false
        } catch (e: Exception) {
            Log.e(TAG, "Error releasing I2C service", e)
        }
    }
    
    /**
     * Устанавливает обработчик событий клавиш
     */
    fun setKeyEventHandler(handler: (Int, Boolean) -> Unit) {
        keyEventHandler = handler
    }
    
    /**
     * Проверяет, инициализирован ли I2C сервис
     */
    fun isInitialized(): Boolean {
        return isInitialized
    }
    
    /**
     * Callback для обработки I2C данных
     * Основан на коде из InputService
     */
    private inner class I2CCallback : I2cCallback {
        override fun onReceiveI2cData(data: IntArray) {
            try {
                Log.d(TAG, "onReceiveI2cData data[0] = ${data[0]}")
                
                // Проверяем, что это данные от клавиш (data[0] == 16)
                if (data[0] == 16) {
                    processKeyData(data)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing I2C data", e)
            }
        }
        
        private fun processKeyData(data: IntArray) {
            // Обработка IIC_VOLUME_UP (игнорируем, если flavor содержит "602")
            if (!BuildPropUtils.isFlavorContains("601")) {
                if (data.size > IICKeyCodes.IIC_DATA_INDEX_VOLUME_UP) {
                    if ((data[IICKeyCodes.IIC_DATA_INDEX_VOLUME_UP] and IICKeyCodes.IIC_PRESS_MASK) != 0) {
                        Log.d(TAG, "IIC_VOLUME_UP Press")
                        handleKeyPress(IICKeyCodes.KEY_CODE_IIC_VOLUME_UP)
                    }
                    if ((data[IICKeyCodes.IIC_DATA_INDEX_VOLUME_UP] and IICKeyCodes.IIC_RELEASE_MASK) != 0) {
                        Log.d(TAG, "IIC_VOLUME_UP Release")
                        handleKeyRelease(IICKeyCodes.KEY_CODE_IIC_VOLUME_UP)
                    }
                }
            }
            
            // Обработка IIC_MUTE
            if (data.size > IICKeyCodes.IIC_DATA_INDEX_MUTE) {
                if ((data[IICKeyCodes.IIC_DATA_INDEX_MUTE] and IICKeyCodes.IIC_PRESS_MASK) != 0) {
                    Log.d(TAG, "IIC_MUTE Press")
                    handleKeyPress(IICKeyCodes.KEY_CODE_IIC_MUTE)
                }
                if ((data[IICKeyCodes.IIC_DATA_INDEX_MUTE] and IICKeyCodes.IIC_RELEASE_MASK) != 0) {
                    Log.d(TAG, "IIC_MUTE Release")
                    handleKeyRelease(IICKeyCodes.KEY_CODE_IIC_MUTE)
                }
            }
            
            // Обработка IIC_VOLUME_DOWN (игнорируем, если flavor содержит "602")
            if (!BuildPropUtils.isFlavorContains("601")) {
                if (data.size > IICKeyCodes.IIC_DATA_INDEX_VOLUME_DOWN) {
                    if ((data[IICKeyCodes.IIC_DATA_INDEX_VOLUME_DOWN] and IICKeyCodes.IIC_PRESS_MASK) != 0) {
                        Log.d(TAG, "IIC_VOLUME_DOWN Press")
                        handleKeyPress(IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN)
                    }
                    if ((data[IICKeyCodes.IIC_DATA_INDEX_VOLUME_DOWN] and IICKeyCodes.IIC_RELEASE_MASK) != 0) {
                        Log.d(TAG, "IIC_VOLUME_DOWN Release")
                        handleKeyRelease(IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN)
                    }
                }
            }
            
            // Обработка IIC_POWER
            if (data.size > IICKeyCodes.IIC_DATA_INDEX_POWER) {
                if ((data[IICKeyCodes.IIC_DATA_INDEX_POWER] and IICKeyCodes.IIC_PRESS_MASK) != 0) {
                    Log.d(TAG, "IIC_POWER Press")
                    handleKeyPress(IICKeyCodes.KEY_CODE_IIC_POWER)
                }
                if ((data[IICKeyCodes.IIC_DATA_INDEX_POWER] and IICKeyCodes.IIC_RELEASE_MASK) != 0) {
                    Log.d(TAG, "IIC_POWER Release")
                    handleKeyRelease(IICKeyCodes.KEY_CODE_IIC_POWER)
                }
            }
        }
        
        /**
         * Обрабатывает нажатие клавиши
         * Для VOLUME_UP, VOLUME_DOWN, MUTE и POWER обрабатываются только короткие нажатия
         */
        private fun handleKeyPress(keyCode: Int) {
            val currentTime = System.currentTimeMillis()
            keyPressTimes[keyCode] = currentTime
            
            // Отправляем событие нажатия (false = нажатие, true = отпускание)
            keyEventHandler?.invoke(keyCode, false)
            
            // Для указанных клавиш не обрабатываем длительные нажатия
            // VOLUME_UP, VOLUME_DOWN, MUTE и POWER обрабатываются только как короткие нажатия
        }
        
        /**
         * Обрабатывает отпускание клавиши
         */
        private fun handleKeyRelease(keyCode: Int) {
            keyPressTimes.remove(keyCode)
            
            // Отменяем таймер длительного нажатия (если был)
            keyPressHandlers[keyCode]?.removeCallbacksAndMessages(null)
            keyPressHandlers.remove(keyCode)
            
            // Для всех указанных клавиш отправляем событие отпускания
            if (keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_UP || 
                keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN ||
                keyCode == IICKeyCodes.KEY_CODE_IIC_MUTE ||
                keyCode == IICKeyCodes.KEY_CODE_IIC_POWER) {
                // Отправляем событие отпускания
                keyEventHandler?.invoke(keyCode, true) // true означает отпускание
            }
        }
    }
    
    /**
     * Callback для уведомления о готовности I2C интерфейса
     */
    private inner class I2CReadyCallback : I2cReadyCallback {
        override fun onReady() {
            Log.d(TAG, "I2C ready callback received")
            onI2CReady()
        }
    }
}
