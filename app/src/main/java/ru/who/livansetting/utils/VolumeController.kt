package ru.who.livansetting.utils

import android.content.Context
import android.media.AudioManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Улучшенный контроллер для регулировки громкости
 * Полностью переписанный с надежным управлением состоянием
 */
class VolumeController(private val context: Context) {
    
    companion object {
        private const val TAG = "VolumeController"
        private const val VOLUME_STEP = 1 // Шаг изменения громкости
        private const val VOLUME_UPDATE_INTERVAL = 150L // Интервал обновления в миллисекундах
        private const val VOLUME_UPDATE_DELAY = 200L // Задержка перед началом непрерывного изменения
        private const val DEBOUNCE_DURATION = 50L // Защита от дребезга
    }
    
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val executor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor { r ->
        Thread(r, "VolumeController").apply { isDaemon = true }
    }
    private val mainHandler = Handler(Looper.getMainLooper())
    
    private var volumeUpTask: java.util.concurrent.ScheduledFuture<*>? = null
    private var volumeDownTask: java.util.concurrent.ScheduledFuture<*>? = null
    
    // Атомарные флаги для надежного управления состоянием
    private val isVolumeUpPressed = AtomicBoolean(false)
    private val isVolumeDownPressed = AtomicBoolean(false)
    private val isVolumeUpContinuous = AtomicBoolean(false)
    private val isVolumeDownContinuous = AtomicBoolean(false)
    
    // Время последнего изменения для debounce
    private var lastVolumeChangeTime = 0L
    
    /**
     * Начинает непрерывное увеличение громкости
     */
    fun startVolumeUp() {
        if (isVolumeUpPressed.get()) {
            Log.d(TAG, "Volume up already in progress, ignoring")
            return
        }
        
        isVolumeUpPressed.set(true)
        Log.d(TAG, "Starting continuous volume up")
        
        // Сначала делаем одно изменение громкости
        adjustVolumeUp(playSound = true, autoStop = true)
        
        // Затем запускаем периодическое изменение с задержкой
        volumeUpTask = executor.scheduleAtFixedRate({
            if (isVolumeUpPressed.get()) {
                adjustVolumeUp(playSound = false, autoStop = true)
            }
        }, VOLUME_UPDATE_DELAY, VOLUME_UPDATE_INTERVAL, TimeUnit.MILLISECONDS)
    }
    
    /**
     * Останавливает непрерывное увеличение громкости
     */
    fun stopVolumeUp() {
        if (!isVolumeUpPressed.get()) {
            return
        }
        
        isVolumeUpPressed.set(false)
        isVolumeUpContinuous.set(false)
        volumeUpTask?.cancel(false)
        volumeUpTask = null
        Log.d(TAG, "Stopped continuous volume up")
    }
    
    /**
     * Начинает непрерывное уменьшение громкости
     */
    fun startVolumeDown() {
        if (isVolumeDownPressed.get()) {
            Log.d(TAG, "Volume down already in progress, ignoring")
            return
        }
        
        isVolumeDownPressed.set(true)
        Log.d(TAG, "Starting continuous volume down")
        
        // Сначала делаем одно изменение громкости
        adjustVolumeDown(playSound = true, autoStop = true)
        
        // Затем запускаем периодическое изменение с задержкой
        volumeDownTask = executor.scheduleAtFixedRate({
            if (isVolumeDownPressed.get()) {
                adjustVolumeDown(playSound = false, autoStop = true)
            }
        }, VOLUME_UPDATE_DELAY, VOLUME_UPDATE_INTERVAL, TimeUnit.MILLISECONDS)
    }
    
    /**
     * Останавливает непрерывное уменьшение громкости
     */
    fun stopVolumeDown() {
        if (!isVolumeDownPressed.get()) {
            return
        }
        
        isVolumeDownPressed.set(false)
        isVolumeDownContinuous.set(false)
        volumeDownTask?.cancel(false)
        volumeDownTask = null
        Log.d(TAG, "Stopped continuous volume down")
    }
    
