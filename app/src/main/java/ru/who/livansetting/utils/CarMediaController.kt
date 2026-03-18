package ru.who.livansetting.utils

import android.content.Context
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.os.Build
import android.util.Log
import ru.who.livansetting.core.MediaNotificationListenerService

/**
 * Контроллер для управления медиа-воспроизведением в автомобиле через MediaSession API.
 */
class CarMediaController private constructor(private val context: Context) {
    
    companion object {
        private const val TAG = "CarMediaController"

        @Volatile
        private var INSTANCE: CarMediaController? = null
        
        fun getInstance(context: Context): CarMediaController {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CarMediaController(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    private val mediaSessionManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        context.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
    } else {
        null
    }
    
    /**
     * Выполняет медиа-действие через MediaSession API.
     */
    fun performCurrentMediaSessionAction(actionType: MediaActionType): Boolean {
        try {
            if (MediaNotificationListenerService.getInstance() == null) {
                Log.w(TAG, "MediaNotificationListenerService is not running")
                return false
            }
            
            val componentName = android.content.ComponentName(context, MediaNotificationListenerService::class.java)
            val activeSessions = mediaSessionManager?.getActiveSessions(componentName) ?: return false

            if (activeSessions.isEmpty()) {
                Log.w(TAG, "No active media sessions found")
                return false
            }
            
            // 1. Ищем реально играющую сессию
            var controller = activeSessions.find { 
                it.playbackState?.state == android.media.session.PlaybackState.STATE_PLAYING 
            }
            
            // 2. Ищем сессию на паузе (если играющих нет)
            if (controller == null) {
                controller = activeSessions.find { 
                    it.playbackState?.state == android.media.session.PlaybackState.STATE_PAUSED 
                }
            }
            
            // 3. Пытаемся избежать "Радио", если есть другие варианты
            if (controller == null) {
                controller = activeSessions.find { 
                    val pkg = it.packageName.lowercase()
                    !pkg.contains("radio") && !pkg.contains("tuner") && !pkg.contains("fm")
                }
            }
            
            // 4. Берем первую доступную
            val finalController = controller ?: activeSessions[0]
            
            Log.d(TAG, "Executing $actionType on ${finalController.packageName}")
            return executeMediaAction(finalController, actionType)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error in MediaSession action", e)
            return false
        }
    }
    
    private fun executeMediaAction(mediaController: MediaController, actionType: MediaActionType): Boolean {
        return try {
            val controls = mediaController.transportControls
            when (actionType) {
                MediaActionType.PLAY_PAUSE -> {
                    if (mediaController.playbackState?.state == android.media.session.PlaybackState.STATE_PLAYING) {
                        controls.pause()
                    } else {
                        controls.play()
                    }
                }
                MediaActionType.NEXT -> controls.skipToNext()
                MediaActionType.PREVIOUS -> controls.skipToPrevious()
            }
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to execute media action", e)
            false
        }
    }
}
