package ru.who.livansetting.features.keys

import android.content.Context
import android.util.Log
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

        // eCarX KeyCode constants inlined to avoid NoClassDefFoundError on emulator
        private const val KEYCODE_R_VOLUME_UP = 200024
        private const val KEYCODE_R_VOLUME_DOWN = 200025

        private val VOLUME_KEYS = setOf(
            KEYCODE_R_VOLUME_UP, KEYCODE_R_VOLUME_DOWN,
            IICKeyCodes.KEY_CODE_IIC_VOLUME_UP, IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN
        )

        private val SHORT_PRESS_ONLY_KEYS = setOf(
            IICKeyCodes.KEY_CODE_IIC_VOLUME_UP, IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN,
            IICKeyCodes.KEY_CODE_IIC_MUTE, IICKeyCodes.KEY_CODE_IIC_POWER
        )
    }

    private val lock = Any()
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
            else -> Log.w(TAG, "Unknown action $action for keyCode $keyCode")
        }
    }

    private fun handleKeyPressed(keyCode: Int) {
        Log.d(TAG, "Key pressed: keyCode=$keyCode")
        val currentTime = System.currentTimeMillis()

        synchronized(lock) {
            val lastTime = lastProcessedTime[keyCode] ?: 0
            if (currentTime - lastTime < DEBOUNCE_DURATION) {
                Log.d(TAG, "Debounce rejected keyCode=$keyCode")
                return
            }

            longPressFutures[keyCode]?.cancel(false)
            processedLongPresses.remove(keyCode)
            keyPressTimes[keyCode] = currentTime
            lastProcessedTime[keyCode] = currentTime

            if (!isShortPressOnlyKey(keyCode)) {
                Log.d(TAG, "Long-press scheduled for keyCode=$keyCode")
                longPressFutures[keyCode] = executor.schedule({
                    val shouldExecute = synchronized(lock) {
                        if (keyPressTimes[keyCode] != null) {
                            processedLongPresses.add(keyCode)
                            true
                        } else {
                            false
                        }
                    }
                    if (shouldExecute) {
                        keyActionExecutor.handleKeyPressWithRemapping(keyCode, true)
                    }
                }, LONG_PRESS_DURATION, TimeUnit.MILLISECONDS)
            }
        }
    }

    private fun handleKeyReleased(keyCode: Int) {
        val currentTime = System.currentTimeMillis()
        val pressTime: Long
        val isLongPress: Boolean

        synchronized(lock) {
            pressTime = keyPressTimes[keyCode] ?: return
            longPressFutures[keyCode]?.cancel(false)
            isLongPress = processedLongPresses.contains(keyCode)
        }

        val duration = currentTime - pressTime
        Log.d(TAG, "Key released: keyCode=$keyCode, duration=${duration}ms, isLongPress=$isLongPress")

        if (isShortPressOnlyKey(keyCode)) {
            keyActionExecutor.handleKeyPressWithRemapping(keyCode, false)
            if (isVolumeKey(keyCode)) {
                if (keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_UP) {
                    keyActionExecutor.handleVolumeUpRelease()
                } else if (keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN) {
                    keyActionExecutor.handleVolumeDownRelease()
                }
            }
        } else {
            if (duration < LONG_PRESS_DURATION) {
                keyActionExecutor.handleKeyPressWithRemapping(keyCode, false)
            } else if (!isLongPress) {
                keyActionExecutor.handleKeyPressWithRemapping(keyCode, true)
            }
            if (isVolumeKey(keyCode)) {
                if (keyCode == KEYCODE_R_VOLUME_UP || keyCode == IICKeyCodes.KEY_CODE_IIC_VOLUME_UP)
                    keyActionExecutor.handleVolumeUpRelease()
                else
                    keyActionExecutor.handleVolumeDownRelease()
            }
        }

        synchronized(lock) {
            keyPressTimes.remove(keyCode)
            longPressFutures.remove(keyCode)
            processedLongPresses.remove(keyCode)
        }
    }

    private fun isVolumeKey(keyCode: Int) = keyCode in VOLUME_KEYS

    private fun isShortPressOnlyKey(keyCode: Int) = keyCode in SHORT_PRESS_ONLY_KEYS

    fun shutdown() {
        executor.shutdownNow()
    }
}
