package ru.who.livansetting

import android.content.Context
import android.util.Log

class KeyInputHandler(private val context: Context) {
    
    companion object {
        private const val TAG = "KeyInputHandler"
    }
    
    fun initialize() {
        Log.d(TAG, "Initializing KeyInputHandler")
        
        // Сервис всегда запускается
        startService()
    }
    
    fun startService() {
        Log.d(TAG, "Starting MainService")
        ru.who.livansetting.services.MainService.startService(context)
    }
    
    fun release() {
        Log.d(TAG, "Releasing KeyInputHandler")
        // Сервис будет продолжать работать в фоне
        // Останавливаем только если пользователь явно отключил сервис
    }
}