    /**
     * Увеличивает громкость на один шаг
     * @param playSound true если нужно воспроизвести звук (для одиночного изменения)
     * @param autoStop true если нужно автоматически остановить при достижении максимума
     */
    private fun adjustVolumeUp(playSound: Boolean = false, autoStop: Boolean = false) {
        try {
            val currentTime = System.currentTimeMillis()
            
            // Debounce проверка
            if (currentTime - lastVolumeChangeTime < DEBOUNCE_DURATION) {
                Log.d(TAG, "Debounce: ignoring volume up change (too soon after last change)")
                return
            }
            
            val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            
            // Валидация значений громкости
            if (currentVolume < 0 || maxVolume <= 0) {
                Log.e(TAG, "Invalid volume values: current=$currentVolume, max=$maxVolume")
                return
            }
            
            if (currentVolume < maxVolume) {
                val newVolume = minOf(currentVolume + VOLUME_STEP, maxVolume)
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, AudioManager.FLAG_SHOW_UI)
                lastVolumeChangeTime = currentTime
                
                val logPrefix = if (playSound) "Volume up once" else "Volume up"
                Log.d(TAG, "$logPrefix: changed from $currentVolume to $newVolume (max: $maxVolume)")
                
                // Для непрерывной регулировки не останавливаем автоматически
                // Остановка происходит только при отпускании кнопки
            } else {
                val logPrefix = if (playSound) "Volume up once" else "Volume up"
                Log.d(TAG, "$logPrefix: already at maximum ($maxVolume)")
                
                // Если достигли максимума и autoStop включен, останавливаем
                if (autoStop) {
                    stopVolumeUp()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error adjusting volume up", e)
            if (autoStop) {
                stopVolumeUp()
            }
        }
    }
    
    /**
     * Уменьшает громкость на один шаг
     * @param playSound true если нужно воспроизвести звук (для одиночного изменения)
     * @param autoStop true если нужно автоматически остановить при достижении минимума
     */
    private fun adjustVolumeDown(playSound: Boolean = false, autoStop: Boolean = false) {
        try {
            val currentTime = System.currentTimeMillis()
            
            // Debounce проверка
            if (currentTime - lastVolumeChangeTime < DEBOUNCE_DURATION) {
                Log.d(TAG, "Debounce: ignoring volume down change (too soon after last change)")
                return
            }
            
            val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            val minVolume = 0
            
            // Валидация значений громкости
            if (currentVolume < 0) {
                Log.e(TAG, "Invalid volume value: current=$currentVolume")
                return
            }
            
            if (currentVolume > minVolume) {
                val newVolume = maxOf(currentVolume - VOLUME_STEP, minVolume)
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, AudioManager.FLAG_SHOW_UI)
                lastVolumeChangeTime = currentTime
                
                val logPrefix = if (playSound) "Volume down once" else "Volume down"
                Log.d(TAG, "$logPrefix: changed from $currentVolume to $newVolume (min: $minVolume)")
                
                // Для непрерывной регулировки не останавливаем автоматически
                // Остановка происходит только при отпускании кнопки
            } else {
                val logPrefix = if (playSound) "Volume down once" else "Volume down"
                Log.d(TAG, "$logPrefix: already at minimum ($minVolume)")
                
                // Если достигли минимума и autoStop включен, останавливаем
                if (autoStop) {
                    stopVolumeDown()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error adjusting volume down", e)
            if (autoStop) {
                stopVolumeDown()
            }
        }
    }
    
    /**
     * Увеличивает громкость на один шаг (публичный метод для одиночного изменения)
     */
    fun adjustVolumeUpOnce() {
        adjustVolumeUp(playSound = true, autoStop = false)
    }
    
    /**
     * Уменьшает громкость на один шаг (публичный метод для одиночного изменения)
     */
    fun adjustVolumeDownOnce() {
        adjustVolumeDown(playSound = true, autoStop = false)
    }
    
    /**
     * Останавливает все операции с громкостью
     */
    fun stopAll() {
        stopVolumeUp()
        stopVolumeDown()
        isVolumeUpPressed.set(false)
        isVolumeDownPressed.set(false)
        isVolumeUpContinuous.set(false)
        isVolumeDownContinuous.set(false)
    }
    
    /**
     * Проверяет, активна ли регулировка громкости
     */
    fun isVolumeAdjustmentActive(): Boolean {
        return isVolumeUpPressed.get() || isVolumeDownPressed.get() || 
               isVolumeUpContinuous.get() || isVolumeDownContinuous.get()
    }
    
    /**
     * Освобождает ресурсы
     */
    fun shutdown() {
        stopAll()
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
