package ru.who.livansetting.utils

import android.util.Log
import com.ecarx.xui.adaptapi.input.KeyCode
import ru.who.livansetting.constants.IICKeyCodes
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * Детектор длительности нажатия клавиш
 * Улучшенная версия с использованием ExecutorService вместо Handler для лучшей производительности
 */
class KeyPressDetector {
    
    companion object {
        private const val TAG = "KeyPressDetector"
        private const val LONG_PRESS_DURATION = 1000L // 1 секунда
        private const val DEBOUNCE_DURATION = 50L // Защита от дребезга
    }
    
    private val keyPressTimes = mutableMapOf<Int, Long>()
    private val longPressFutures = mutableMapOf<Int, java.util.concurrent.ScheduledFuture<*>>()
    private val processedLongPresses = mutableSetOf<Int>() // Отслеживаем уже обработанные длинные нажатия
    
    // Используем ExecutorService вместо Handler для лучшей производительности
    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor { r ->
        Thread(r, "KeyPressDetector").apply { isDaemon = true }
    }
    
    // Добавляем debounce для предотвращения множественных срабатываний
    private val lastProcessedTime = mutableMapOf<Int, Long>()
    
    // Колбэки для обработки отпускания клавиш громкости
    private var volumeKeyReleaseCallback: ((keyCode: Int) -> Unit)? = null
    
    /**
     * Обрабатывает нажатие клавиши
     * @param keyCode код клавиши
     * @param onKeyProcessed колбэк для обработки результата
     */
    fun onKeyPressed(keyCode: Int, onKeyProcessed: (keyCode: Int, isLongPress: Boolean) -> Unit) {
        Log.d(TAG, "Key pressed: $keyCode")
        val currentTime = System.currentTimeMillis()
        
        // Debounce проверка
        val lastTime = lastProcessedTime[keyCode] ?: 0
        if (currentTime - lastTime < DEBOUNCE_DURATION) {
            Log.d(TAG, "Debounce: ignoring key $keyCode (too soon after last press)")
            return
        }
        
        // Отменяем предыдущую задачу для этой клавиши, если она есть
        longPressFutures[keyCode]?.cancel(false)
        
        // Очищаем флаг обработанного длинного нажатия для новой клавиши
        processedLongPresses.remove(keyCode)
        keyPressTimes[keyCode] = currentTime
        lastProcessedTime[keyCode] = currentTime
        
        // Создаем задачу для проверки длинного нажатия
        val longPressFuture = executor.schedule({
            val pressTime = keyPressTimes[keyCode]
            if (pressTime != null) {
                val duration = System.currentTimeMillis() - pressTime
                val isLongPress = duration >= LONG_PRESS_DURATION
                
                Log.d(TAG, "Long press check for key $keyCode: duration=$duration, isLongPress=$isLongPress")
                
                if (isLongPress) {
                    // Помечаем длинное нажатие как обработанное
                    processedLongPresses.add(keyCode)
                }
                
                onKeyProcessed(keyCode, isLongPress)
            }
        }, LONG_PRESS_DURATION, TimeUnit.MILLISECONDS)
        
        longPressFutures[keyCode] = longPressFuture
    }
    
    /**
     * Обрабатывает отпускание клавиши
     * @param keyCode код клавиши
     * @param onKeyProcessed колбэк для обработки результата
     */
    fun onKeyReleased(keyCode: Int, onKeyProcessed: (keyCode: Int, isLongPress: Boolean) -> Unit) {
        Log.d(TAG, "Key released: $keyCode")
        
        val pressTime = keyPressTimes[keyCode]
        if (pressTime != null) {
            val duration = System.currentTimeMillis() - pressTime
            val isLongPress = duration >= LONG_PRESS_DURATION
            
            Log.d(TAG, "Key released check for key $keyCode: duration=$duration, isLongPress=$isLongPress")
            
            // Специальная обработка для клавиш громкости
            if (isVolumeKey(keyCode)) {
                // Для клавиш громкости при отпускании всегда вызываем колбэк отпускания
                volumeKeyReleaseCallback?.invoke(keyCode)
                Log.d(TAG, "Volume key released: $keyCode")
            }
            
            // Если это длинное нажатие, но оно уже было обработано в onKeyPressed, не обрабатываем повторно
            if (isLongPress && processedLongPresses.contains(keyCode)) {
                Log.d(TAG, "Long press for key $keyCode already processed, skipping")
            } else {
                onKeyProcessed(keyCode, isLongPress)
            }
            
            // Отменяем задачу длинного нажатия, если она еще не выполнилась
            longPressFutures[keyCode]?.cancel(false)
            
            // Очищаем данные для этой клавиши
            cleanupKeyData(keyCode)
        }
    }
    
    /**
     * Очищает данные для конкретной клавиши
     */
    private fun cleanupKeyData(keyCode: Int) {
        keyPressTimes.remove(keyCode)
        longPressFutures.remove(keyCode)
        processedLongPresses.remove(keyCode)
        lastProcessedTime.remove(keyCode)
    }
    
    /**
     * Очищает все отслеживаемые нажатия
     */
    fun clear() {
        // Отменяем все отложенные задачи
        longPressFutures.values.forEach { future ->
            future.cancel(false)
        }
        
        keyPressTimes.clear()
        longPressFutures.clear()
        processedLongPresses.clear()
        lastProcessedTime.clear()
    }
    
    /**
     * Устанавливает колбэк для обработки отпускания клавиш громкости
     * @param callback колбэк для обработки отпускания клавиш громкости
     */
    fun setVolumeKeyReleaseCallback(callback: (keyCode: Int) -> Unit) {
        volumeKeyReleaseCallback = callback
    }
    
    /**
     * Проверяет, является ли клавиша клавишей громкости
     * @param keyCode код клавиши
     * @return true если это клавиша громкости
     */
    private fun isVolumeKey(keyCode: Int): Boolean {
        return keyCode == KeyCode.KEYCODE_R_VOLUME_UP || 
               keyCode == KeyCode.KEYCODE_R_VOLUME_DOWN ||
               keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_UP ||
               keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN
    }
    
    /**
     * Освобождает ресурсы и завершает работу executor'а
     * Должен вызываться при уничтожении объекта для предотвращения утечек памяти
     */
    fun shutdown() {
        clear()
        volumeKeyReleaseCallback = null
        executor.shutdown()
        try {
            if (!executor.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow()
            }
        } catch (e: InterruptedException) {
            executor.shutdownNow()
            Thread.currentThread().interrupt()
        }
    }
}
