package ru.who.livansetting

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "BootReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                Log.d(TAG, "receive action boot completed")
                // Устанавливаем флаг завершения загрузки системы
                ru.who.livansetting.services.MainService.setSystemBootComplete()
                startMainService(context)
            }
            Intent.ACTION_LOCKED_BOOT_COMPLETED -> {
                Log.d(TAG, "receive action locked boot completed")
                // Устанавливаем флаг завершения загрузки системы
                ru.who.livansetting.services.MainService.setSystemBootComplete()
                startMainService(context)
            }
            Intent.ACTION_MEDIA_MOUNTED,
            Intent.ACTION_MEDIA_EJECT,
            Intent.ACTION_MEDIA_REMOVED,
            Intent.ACTION_MEDIA_UNMOUNTED,
            Intent.ACTION_MEDIA_BAD_REMOVAL -> {
                Log.d(TAG, "receive media action: ${intent.action}")
                // Можно добавить дополнительную логику для медиа событий если нужно
            }
        }
    }
    
    /**
     * Запускает MainService
     */
    private fun startMainService(context: Context) {
        try {
            Log.d(TAG, "Starting MainService")
            ru.who.livansetting.services.MainService.startService(context)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start MainService", e)
        }
    }
}