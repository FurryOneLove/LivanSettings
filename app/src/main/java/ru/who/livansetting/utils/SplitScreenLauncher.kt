package ru.who.livansetting.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityOptionsCompat

/**
 * Утилита для запуска приложений в режиме разделенного экрана (Split Screen)
 */
object SplitScreenLauncher {
    
    private const val TAG = "SplitScreenLauncher"
    
    // Константы для режимов окна
    object ActivityOptionsFlags {
        const val KEY_LAUNCH_WINDOWING_MODE = "android.activity.windowingMode"
        const val KEY_SPLIT_SCREEN_CREATE_MODE = "android:activity.splitScreenCreateMode"
        const val SPLIT_SCREEN_CREATE_MODE_BOTTOM_OR_RIGHT = 1
        const val SPLIT_SCREEN_CREATE_MODE_TOP_OR_LEFT = 0
        const val WINDOWING_MODE_SPLIT_SCREEN_PRIMARY = 3
        const val WINDOWING_MODE_SPLIT_SCREEN_SECONDARY = 4
    }
    
    // Константы для типов экранов
    object ScreenType {
        const val SCREEN_TYPE_BOTTOM = 4
        const val SCREEN_TYPE_LEFT = 1
        const val SCREEN_TYPE_RIGHT = 2
        const val SCREEN_TYPE_TOP = 3
    }
    
    // Результат запуска split screen
    data class LaunchResult(
        val success: Boolean,
        val errorMessage: String? = null,
        val primaryApp: String? = null,
        val secondaryApp: String? = null
    )
    
