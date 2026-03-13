package ru.who.livansetting.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.ecarx.xui.adaptapi.input.KeyCode
import ru.who.livansetting.constants.IICKeyCodes
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Упрощенный обработчик клавиш
 * Объединяет функциональность HardwareKeyHandler, KeyDetector и KeyBase
 */
class SimpleKeyHandler(private val context: Context) {
    
    companion object {
        private const val TAG = "SimpleKeyHandler"
        private const val LONG_PRESS_DURATION = 1000L // 1 секунда
        private const val DEBOUNCE_DURATION = 50L // Защита от дребезга
    }
    
    private val keyActionExecutor = KeyActionExecutor(context)
    private val keyPressTimes = mutableMapOf<Int, Long>()
    private val longPressFutures = mutableMapOf<Int, ScheduledFuture<*>>()
    private val processedLongPresses = mutableSetOf<Int>()
    private val lastProcessedTime = mutableMapOf<Int, Long>()
    
    // Используем ExecutorService для лучшей производительности
    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor { r ->
        Thread(r, "SimpleKeyHandler").apply { isDaemon = true }
    }
    
    /**
     * Обрабатывает событие клавиши
     * @param keyCode код клавиши
     * @param action действие (1 - нажатие, 0 - отпускание)
     */
    fun handleKeyEvent(keyCode: Int, action: Int) {
        when (action) {
            1 -> handleKeyPressed(keyCode)
            0 -> handleKeyReleased(keyCode)
        }
    }
    
    /**
     * Обрабатывает нажатие клавиши
     */
    private fun handleKeyPressed(keyCode: Int) {
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
        
        // Для клавиш, которые должны обрабатывать только короткие нажатия, не запускаем таймер
        if (isShortPressOnlyKey(keyCode)) {
            Log.d(TAG, "Key $keyCode is short press only, skipping long press timer")
            return
        }
        
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
                    processKeyAction(keyCode, true)
                }
            }
        }, LONG_PRESS_DURATION, TimeUnit.MILLISECONDS)
        
        longPressFutures[keyCode] = longPressFuture
    }
    
    /**
     * Обрабатывает отпускание клавиши
     */
    private fun handleKeyReleased(keyCode: Int) {
        Log.d(TAG, "Key released: $keyCode")
        
        val pressTime = keyPressTimes[keyCode]
        if (pressTime != null) {
            val duration = System.currentTimeMillis() - pressTime
            val isLongPress = duration >= LONG_PRESS_DURATION
            
            Log.d(TAG, "Key released check for key $keyCode: duration=$duration, isLongPress=$isLongPress")
            
            // Отменяем задачу длинного нажатия, если она еще не выполнилась
            longPressFutures[keyCode]?.cancel(false)
            
            // Специальная обработка для клавиш, которые должны обрабатывать только короткие нажатия
            if (isShortPressOnlyKey(keyCode)) {
                // Для этих клавиш обрабатываем только короткие нажатия при отпускании
                if (!isLongPress) {
                    Log.d(TAG, "Processing short press for short-press-only key $keyCode on release")
                    processKeyAction(keyCode, false)
                } else {
                    Log.d(TAG, "Long press detected for short-press-only key $keyCode, treating as short press")
                    processKeyAction(keyCode, false)
                }
                
                // Вызываем обработку отпускания для клавиш громкости
                if (isVolumeKey(keyCode)) {
                    processKeyRelease(keyCode)
                    Log.d(TAG, "Volume key released: $keyCode")
                }
            } else if (isVolumeKey(keyCode)) {
                // Для остальных клавиш громкости обрабатываем короткие и длинные нажатия
                if (!isLongPress) {
                    Log.d(TAG, "Processing short press for volume key $keyCode on release")
                    processKeyAction(keyCode, false)
                } else if (isLongPress && !processedLongPresses.contains(keyCode)) {
                    // Это длинное нажатие, которое не было обработано в onKeyPressed
                    Log.d(TAG, "Processing long press for volume key $keyCode on release")
                    processKeyAction(keyCode, true)
                } else {
                    Log.d(TAG, "Long press for volume key $keyCode already processed, skipping")
                }
                
                // Всегда вызываем обработку отпускания для остановки непрерывной регулировки
                processKeyRelease(keyCode)
                Log.d(TAG, "Volume key released: $keyCode")
            } else {
                // Для остальных клавиш обрабатываем короткие и длинные нажатия
                if (!isLongPress) {
                    Log.d(TAG, "Processing short press for key $keyCode on release")
                    processKeyAction(keyCode, false)
                } else if (isLongPress && !processedLongPresses.contains(keyCode)) {
                    // Это длинное нажатие, которое не было обработано в onKeyPressed
                    Log.d(TAG, "Processing long press for key $keyCode on release")
                    processKeyAction(keyCode, true)
                } else {
                    Log.d(TAG, "Long press for key $keyCode already processed, skipping")
                }
            }
            
            // Очищаем данные для этой клавиши
            cleanupKeyData(keyCode)
        } else {
            Log.w(TAG, "Key released without press time recorded: $keyCode")
        }
    }
    
    /**
     * Обрабатывает действие клавиши
     */
    private fun processKeyAction(keyCode: Int, isLongPress: Boolean) {
        Log.d(TAG, "Processing key action: $keyCode, isLongPress: $isLongPress")
        keyActionExecutor.handleKeyPressWithRemapping(keyCode, isLongPress)
    }
    
    /**
     * Обрабатывает отпускание клавиши (для клавиш громкости)
     */
    private fun processKeyRelease(keyCode: Int) {
        when (keyCode) {
            KeyCode.KEYCODE_R_VOLUME_UP,
            IICKeyCodes.KEY_CODE_IIC_VOLUME_UP -> {
                keyActionExecutor.handleVolumeUpRelease()
            }
            KeyCode.KEYCODE_R_VOLUME_DOWN,
            IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN -> {
                keyActionExecutor.handleVolumeDownRelease()
            }
        }
    }
    
    /**
     * Проверяет, является ли клавиша клавишей громкости
     */
    private fun isVolumeKey(keyCode: Int): Boolean {
        return keyCode == KeyCode.KEYCODE_R_VOLUME_UP || 
               keyCode == KeyCode.KEYCODE_R_VOLUME_DOWN ||
               keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_UP ||
               keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN
    }
    
    /**
     * Проверяет, должна ли клавиша обрабатывать только короткие нажатия
     */
    private fun isShortPressOnlyKey(keyCode: Int): Boolean {
        return keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_UP ||
               keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN ||
               keyCode == IICKeyCodes.KEY_CODE_IIC_MUTE ||
               keyCode == IICKeyCodes.KEY_CODE_IIC_POWER
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
     * Очищает все данные
     */
    fun clear() {
        // Отменяем все активные задачи
        longPressFutures.values.forEach { it.cancel(false) }
        longPressFutures.clear()
        
        // Очищаем все данные
        keyPressTimes.clear()
        processedLongPresses.clear()
        lastProcessedTime.clear()
    }
    
    /**
     * Получает KeyActionExecutor для внешнего использования
     */
    fun getKeyActionExecutor(): KeyActionExecutor {
        return keyActionExecutor
    }
    
    /**
     * Освобождает ресурсы
     */
    fun shutdown() {
        clear()
        executor.shutdown()
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow()
            }
        } catch (e: InterruptedException) {
            executor.shutdownNow()
            Thread.currentThread().interrupt()
        }
    }
}
