package ru.who.livansetting.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import ru.who.livansetting.MainActivity

/**
 * NotificationListenerService для работы с MediaSession API
 * Позволяет получать активные медиа-сессии через MediaSessionManager
 */
class MediaNotificationListenerService : NotificationListenerService() {
    
    companion object {
        private const val TAG = "MediaNotificationListener"
        private const val NOTIFICATION_ID = 2
        private const val CHANNEL_ID = "media_notification_listener_channel"
        
        @Volatile
        private var instance: MediaNotificationListenerService? = null
        
        fun getInstance(): MediaNotificationListenerService? = instance
        
        fun isServiceEnabled(context: Context): Boolean {
            return try {
                val mediaSessionManager = context.getSystemService(Context.MEDIA_SESSION_SERVICE) as android.media.session.MediaSessionManager
                val componentName = android.content.ComponentName(context, MediaNotificationListenerService::class.java)
                
                // Пытаемся получить активные сессии - если получится, значит разрешение есть
                val sessions = mediaSessionManager.getActiveSessions(componentName)
                Log.d(TAG, "Can access media sessions: ${sessions != null}")
                sessions != null
            } catch (e: SecurityException) {
                Log.d(TAG, "SecurityException - permission not granted")
                false
            } catch (e: Exception) {
                Log.e(TAG, "Error accessing media sessions", e)
                false
            }
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "MediaNotificationListenerService created")
        instance = this
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MediaNotificationListenerService destroyed")
        instance = null
    }
    
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        // Обработка новых уведомлений (если нужно)
        super.onNotificationPosted(sbn)
    }
    
    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        // Обработка удаленных уведомлений (если нужно)
        super.onNotificationRemoved(sbn)
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Media Notification Listener",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Служба для работы с медиа-сессиями"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Media Session Service")
            .setContentText("Служба для управления медиа-сессиями")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}
