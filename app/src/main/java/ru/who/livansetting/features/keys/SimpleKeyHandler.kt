package ru.who.livansetting.features.keys

import android.content.Context
import android.util.Log
import com.ecarx.xui.adaptapi.input.KeyCode
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Упрощенный обработчик клавиш
 */
class SimpleKeyHandler(
    private val context: Context,
    private val keyActionExecutor: KeyActionExecutor = KeyActionExecutor(context)
) {
    
    companion object {
        private const val TAG = "SimpleKeyHandler"
        private const val LONG_PRESS_DURATION = 1000L
        private const val DEBOUNCE_DURATION = 50L
    }
    
    private val keyPressTimes = mutableMapOf<Int, Long>()
    private val longPressFutures = mutableMapOf<Int, ScheduledFuture<*>>()
    private val processedLongPresses = mutableSetOf<Int>()
    private val lastProcessedTime = mutableMapOf<Int, Long>()
    
    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor { r ->
        Thread(r, "SimpleKeyHandler").apply { isDaemon = true }
    }
    
    fun handleKeyEvent(keyCode: Int, action: Int) {
        when (action) {
            1 -> handleKeyPressed(keyCode)
            0 -> handleKeyReleased(keyCode)
        }
    }
    
    private fun handleKeyPressed(keyCode: Int) {
        val currentTime = System.currentTimeMillis()
        val lastTime = lastProcessedTime[keyCode] ?: 0
        if (currentTime - lastTime < DEBOUNCE_DURATION) return
        
        longPressFutures[keyCode]?.cancel(false)
        processedLongPresses.remove(keyCode)
        keyPressTimes[keyCode] = currentTime
        lastProcessedTime[keyCode] = currentTime
        
        if (isShortPressOnlyKey(keyCode)) return
        
        longPressFutures[keyCode] = executor.schedule({
            if (keyPressTimes[keyCode] != null) {
                processedLongPresses.add(keyCode)
                keyActionExecutor.handleKeyPressWithRemapping(keyCode, true)
            }
        }, LONG_PRESS_DURATION, TimeUnit.MILLISECONDS)
    }
    
    private fun handleKeyReleased(keyCode: Int) {
        val pressTime = keyPressTimes[keyCode] ?: return
        val duration = System.currentTimeMillis() - pressTime
        longPressFutures[keyCode]?.cancel(false)
        
        if (isShortPressOnlyKey(keyCode)) {
            keyActionExecutor.handleKeyPressWithRemapping(keyCode, false)
            if (isVolumeKey(keyCode)) keyActionExecutor.handleVolumeUpRelease() // Simplification for release
        } else {
            if (duration < LONG_PRESS_DURATION) {
                keyActionExecutor.handleKeyPressWithRemapping(keyCode, false)
            } else if (!processedLongPresses.contains(keyCode)) {
                keyActionExecutor.handleKeyPressWithRemapping(keyCode, true)
            }
            if (isVolumeKey(keyCode)) {
                if (keyCode == KeyCode.KEYCODE_R_VOLUME_UP || keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_UP)
                    keyActionExecutor.handleVolumeUpRelease()
                else
                    keyActionExecutor.handleVolumeDownRelease()
            }
        }
        
        keyPressTimes.remove(keyCode)
        longPressFutures.remove(keyCode)
        processedLongPresses.remove(keyCode)
    }
    
    private fun isVolumeKey(keyCode: Int) = keyCode in listOf(
        KeyCode.KEYCODE_R_VOLUME_UP, KeyCode.KEYCODE_R_VOLUME_DOWN,
        IICKeyCodes.KEY_CODE_IIC_VOLUME_UP, IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN
    )
    
    private fun isShortPressOnlyKey(keyCode: Int) = keyCode in listOf(
        IICKeyCodes.KEY_CODE_IIC_VOLUME_UP, IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN,
        IICKeyCodes.KEY_CODE_IIC_MUTE, IICKeyCodes.KEY_CODE_IIC_POWER
    )
    
    fun shutdown() {
        executor.shutdownNow()
    }
}
