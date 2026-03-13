package ru.who.livansetting.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

/**
 * Менеджер для отслеживания первого запуска приложения
 */
class FirstRunManager(private val context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "first_run_prefs"
        private const val KEY_FIRST_RUN = "is_first_run"
        private const val KEY_NOTIFICATION_PERMISSION_REQUESTED = "notification_permission_requested"
        private const val TAG = "FirstRunManager"
    }
    
    /**
     * Проверяет, является ли это первым запуском приложения
     */
    fun isFirstRun(): Boolean {
        val isFirstRun = prefs.getBoolean(KEY_FIRST_RUN, true)
        Log.d(TAG, "isFirstRun: $isFirstRun")
        return isFirstRun
    }
    
    /**
     * Отмечает, что первый запуск был выполнен
     */
    fun markFirstRunCompleted() {
        prefs.edit()
            .putBoolean(KEY_FIRST_RUN, false)
            .apply()
        Log.d(TAG, "First run marked as completed")
    }
    
    /**
     * Сбрасывает флаг первого запуска (для тестирования)
     */
    fun resetFirstRun() {
        prefs.edit()
            .putBoolean(KEY_FIRST_RUN, true)
            .apply()
        Log.d(TAG, "First run flag reset")
    }
    
    /**
     * Проверяет, нужно ли запросить разрешение на доступ к уведомлениям
     */
    fun shouldRequestNotificationPermission(): Boolean {
        val permissionRequested = prefs.getBoolean(KEY_NOTIFICATION_PERMISSION_REQUESTED, false)
        val isEnabled = ru.who.livansetting.services.MediaNotificationListenerService.isServiceEnabled(context)
        
        Log.d(TAG, "Permission requested: $permissionRequested, Service enabled: $isEnabled")
        return !permissionRequested || !isEnabled
    }
    
    /**
     * Отмечает, что запрос разрешения был выполнен
     */
    fun markNotificationPermissionRequested() {
        prefs.edit()
            .putBoolean(KEY_NOTIFICATION_PERMISSION_REQUESTED, true)
            .apply()
        Log.d(TAG, "Notification permission request marked as completed")
    }
    
    /**
     * Запрашивает разрешение на доступ к уведомлениям
     */
    fun requestNotificationPermission(activity: Activity) {
        try {
            Log.d(TAG, "Requesting notification listener permission")
            
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            
            activity.startActivity(intent)
            markNotificationPermissionRequested()
            
        } catch (e: Exception) {
            Log.e(TAG, "Error requesting notification permission", e)
        }
    }
    
    /**
     * Проверяет, предоставлено ли разрешение на доступ к уведомлениям
     */
    fun isNotificationPermissionGranted(): Boolean {
        return ru.who.livansetting.services.MediaNotificationListenerService.isServiceEnabled(context)
    }
}
