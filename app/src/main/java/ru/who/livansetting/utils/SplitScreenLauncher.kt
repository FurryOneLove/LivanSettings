package ru.who.livansetting.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log

/**
 * Утилита для запуска приложений в режиме разделенного экрана (Split Screen)
 * Оптимизирована для Android 9 и выше.
 */
object SplitScreenLauncher {
    
    private const val TAG = "SplitScreenLauncher"
    
    /**
     * Проверяет поддержку split screen режима на устройстве
     */
    fun isSplitScreenSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }
    
    /**
     * Запуск split screen режима по именам пакетов
     * @param context контекст приложения
     * @param primaryPackageName имя пакета первого приложения (слева/сверху)
     * @param secondaryPackageName имя пакета второго приложения (справа/снизу)
     * @return результат операции
     */
    fun launchSplitScreenModeByPackage(
        context: Context,
        primaryPackageName: String,
        secondaryPackageName: String
    ): LaunchResult {
        if (!isSplitScreenSupported()) {
            return LaunchResult(false, "Split screen mode is not supported on this device")
        }
        
        if (primaryPackageName.isEmpty() || secondaryPackageName.isEmpty()) {
            return LaunchResult(false, "Package names cannot be empty")
        }
        
        val packageManager = context.packageManager
        
        // Получение Intent для первого приложения
        val intent1 = packageManager.getLaunchIntentForPackage(primaryPackageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        }
        
        // Получение Intent для второго приложения
        val intent2 = packageManager.getLaunchIntentForPackage(secondaryPackageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        }
        
        if (intent1 == null || intent2 == null) {
            return LaunchResult(false, "One or both apps are not installed")
        }
        
        try {
            Log.d(TAG, "Launching split screen: $primaryPackageName | $secondaryPackageName")
            
            // Настройка параметров для первого приложения (PRIMARY)
            val options1 = android.app.ActivityOptions.makeBasic()
            val bundle1 = options1.toBundle()
            // Константы для Android 9
            bundle1.putInt("android.activity.windowingMode", 3) // WINDOWING_MODE_SPLIT_SCREEN_PRIMARY
            bundle1.putInt("android:activity.splitScreenCreateMode", 0) // 0 = TOP_OR_LEFT
            
            context.startActivity(intent1, bundle1)
            
            // Задержка перед запуском второго приложения для стабилизации режима
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    // Настройка параметров для второго приложения (SECONDARY)
                    val options2 = android.app.ActivityOptions.makeBasic()
                    val bundle2 = options2.toBundle()
                    bundle2.putInt("android.activity.windowingMode", 4) // WINDOWING_MODE_SPLIT_SCREEN_SECONDARY
                    
                    context.startActivity(intent2, bundle2)
                } catch (e: Exception) {
                    Log.e(TAG, "Error launching secondary app", e)
                }
            }, 800) // 800ms обычно достаточно для перестроения UI
            
            return LaunchResult(true, primaryApp = primaryPackageName, secondaryApp = secondaryPackageName)
        } catch (e: Exception) {
            Log.e(TAG, "Error launching split screen mode", e)
            return LaunchResult(false, "Error: ${e.message}")
        }
    }
    
    /**
     * Результат запуска split screen
     */
    data class LaunchResult(
        val success: Boolean,
        val errorMessage: String? = null,
        val primaryApp: String? = null,
        val secondaryApp: String? = null
    )
}