    /**
     * Проверяет поддержку split screen режима на устройстве
     */
    fun isSplitScreenSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }
    
    /**
     * Проверяет возможность запуска приложения
     */
    private fun isCallable(context: Context, intent: Intent): Boolean {
        return context.packageManager.resolveActivity(intent, 0) != null
    }
    
    /**
     * Основной метод запуска split screen режима
     * @param context контекст приложения
     * @param app1Intent Intent первого приложения
     * @param app2Intent Intent второго приложения
     * @param handler Handler для отложенных операций
     * @return результат операции (null = успех, строка = ошибка)
     */
    fun launchSplitScreenMode(
        context: Context,
        app1Intent: Intent,
        app2Intent: Intent,
        handler: Handler
    ): String? {
        // Валидация входных параметров
        if (context == null || app1Intent == null || app2Intent == null || handler == null) {
            return "context == null || app1Intent == null || app2Intent == null || handler == null"
        }
        
        // Настройка первого приложения
        app1Intent.addCategory("android.intent.category.LAUNCHER")
        app1Intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        
        // Создание Intent для домашнего экрана
        val homeIntent = Intent("android.intent.action.MAIN").apply {
            addCategory("android.intent.category.HOME")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        // Проверка возможности запуска приложений
        if (!isCallable(context, app1Intent)) {
            return "!isCallable(context, app1Intent)"
        }
        if (!isCallable(context, homeIntent)) {
            return "!isCallable(context, homeIntent)"
        }
        
        // Запуск первого приложения
        context.startActivity(app1Intent)
        
        // Запуск домашнего экрана через 50мс
        handler.postDelayed({
            context.startActivity(homeIntent)
        }, 50L)
        
        // Запуск split screen режима через 500мс
        handler.postDelayed({
            launchSplitScreenApps(app1Intent, app2Intent, context)
        }, 500L)
        
        return null // Успешное выполнение
    }
    
    /**
     * Запуск приложений в split screen режиме
     */
    private fun launchSplitScreenApps(app1Intent: Intent, app2Intent: Intent, context: Context) {
        // Настройка флагов для обоих приложений
        app1Intent.addCategory("android.intent.category.LAUNCHER")
        app1Intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        
        app2Intent.addCategory("android.intent.category.LAUNCHER")
        app2Intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        
        // Создание Bundle с параметрами split screen
        val bundle = ActivityOptionsCompat.makeBasic().toBundle()
        if (bundle != null) {
            // Установка режима окна для split screen
            bundle.putInt(ActivityOptionsFlags.KEY_LAUNCH_WINDOWING_MODE, 
                         ActivityOptionsFlags.WINDOWING_MODE_SPLIT_SCREEN_PRIMARY)
            
            // Установка позиции создания (TOP_OR_LEFT)
            bundle.putInt(ActivityOptionsFlags.KEY_SPLIT_SCREEN_CREATE_MODE, 
                         ActivityOptionsFlags.SPLIT_SCREEN_CREATE_MODE_TOP_OR_LEFT)
        }
        
        // Запуск обоих приложений в split screen режиме
        // Порядок важен: сначала второе приложение, затем первое
        context.startActivities(arrayOf(app2Intent, app1Intent), bundle)
    }
    
    /**
     * Запуск split screen режима по именам пакетов
     * @param context контекст приложения
     * @param package1 имя пакета первого приложения
     * @param package2 имя пакета второго приложения
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
        
        if (primaryPackageName == secondaryPackageName) {
            return LaunchResult(false, "Cannot launch the same app in split screen mode")
        }
        
        val packageManager = context.packageManager
        
        // Получение Intent для первого приложения
        val app1Intent = packageManager.getLaunchIntentForPackage(primaryPackageName)
        if (app1Intent == null) {
            return LaunchResult(false, "Primary app '$primaryPackageName' is not installed")
        }
        
        // Получение Intent для второго приложения
        val app2Intent = packageManager.getLaunchIntentForPackage(secondaryPackageName)
        if (app2Intent == null) {
            return LaunchResult(false, "Secondary app '$secondaryPackageName' is not installed")
        }
        
        // Создание Handler для отложенных операций
        val handler = Handler(Looper.getMainLooper())
        
        // Запуск split screen режима
        val result = launchSplitScreenMode(context, app1Intent, app2Intent, handler)
        
        return if (result == null) {
            LaunchResult(true, primaryApp = primaryPackageName, secondaryApp = secondaryPackageName)
        } else {
            LaunchResult(false, result)
        }
    }
    
    /**
     * Запуск split screen режима с указанной позицией
     * @param context контекст приложения
     * @param package1 имя пакета первого приложения
     * @param package2 имя пакета второго приложения
     * @param position позиция (0 = TOP_OR_LEFT, 1 = BOTTOM_OR_RIGHT)
     * @return результат операции
     */
    fun launchSplitScreenWithPosition(
        context: Context,
        package1: String,
        package2: String,
        position: Int
    ): LaunchResult {
        if (!isSplitScreenSupported()) {
            return LaunchResult(false, "Split screen mode is not supported on this device")
        }
        
        if (package1.isEmpty() || package2.isEmpty()) {
            return LaunchResult(false, "Package names cannot be empty")
        }
        
        if (package1 == package2) {
            return LaunchResult(false, "Cannot launch the same app in split screen mode")
        }
        
        val packageManager = context.packageManager
        
        // Получение Intent для приложений
        val app1Intent = packageManager.getLaunchIntentForPackage(package1)
        val app2Intent = packageManager.getLaunchIntentForPackage(package2)
        
        if (app1Intent == null) {
            return LaunchResult(false, "App '$package1' is not installed")
        }
        if (app2Intent == null) {
            return LaunchResult(false, "App '$package2' is not installed")
        }
        
        // Настройка флагов для обоих приложений
        app1Intent.addCategory("android.intent.category.LAUNCHER")
        app1Intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        
        app2Intent.addCategory("android.intent.category.LAUNCHER")
        app2Intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        
        // Создание Bundle с параметрами split screen
        val bundle = ActivityOptionsCompat.makeBasic().toBundle()
        if (bundle != null) {
            // Установка режима окна для split screen
            bundle.putInt(ActivityOptionsFlags.KEY_LAUNCH_WINDOWING_MODE, 
                         ActivityOptionsFlags.WINDOWING_MODE_SPLIT_SCREEN_PRIMARY)
            
            // Установка позиции создания
            bundle.putInt(ActivityOptionsFlags.KEY_SPLIT_SCREEN_CREATE_MODE, position)
        }
        
        try {
            // Запуск обоих приложений в split screen режиме
            context.startActivities(arrayOf(app2Intent, app1Intent), bundle)
            return LaunchResult(true, primaryApp = package1, secondaryApp = package2)
        } catch (e: Exception) {
            Log.e(TAG, "Error launching split screen", e)
            return LaunchResult(false, "Error launching split screen: ${e.message}")
        }
    }
    
    /**
     * Проверяет, поддерживает ли приложение split screen режим
     * @param context контекст приложения
     * @param packageName имя пакета приложения
     * @return true если приложение поддерживает split screen
     */
    fun isPackageSupportedSplitScreen(context: Context, packageName: String): Boolean {
        return try {
            val packageManager = context.packageManager
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            launchIntent != null && isCallable(context, launchIntent)
        } catch (e: Exception) {
            Log.e(TAG, "Error checking split screen support for $packageName", e)
            false
        }
    }
    
    /**
     * Получает список всех установленных приложений, поддерживающих split screen
     * @param context контекст приложения
     * @return список пакетов приложений
     */
    fun getSupportedSplitScreenApps(context: Context): List<String> {
        val packageManager = context.packageManager
        val installedPackages = packageManager.getInstalledPackages(0)
        val supportedApps = mutableListOf<String>()
        
        for (packageInfo in installedPackages) {
            val packageName = packageInfo.packageName
            if (isPackageSupportedSplitScreen(context, packageName)) {
                supportedApps.add(packageName)
            }
        }
        
        return supportedApps.sorted()
    }
}
