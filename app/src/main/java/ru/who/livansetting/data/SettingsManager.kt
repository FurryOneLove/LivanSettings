package ru.who.livansetting.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages persistent settings for the app using SharedPreferences.
 */
class SettingsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getSharedPreferences(): SharedPreferences = prefs

    // --- Generalized Methods ---

    private fun getFullKey(button: String, isLongPress: Boolean, feature: String): String {
        val pressType = if (isLongPress) "long" else "short"
        return "${button}_${pressType}_press_$feature"
    }

    fun isButtonRemapped(button: String, isLongPress: Boolean): Boolean =
        prefs.getBoolean(getFullKey(button, isLongPress, "remapped"), false)

    fun setButtonRemapped(button: String, isLongPress: Boolean, remapped: Boolean) {
        prefs.edit().putBoolean(getFullKey(button, isLongPress, "remapped"), remapped).apply()
    }

    fun getButtonAppPackage(button: String, isLongPress: Boolean): String? =
        prefs.getString(getFullKey(button, isLongPress, "app_package"), null)

    fun getButtonAppName(button: String, isLongPress: Boolean): String? =
        prefs.getString(getFullKey(button, isLongPress, "app_name"), null)

    fun setButtonApp(button: String, isLongPress: Boolean, app: AppInfo?) {
        val keyPkg = getFullKey(button, isLongPress, "app_package")
        val keyName = getFullKey(button, isLongPress, "app_name")
        if (app == null) {
            prefs.edit().remove(keyPkg).remove(keyName).apply()
        } else {
            prefs.edit().putString(keyPkg, app.packageName).putString(keyName, app.appName).apply()
        }
    }

    fun getButtonActionType(button: String, isLongPress: Boolean): ButtonActionType {
        val actionType = prefs.getString(getFullKey(button, isLongPress, "action_type"), ButtonActionType.DEFAULT_ACTION.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.DEFAULT_ACTION.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.DEFAULT_ACTION
        }
    }

    fun setButtonActionType(button: String, isLongPress: Boolean, actionType: ButtonActionType) {
        prefs.edit().putString(getFullKey(button, isLongPress, "action_type"), actionType.name).apply()
    }

    fun getButtonSplitAppPackage(button: String, isLongPress: Boolean, isLeft: Boolean): String? {
        val side = if (isLeft) "left" else "right"
        return prefs.getString(getFullKey(button, isLongPress, "split_${side}_app_package"), null)
    }

    fun getButtonSplitAppName(button: String, isLongPress: Boolean, isLeft: Boolean): String? {
        val side = if (isLeft) "left" else "right"
        return prefs.getString(getFullKey(button, isLongPress, "split_${side}_app_name"), null)
    }

    fun setButtonSplitApp(button: String, isLongPress: Boolean, isLeft: Boolean, app: AppInfo?) {
        val side = if (isLeft) "left" else "right"
        val keyPkg = getFullKey(button, isLongPress, "split_${side}_app_package")
        val keyName = getFullKey(button, isLongPress, "split_${side}_app_name")
        if (app == null) {
            prefs.edit().remove(keyPkg).remove(keyName).apply()
        } else {
            prefs.edit().putString(keyPkg, app.packageName).putString(keyName, app.appName).apply()
        }
    }

    fun getButtonIntentAction(button: String, isLongPress: Boolean): String? =
        prefs.getString(getFullKey(button, isLongPress, "intent_action"), null)

    fun setButtonIntentAction(button: String, isLongPress: Boolean, action: String?) {
        val key = getFullKey(button, isLongPress, "intent_action")
        if (action.isNullOrEmpty()) {
            prefs.edit().remove(key).apply()
        } else {
            prefs.edit().putString(key, action).apply()
        }
    }

    // Other settings
    fun isWelcomeLightEnabled(): Boolean = prefs.getBoolean(KEY_WELCOME_LIGHT_ENABLED, true)
    fun setWelcomeLightEnabled(enabled: Boolean) = prefs.edit().putBoolean(KEY_WELCOME_LIGHT_ENABLED, enabled).apply()

    fun getDriveModeSelection(): DriveModeSelection {
        val mode = prefs.getString(KEY_DRIVE_MODE_SELECTION, DriveModeSelection.NONE.name)
        return try {
            DriveModeSelection.valueOf(mode ?: DriveModeSelection.NONE.name)
        } catch (e: IllegalArgumentException) {
            DriveModeSelection.NONE
        }
    }

    fun setDriveModeSelection(mode: DriveModeSelection) = prefs.edit().putString(KEY_DRIVE_MODE_SELECTION, mode.name).apply()

    companion object {
        private const val PREFS_NAME = "ru.who.livansetting.prefs"

        // Button Prefixes
        const val BTN_MODE = "mode"
        const val BTN_HOME = "home"
        const val BTN_TRACK_NEXT = "track_next"
        const val BTN_TRACK_PREV = "track_prev"
        const val BTN_MUTE = "mute"
        const val BTN_CALL = "call"
        const val BTN_TOUCH_MUTE = "touch_mute"
        const val BTN_TOUCH_POWER = "touch_power"
        const val BTN_MEDIA_PLAY_PAUSE = "media_play_pause"

        private const val KEY_WELCOME_LIGHT_ENABLED = "welcome_light_enabled"
        private const val KEY_DRIVE_MODE_SELECTION = "drive_mode_selection"
        private const val KEY_SCREENSAVER_ACTIVE = "screensaver_active"
    }

    fun isScreensaverActive(): Boolean =
        prefs.getBoolean(KEY_SCREENSAVER_ACTIVE, false)

    fun setScreensaverActive(active: Boolean) =
        prefs.edit().putBoolean(KEY_SCREENSAVER_ACTIVE, active).apply()
}
