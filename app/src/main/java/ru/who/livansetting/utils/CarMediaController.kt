package ru.who.livansetting.utils

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.session.MediaController
import android.media.session.MediaSession
import android.media.session.MediaSessionManager
import android.os.Build
import android.util.Log
import android.view.KeyEvent
import ru.who.livansetting.services.MediaNotificationListenerService
import java.util.concurrent.ConcurrentHashMap

/**
 * Контроллер для управления медиа-воспроизведением в автомобиле
 * Поддерживает два метода управления: MediaSessions и KeyEvent
 */
class CarMediaController private constructor(private val context: Context) {
    
    companion object {
        private const val TAG = "CarMediaController"
        
        // Константы алгоритмов управления
        const val ALGORITHM_MEDIA_SESSION = 0
        const val ALGORITHM_KEY_EVENT = 1
        
        // Ключи для SharedPreferences
        private const val PREF_MEDIA_CONTROL_ALGORITHM = "media_control_algorithm"
        
        @Volatile
        private var INSTANCE: CarMediaController? = null
        
        fun getInstance(context: Context): CarMediaController {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CarMediaController(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val mediaSessionManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        context.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
    } else {
        null
    }
    
    // Статистика для отладки
    private val stats = ConcurrentHashMap<String, Int>()
    
    init {
        // Инициализируем статистику
        stats["media_session_success"] = 0
        stats["media_session_failed"] = 0
        stats["key_event_success"] = 0
        stats["key_event_failed"] = 0
    }
    
    /**
     * Получает текущий алгоритм управления медиа
     */
    fun getMediaControlAlgorithm(preferences: SharedPreferences): Int {
        return preferences.getInt(PREF_MEDIA_CONTROL_ALGORITHM, ALGORITHM_MEDIA_SESSION)
    }
    
    /**
     * Устанавливает алгоритм управления медиа
     */
    fun setMediaControlAlgorithm(algorithm: Int, preferences: SharedPreferences) {
        preferences.edit()
            .putInt(PREF_MEDIA_CONTROL_ALGORITHM, algorithm)
            .apply()
        Log.d(TAG, "Media control algorithm set to: ${getAlgorithmName(algorithm)}")
    }
    
    /**
     * Получает название алгоритма
     */
    fun getAlgorithmName(algorithm: Int): String {
        return when (algorithm) {
            ALGORITHM_MEDIA_SESSION -> "MediaSession"
            ALGORITHM_KEY_EVENT -> "KeyEvent"
            else -> "Unknown"
        }
    }
    
    /**
     * Выполняет медиа-действие с использованием текущего алгоритма
     * Совместимость с существующим кодом
     */
    fun performMediaAction(actionType: MediaActionType, preferences: SharedPreferences): Boolean {
        return performCurrentMediaSessionAction(actionType, preferences)
    }
    
    /**
     * Выполняет медиа-действие с использованием текущего алгоритма
     */
    fun performCurrentMediaSessionAction(actionType: MediaActionType, preferences: SharedPreferences): Boolean {
        val algorithm = getMediaControlAlgorithm(preferences)
        return when (algorithm) {
            ALGORITHM_MEDIA_SESSION -> performMediaActionViaSession(actionType, preferences)
            ALGORITHM_KEY_EVENT -> performMediaActionViaKeyEvent(actionType, preferences)
            else -> {
                Log.e(TAG, "Unknown algorithm: $algorithm")
                false
            }
        }
    }
    
    /**
     * Выполняет медиа-действие через MediaSession API
     */
    private fun performMediaActionViaSession(actionType: MediaActionType, preferences: SharedPreferences): Boolean {
        try {
            if (mediaSessionManager == null) {
                Log.e(TAG, "MediaSessionManager not available")
                stats["media_session_failed"] = stats["media_session_failed"]!! + 1
                return false
            }
            
            // Получаем компонент NotificationListenerService для доступа к MediaSessions
            val notificationService = MediaNotificationListenerService.getInstance()
            if (notificationService == null) {
                Log.e(TAG, "MediaNotificationListenerService not available")
                stats["media_session_failed"] = stats["media_session_failed"]!! + 1
                return false
            }
            
            val componentName = android.content.ComponentName(context, MediaNotificationListenerService::class.java)
            val activeSessions = mediaSessionManager.getActiveSessions(componentName)
            
            if (activeSessions.isEmpty()) {
                Log.w(TAG, "No active media sessions found")
                stats["media_session_failed"] = stats["media_session_failed"]!! + 1
                return false
            }
            
            // Выбираем первую активную сессию
            val mediaController = activeSessions[0]
            val success = executeMediaAction(mediaController, actionType)
            
            if (success) {
                stats["media_session_success"] = stats["media_session_success"]!! + 1
                Log.d(TAG, "MediaSession action successful: $actionType")
            } else {
                stats["media_session_failed"] = stats["media_session_failed"]!! + 1
                Log.w(TAG, "MediaSession action failed: $actionType")
            }
            
            return success
            
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException accessing media sessions", e)
            stats["media_session_failed"] = stats["media_session_failed"]!! + 1
            return false
        } catch (e: Exception) {
            Log.e(TAG, "Error performing media action via session", e)
            stats["media_session_failed"] = stats["media_session_failed"]!! + 1
            return false
        }
    }
    
    /**
     * Выполняет медиа-действие через эмуляцию KeyEvent
     */
    private fun performMediaActionViaKeyEvent(actionType: MediaActionType, preferences: SharedPreferences): Boolean {
        try {
            val keyCode = when (actionType) {
                MediaActionType.PLAY_PAUSE -> KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                MediaActionType.NEXT -> KeyEvent.KEYCODE_MEDIA_NEXT
                MediaActionType.PREVIOUS -> KeyEvent.KEYCODE_MEDIA_PREVIOUS
            }
            
            // Создаем KeyEvent для нажатия
            val downEvent = KeyEvent(KeyEvent.ACTION_DOWN, keyCode)
            val upEvent = KeyEvent(KeyEvent.ACTION_UP, keyCode)
            
            // Отправляем события через AudioManager
            audioManager.dispatchMediaKeyEvent(downEvent)
            audioManager.dispatchMediaKeyEvent(upEvent)
            
            val success = true // dispatchMediaKeyEvent не возвращает boolean
            
            if (success) {
                stats["key_event_success"] = stats["key_event_success"]!! + 1
                Log.d(TAG, "KeyEvent action successful: $actionType")
            } else {
                stats["key_event_failed"] = stats["key_event_failed"]!! + 1
                Log.w(TAG, "KeyEvent action failed: $actionType")
            }
            
            return success
            
        } catch (e: Exception) {
            Log.e(TAG, "Error performing media action via key event", e)
            stats["key_event_failed"] = stats["key_event_failed"]!! + 1
            return false
        }
    }
    
    /**
     * Выполняет конкретное медиа-действие через MediaController
     */
    private fun executeMediaAction(mediaController: MediaController, actionType: MediaActionType): Boolean {
        return try {
            when (actionType) {
                MediaActionType.PLAY_PAUSE -> {
                    val transportControls = mediaController.transportControls
                    val playbackState = mediaController.playbackState
                    
                    if (playbackState?.state == android.media.session.PlaybackState.STATE_PLAYING) {
                        transportControls.pause()
                    } else {
                        transportControls.play()
                    }
                    true
                }
                MediaActionType.NEXT -> {
                    mediaController.transportControls.skipToNext()
                    true
                }
                MediaActionType.PREVIOUS -> {
                    mediaController.transportControls.skipToPrevious()
                    true
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error executing media action: $actionType", e)
            false
        }
    }
    
    /**
     * Получает расширенную статистику работы алгоритмов
     */
    fun getExtendedAlgorithmStats(preferences: SharedPreferences): Map<String, Any> {
        val algorithm = getMediaControlAlgorithm(preferences)
        val algorithmName = getAlgorithmName(algorithm)
        
        return mapOf(
            "current_algorithm" to algorithm,
            "algorithm_name" to algorithmName,
            "media_session_success" to (stats["media_session_success"] ?: 0),
            "media_session_failed" to (stats["media_session_failed"] ?: 0),
            "key_event_success" to (stats["key_event_success"] ?: 0),
            "key_event_failed" to (stats["key_event_failed"] ?: 0),
            "media_session_manager_available" to (mediaSessionManager != null),
            "notification_service_available" to (MediaNotificationListenerService.getInstance() != null)
        )
    }
    
    /**
     * Сбрасывает статистику
     */
    fun resetStats() {
        stats["media_session_success"] = 0
        stats["media_session_failed"] = 0
        stats["key_event_success"] = 0
        stats["key_event_failed"] = 0
        Log.d(TAG, "Stats reset")
    }
    
    /**
     * Проверяет доступность MediaSession API
     */
    fun isMediaSessionAvailable(): Boolean {
        return mediaSessionManager != null && 
               MediaNotificationListenerService.getInstance() != null &&
               MediaNotificationListenerService.isServiceEnabled(context)
    }
    
    /**
     * Проверяет доступность KeyEvent API
     */
    fun isKeyEventAvailable(): Boolean {
        return audioManager != null
    }
}
