package ru.who.livansetting.utils

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log

/**
 * Утилита для запуска приложений в режиме разделенного экрана (Split Screen)
 * Использует многоуровневый подход: reflection → eCarX broadcast → AOSP fallback.
 */
object SplitScreenLauncher {

    private const val TAG = "SplitScreenLauncher"

    private const val WINDOWING_MODE_SPLIT_PRIMARY = 3
    private const val WINDOWING_MODE_SPLIT_SECONDARY = 4

    /**
     * Проверяет поддержку split screen режима на устройстве
     */
    fun isSplitScreenSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    /**
     * Запуск split screen режима по именам пакетов.
     * Пробует три уровня: reflection windowing mode, eCarX broadcast, AOSP bundle fallback.
     */
    fun launchSplitScreenModeByPackage(
        context: Context,
        primaryPackageName: String,
        secondaryPackageName: String
    ): LaunchResult {
        Log.d(TAG, "launchSplitScreenModeByPackage: primary=$primaryPackageName secondary=$secondaryPackageName")

        if (!isSplitScreenSupported()) {
            return LaunchResult(false, "Split screen mode is not supported on this device")
        }

        if (primaryPackageName.isEmpty() || secondaryPackageName.isEmpty()) {
            return LaunchResult(false, "Package names cannot be empty")
        }

        val packageManager = context.packageManager

        val intent1 = packageManager.getLaunchIntentForPackage(primaryPackageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        }
        val intent2 = packageManager.getLaunchIntentForPackage(secondaryPackageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        }

        if (intent1 == null || intent2 == null) {
            return LaunchResult(false, "One or both apps are not installed")
        }

        // --- Tier 1: Reflective windowing mode (most likely to work on eCarX) ---
        try {
            Log.d(TAG, "Tier 1: attempting reflective setLaunchWindowingMode")
            val options1 = ActivityOptions.makeBasic()
            val options2 = ActivityOptions.makeBasic()

            val primarySet = setLaunchWindowingMode(options1, WINDOWING_MODE_SPLIT_PRIMARY)
            val secondarySet = setLaunchWindowingMode(options2, WINDOWING_MODE_SPLIT_SECONDARY)

            if (primarySet && secondarySet) {
                context.startActivity(intent1, options1.toBundle())
                Handler(Looper.getMainLooper()).postDelayed({
                    try {
                        context.startActivity(intent2, options2.toBundle())
                    } catch (t: Throwable) {
                        Log.e(TAG, "Tier 1: secondary app launch failed", t)
                    }
                }, 800)
                Log.d(TAG, "Tier 1: success")
                return LaunchResult(true, primaryApp = primaryPackageName, secondaryApp = secondaryPackageName)
            }
            Log.w(TAG, "Tier 1: setLaunchWindowingMode reflection not available, skipping")
        } catch (t: Throwable) {
            Log.e(TAG, "Tier 1 failed", t)
        }

        // --- Tier 2: eCarX broadcast ---
        try {
            Log.d(TAG, "Tier 2: attempting eCarX split screen broadcast")
            context.sendBroadcast(Intent("ecarx.intent.action.SPLIT_SCREEN").apply {
                putExtra("primary_package", primaryPackageName)
                putExtra("secondary_package", secondaryPackageName)
            })
            Log.d(TAG, "Tier 2: broadcast sent")
            return LaunchResult(true, primaryApp = primaryPackageName, secondaryApp = secondaryPackageName)
        } catch (t: Throwable) {
            Log.e(TAG, "Tier 2 failed", t)
        }

        // --- Tier 3: AOSP bundle fallback ---
        try {
            Log.d(TAG, "Tier 3: attempting AOSP bundle windowing mode")
            val options1 = ActivityOptions.makeBasic()
            val bundle1 = options1.toBundle()
            bundle1.putInt("android.activity.windowingMode", WINDOWING_MODE_SPLIT_PRIMARY)
            bundle1.putInt("android:activity.splitScreenCreateMode", 0)

            context.startActivity(intent1, bundle1)

            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    val options2 = ActivityOptions.makeBasic()
                    val bundle2 = options2.toBundle()
                    bundle2.putInt("android.activity.windowingMode", WINDOWING_MODE_SPLIT_SECONDARY)
                    context.startActivity(intent2, bundle2)
                } catch (t: Throwable) {
                    Log.e(TAG, "Tier 3: secondary app launch failed", t)
                }
            }, 800)

            Log.d(TAG, "Tier 3: success")
            return LaunchResult(true, primaryApp = primaryPackageName, secondaryApp = secondaryPackageName)
        } catch (t: Throwable) {
            Log.e(TAG, "Tier 3 failed", t)
        }

        return LaunchResult(false, "All tiers failed")
    }

    private fun setLaunchWindowingMode(options: ActivityOptions, mode: Int): Boolean {
        return try {
            val method = ActivityOptions::class.java.getDeclaredMethod("setLaunchWindowingMode", Int::class.java)
            method.invoke(options, mode)
            true
        } catch (t: Throwable) {
            Log.w(TAG, "setLaunchWindowingMode reflection failed: ${t.message}")
            false
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
