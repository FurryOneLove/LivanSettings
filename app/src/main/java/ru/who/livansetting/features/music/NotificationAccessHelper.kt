package ru.who.livansetting.features.music

import android.content.Context
import android.provider.Settings
import android.util.Log

/**
 * Самостоятельно выдаёт приложению доступ к уведомлениям (NotificationListener),
 * чтобы не заставлять пользователя искать пункт в настройках ГУ (которого там
 * может не быть).
 *
 * Работает двумя путями:
 *  1) Settings.Secure напрямую — если у приложения есть WRITE_SECURE_SETTINGS
 *     (доступно системным приложениям без root).
 *  2) через su (root) — если первый путь не сработал.
 *
 * Безопасно вызывать многократно: если доступ уже есть — ничего не делает.
 */
object NotificationAccessHelper {

    private const val TAG = "NotifAccessHelper"
    private const val KEY = "enabled_notification_listeners"

    /** Полное имя компонента слушателя уведомлений. */
    private fun component(context: Context): String =
        "${context.packageName}/${context.packageName}.core.MediaNotificationListenerService"

    /** Уже выдан ли доступ. */
    fun isGranted(context: Context): Boolean {
        return try {
            val flat = Settings.Secure.getString(
                context.contentResolver, KEY
            ) ?: return false
            flat.split(":").any { it.equals(component(context), ignoreCase = true) }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Пытается выдать доступ. Возвращает true, если после попытки доступ есть.
     */
    fun grant(context: Context): Boolean {
        if (isGranted(context)) {
            Log.d(TAG, "notification access already granted")
            return true
        }

        // Путь 1: запись в Settings.Secure (нужен WRITE_SECURE_SETTINGS).
        if (grantViaSecureSettings(context)) {
            Log.i(TAG, "granted via Settings.Secure")
            return true
        }

        // Путь 2: через root.
        if (grantViaRoot(context)) {
            Log.i(TAG, "granted via root")
            return true
        }

        Log.w(TAG, "could not grant notification access automatically")
        return false
    }

    private fun grantViaSecureSettings(context: Context): Boolean {
        return try {
            val existing = Settings.Secure.getString(context.contentResolver, KEY) ?: ""
            val comp = component(context)
            val updated = if (existing.isBlank()) comp else "$existing:$comp"
            Settings.Secure.putString(context.contentResolver, KEY, updated)
            isGranted(context)
        } catch (e: SecurityException) {
            // Нет WRITE_SECURE_SETTINGS — это ожидаемо для несистемного приложения.
            Log.d(TAG, "no WRITE_SECURE_SETTINGS, will try root")
            false
        } catch (e: Exception) {
            Log.w(TAG, "grantViaSecureSettings error", e)
            false
        }
    }

    private fun grantViaRoot(context: Context): Boolean {
        return try {
            val comp = component(context)
            // Сначала пробуем современную команду cmd notification, затем — settings put.
            val cmd = "cmd notification allow_listener $comp || " +
                "settings put secure $KEY \"\$(settings get secure $KEY):$comp\""
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", cmd))
            process.waitFor()
            isGranted(context)
        } catch (e: Exception) {
            Log.d(TAG, "root path unavailable: ${e.message}")
            false
        }
    }
}
