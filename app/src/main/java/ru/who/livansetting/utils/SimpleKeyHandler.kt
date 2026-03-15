package ru.who.livansetting.utils

import android.content.Context
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
class SimpleKeyHandler(
    private val context: Context,
    private val keyActionExecutor: KeyActionExecutor = KeyActionExecutor(context)
) {
    
    companion object {
        private const val TAG = "SimpleKeyHandler"
        private const val LONG_PRESS_DURATION = 1000L // 1 секунда
        private const val DEBOUNCE_DURATION = 50L // Защита от дребезга
    }
    
    private val keyPressTimes = mutableMapOf<Int, Long>()
    private val longPressFutures = mutableMapOf<Int, ScheduledFuture<*>>()
    private val processedLongPresses = mutableSetOf<Int>()
    private val lastProcessedTime = mutableMapOf<Int, Long>()
    
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
    
    private fun handleKeyPressed(keyCode: Int) {
        Log.d(TAG, "Key pressed: $keyCode")
        val currentTime = System.currentTimeMillis()
        
        val lastTime = lastProcessedTime[keyCode] ?: 0
        if (currentTime - lastTime < DEBOUNCE_DURATION) return
        
        longPressFutures[keyCode]?.cancel(false)
        processedLongPresses.remove(keyCode)
        keyPressTimes[keyCode] = currentTime
        lastProcessedTime[keyCode] = currentTime
        
        if (isShortPressOnlyKey(keyCode)) return
        
        val longPressFuture = executor.schedule({
            val pressTime = keyPressTimes[keyCode]
            if (pressTime != null) {
                processedLongPresses.add(keyCode)
                processKeyAction(keyCode, true)
            }
        }, LONG_PRESS_DURATION, TimeUnit.MILLISECONDS)
        
        longPressFutures[keyCode] = longPressFuture
    }
    
    private fun handleKeyReleased(keyCode: Int) {
        Log.d(TAG, "Key released: $keyCode")
        
        val pressTime = keyPressTimes[keyCode]
        if (pressTime != null) {
            val duration = System.currentTimeMillis() - pressTime
            val isLongPress = duration >= LONG_PRESS_DURATION
            
            longPressFutures[keyCode]?.cancel(false)
            
            if (isShortPressOnlyKey(keyCode)) {
                processKeyAction(keyCode, false)
                if (isVolumeKey(keyCode)) processKeyRelease(keyCode)
            } else {
                if (!isLongPress) {
                    processKeyAction(keyCode, false)
                } else if (!processedLongPresses.contains(keyCode)) {
                    processKeyAction(keyCode, true)
                }
                
                if (isVolumeKey(keyCode)) processKeyRelease(keyCode)
            }
            
            cleanupKeyData(keyCode)
        }
    }
    
    private fun processKeyAction(keyCode: Int, isLongPress: Boolean) {
        keyActionExecutor.handleKeyPressWithRemapping(keyCode, isLongPress)
    }
    
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
    
    private fun isVolumeKey(keyCode: Int): Boolean {
        return keyCode == KeyCode.KEYCODE_R_VOLUME_UP || 
               keyCode == KeyCode.KEYCODE_R_VOLUME_DOWN ||
               keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_UP ||
               keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN
    }
    
    private fun isShortPressOnlyKey(keyCode: Int): Boolean {
        return keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_UP ||
               keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN ||
               keyCode == IICKeyCodes.KEY_CODE_IIC_MUTE ||
               keyCode == IICKeyCodes.KEY_CODE_IIC_POWER
    }
    
    private fun cleanupKeyData(keyCode: Int) {
        keyPressTimes.remove(keyCode)
        longPressFutures.remove(keyCode)
        processedLongPresses.remove(keyCode)
    }
    
    fun clear() {
        longPressFutures.values.forEach { it.cancel(false) }
        longPressFutures.clear()
        keyPressTimes.clear()
        processedLongPresses.clear()
        lastProcessedTime.clear()
    }
    
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
