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

    // --- Legacy Compatibility Methods (can be gradually replaced in UI) ---
    // Mode
    fun isModeShortPressRemapped() = isButtonRemapped(BTN_MODE, false)
    fun setModeShortPressRemapped(v: Boolean) = setButtonRemapped(BTN_MODE, false, v)
    fun getModeShortPressAppPackage() = getButtonAppPackage(BTN_MODE, false)
    fun getModeShortPressAppName() = getButtonAppName(BTN_MODE, false)
    fun setModeShortPressApp(app: AppInfo?) = setButtonApp(BTN_MODE, false, app)
    fun isModeLongPressRemapped() = isButtonRemapped(BTN_MODE, true)
    fun setModeLongPressRemapped(v: Boolean) = setButtonRemapped(BTN_MODE, true, v)
    fun getModeLongPressAppPackage() = getButtonAppPackage(BTN_MODE, true)
    fun getModeLongPressAppName() = getButtonAppName(BTN_MODE, true)
    fun setModeLongPressApp(app: AppInfo?) = setButtonApp(BTN_MODE, true, app)
    fun getModeShortPressActionType() = getButtonActionType(BTN_MODE, false)
    fun setModeShortPressActionType(v: ButtonActionType) = setButtonActionType(BTN_MODE, false, v)
    fun getModeShortPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_MODE, false, true)
    fun getModeShortPressSplitLeftAppName() = getButtonSplitAppName(BTN_MODE, false, true)
    fun setModeShortPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_MODE, false, true, app)
    fun getModeShortPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_MODE, false, false)
    fun getModeShortPressSplitRightAppName() = getButtonSplitAppName(BTN_MODE, false, false)
    fun setModeShortPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_MODE, false, false, app)
    fun getModeLongPressActionType() = getButtonActionType(BTN_MODE, true)
    fun setModeLongPressActionType(v: ButtonActionType) = setButtonActionType(BTN_MODE, true, v)
    fun getModeLongPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_MODE, true, true)
    fun getModeLongPressSplitLeftAppName() = getButtonSplitAppName(BTN_MODE, true, true)
    fun setModeLongPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_MODE, true, true, app)
    fun getModeLongPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_MODE, true, false)
    fun getModeLongPressSplitRightAppName() = getButtonSplitAppName(BTN_MODE, true, false)
    fun setModeLongPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_MODE, true, false, app)

    // Home
    fun isHomeShortPressRemapped() = isButtonRemapped(BTN_HOME, false)
    fun setHomeShortPressRemapped(v: Boolean) = setButtonRemapped(BTN_HOME, false, v)
    fun getHomeShortPressAppPackage() = getButtonAppPackage(BTN_HOME, false)
    fun getHomeShortPressAppName() = getButtonAppName(BTN_HOME, false)
    fun setHomeShortPressApp(app: AppInfo?) = setButtonApp(BTN_HOME, false, app)
    fun isHomeLongPressRemapped() = isButtonRemapped(BTN_HOME, true)
    fun setHomeLongPressRemapped(v: Boolean) = setButtonRemapped(BTN_HOME, true, v)
    fun getHomeLongPressAppPackage() = getButtonAppPackage(BTN_HOME, true)
    fun getHomeLongPressAppName() = getButtonAppName(BTN_HOME, true)
    fun setHomeLongPressApp(app: AppInfo?) = setButtonApp(BTN_HOME, true, app)
    fun getHomeShortPressActionType() = getButtonActionType(BTN_HOME, false)
    fun setHomeShortPressActionType(v: ButtonActionType) = setButtonActionType(BTN_HOME, false, v)
    fun getHomeShortPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_HOME, false, true)
    fun getHomeShortPressSplitLeftAppName() = getButtonSplitAppName(BTN_HOME, false, true)
    fun setHomeShortPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_HOME, false, true, app)
    fun getHomeShortPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_HOME, false, false)
    fun getHomeShortPressSplitRightAppName() = getButtonSplitAppName(BTN_HOME, false, false)
    fun setHomeShortPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_HOME, false, false, app)
    fun getHomeLongPressActionType() = getButtonActionType(BTN_HOME, true)
    fun setHomeLongPressActionType(v: ButtonActionType) = setButtonActionType(BTN_HOME, true, v)
    fun getHomeLongPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_HOME, true, true)
    fun getHomeLongPressSplitLeftAppName() = getButtonSplitAppName(BTN_HOME, true, true)
    fun setHomeLongPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_HOME, true, true, app)
    fun getHomeLongPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_HOME, true, false)
    fun getHomeLongPressSplitRightAppName() = getButtonSplitAppName(BTN_HOME, true, false)
    fun setHomeLongPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_HOME, true, false, app)

    // Track Next
    fun isTrackNextShortPressRemapped() = isButtonRemapped(BTN_TRACK_NEXT, false)
    fun setTrackNextShortPressRemapped(v: Boolean) = setButtonRemapped(BTN_TRACK_NEXT, false, v)
    fun getTrackNextShortPressAppPackage() = getButtonAppPackage(BTN_TRACK_NEXT, false)
    fun getTrackNextShortPressAppName() = getButtonAppName(BTN_TRACK_NEXT, false)
    fun setTrackNextShortPressApp(app: AppInfo?) = setButtonApp(BTN_TRACK_NEXT, false, app)
    fun isTrackNextLongPressRemapped() = isButtonRemapped(BTN_TRACK_NEXT, true)
    fun setTrackNextLongPressRemapped(v: Boolean) = setButtonRemapped(BTN_TRACK_NEXT, true, v)
    fun getTrackNextLongPressAppPackage() = getButtonAppPackage(BTN_TRACK_NEXT, true)
    fun getTrackNextLongPressAppName() = getButtonAppName(BTN_TRACK_NEXT, true)
    fun setTrackNextLongPressApp(app: AppInfo?) = setButtonApp(BTN_TRACK_NEXT, true, app)
    fun getTrackNextShortPressActionType() = getButtonActionType(BTN_TRACK_NEXT, false)
    fun setTrackNextShortPressActionType(v: ButtonActionType) = setButtonActionType(BTN_TRACK_NEXT, false, v)
    fun getTrackNextShortPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_TRACK_NEXT, false, true)
    fun getTrackNextShortPressSplitLeftAppName() = getButtonSplitAppName(BTN_TRACK_NEXT, false, true)
    fun setTrackNextShortPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_TRACK_NEXT, false, true, app)
    fun getTrackNextShortPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_TRACK_NEXT, false, false)
    fun getTrackNextShortPressSplitRightAppName() = getButtonSplitAppName(BTN_TRACK_NEXT, false, false)
    fun setTrackNextShortPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_TRACK_NEXT, false, false, app)
    fun getTrackNextLongPressActionType() = getButtonActionType(BTN_TRACK_NEXT, true)
    fun setTrackNextLongPressActionType(v: ButtonActionType) = setButtonActionType(BTN_TRACK_NEXT, true, v)
    fun getTrackNextLongPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_TRACK_NEXT, true, true)
    fun getTrackNextLongPressSplitLeftAppName() = getButtonSplitAppName(BTN_TRACK_NEXT, true, true)
    fun setTrackNextLongPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_TRACK_NEXT, true, true, app)
    fun getTrackNextLongPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_TRACK_NEXT, true, false)
    fun getTrackNextLongPressSplitRightAppName() = getButtonSplitAppName(BTN_TRACK_NEXT, true, false)
    fun setTrackNextLongPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_TRACK_NEXT, true, false, app)

    // Track Prev
    fun isTrackPrevShortPressRemapped() = isButtonRemapped(BTN_TRACK_PREV, false)
    fun setTrackPrevShortPressRemapped(v: Boolean) = setButtonRemapped(BTN_TRACK_PREV, false, v)
    fun getTrackPrevShortPressAppPackage() = getButtonAppPackage(BTN_TRACK_PREV, false)
    fun getTrackPrevShortPressAppName() = getButtonAppName(BTN_TRACK_PREV, false)
    fun setTrackPrevShortPressApp(app: AppInfo?) = setButtonApp(BTN_TRACK_PREV, false, app)
    fun isTrackPrevLongPressRemapped() = isButtonRemapped(BTN_TRACK_PREV, true)
    fun setTrackPrevLongPressRemapped(v: Boolean) = setButtonRemapped(BTN_TRACK_PREV, true, v)
    fun getTrackPrevLongPressAppPackage() = getButtonAppPackage(BTN_TRACK_PREV, true)
    fun getTrackPrevLongPressAppName() = getButtonAppName(BTN_TRACK_PREV, true)
    fun setTrackPrevLongPressApp(app: AppInfo?) = setButtonApp(BTN_TRACK_PREV, true, app)
    fun getTrackPrevShortPressActionType() = getButtonActionType(BTN_TRACK_PREV, false)
    fun setTrackPrevShortPressActionType(v: ButtonActionType) = setButtonActionType(BTN_TRACK_PREV, false, v)
    fun getTrackPrevShortPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_TRACK_PREV, false, true)
    fun getTrackPrevShortPressSplitLeftAppName() = getButtonSplitAppName(BTN_TRACK_PREV, false, true)
    fun setTrackPrevShortPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_TRACK_PREV, false, true, app)
    fun getTrackPrevShortPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_TRACK_PREV, false, false)
    fun getTrackPrevShortPressSplitRightAppName() = getButtonSplitAppName(BTN_TRACK_PREV, false, false)
    fun setTrackPrevShortPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_TRACK_PREV, false, false, app)
    fun getTrackPrevLongPressActionType() = getButtonActionType(BTN_TRACK_PREV, true)
    fun setTrackPrevLongPressActionType(v: ButtonActionType) = setButtonActionType(BTN_TRACK_PREV, true, v)
    fun getTrackPrevLongPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_TRACK_PREV, true, true)
    fun getTrackPrevLongPressSplitLeftAppName() = getButtonSplitAppName(BTN_TRACK_PREV, true, true)
    fun setTrackPrevLongPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_TRACK_PREV, true, true, app)
    fun getTrackPrevLongPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_TRACK_PREV, true, false)
    fun getTrackPrevLongPressSplitRightAppName() = getButtonSplitAppName(BTN_TRACK_PREV, true, false)
    fun setTrackPrevLongPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_TRACK_PREV, true, false, app)

    // Mute
    fun isMuteShortPressRemapped() = isButtonRemapped(BTN_MUTE, false)
    fun setMuteShortPressRemapped(v: Boolean) = setButtonRemapped(BTN_MUTE, false, v)
    fun getMuteShortPressAppPackage() = getButtonAppPackage(BTN_MUTE, false)
    fun getMuteShortPressAppName() = getButtonAppName(BTN_MUTE, false)
    fun setMuteShortPressApp(app: AppInfo?) = setButtonApp(BTN_MUTE, false, app)
    fun isMuteLongPressRemapped() = isButtonRemapped(BTN_MUTE, true)
    fun setMuteLongPressRemapped(v: Boolean) = setButtonRemapped(BTN_MUTE, true, v)
    fun getMuteLongPressAppPackage() = getButtonAppPackage(BTN_MUTE, true)
    fun getMuteLongPressAppName() = getButtonAppName(BTN_MUTE, true)
    fun setMuteLongPressApp(app: AppInfo?) = setButtonApp(BTN_MUTE, true, app)
    fun getMuteShortPressActionType() = getButtonActionType(BTN_MUTE, false)
    fun setMuteShortPressActionType(v: ButtonActionType) = setButtonActionType(BTN_MUTE, false, v)
    fun getMuteShortPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_MUTE, false, true)
    fun getMuteShortPressSplitLeftAppName() = getButtonSplitAppName(BTN_MUTE, false, true)
    fun setMuteShortPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_MUTE, false, true, app)
    fun getMuteShortPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_MUTE, false, false)
    fun getMuteShortPressSplitRightAppName() = getButtonSplitAppName(BTN_MUTE, false, false)
    fun setMuteShortPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_MUTE, false, false, app)
    fun getMuteLongPressActionType() = getButtonActionType(BTN_MUTE, true)
    fun setMuteLongPressActionType(v: ButtonActionType) = setButtonActionType(BTN_MUTE, true, v)
    fun getMuteLongPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_MUTE, true, true)
    fun getMuteLongPressSplitLeftAppName() = getButtonSplitAppName(BTN_MUTE, true, true)
    fun setMuteLongPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_MUTE, true, true, app)
    fun getMuteLongPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_MUTE, true, false)
    fun getMuteLongPressSplitRightAppName() = getButtonSplitAppName(BTN_MUTE, true, false)
    fun setMuteLongPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_MUTE, true, false, app)

    // Call
    fun isCallShortPressRemapped() = isButtonRemapped(BTN_CALL, false)
    fun setCallShortPressRemapped(v: Boolean) = setButtonRemapped(BTN_CALL, false, v)
    fun getCallShortPressAppPackage() = getButtonAppPackage(BTN_CALL, false)
    fun getCallShortPressAppName() = getButtonAppName(BTN_CALL, false)
    fun setCallShortPressApp(app: AppInfo?) = setButtonApp(BTN_CALL, false, app)
    fun isCallLongPressRemapped() = isButtonRemapped(BTN_CALL, true)
    fun setCallLongPressRemapped(v: Boolean) = setButtonRemapped(BTN_CALL, true, v)
    fun getCallLongPressAppPackage() = getButtonAppPackage(BTN_CALL, true)
    fun getCallLongPressAppName() = getButtonAppName(BTN_CALL, true)
    fun setCallLongPressApp(app: AppInfo?) = setButtonApp(BTN_CALL, true, app)
    fun getCallShortPressActionType() = getButtonActionType(BTN_CALL, false)
    fun setCallShortPressActionType(v: ButtonActionType) = setButtonActionType(BTN_CALL, false, v)
    fun getCallShortPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_CALL, false, true)
    fun getCallShortPressSplitLeftAppName() = getButtonSplitAppName(BTN_CALL, false, true)
    fun setCallShortPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_CALL, false, true, app)
    fun getCallShortPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_CALL, false, false)
    fun getCallShortPressSplitRightAppName() = getButtonSplitAppName(BTN_CALL, false, false)
    fun setCallShortPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_CALL, false, false, app)
    fun getCallLongPressActionType() = getButtonActionType(BTN_CALL, true)
    fun setCallLongPressActionType(v: ButtonActionType) = setButtonActionType(BTN_CALL, true, v)
    fun getCallLongPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_CALL, true, true)
    fun getCallLongPressSplitLeftAppName() = getButtonSplitAppName(BTN_CALL, true, true)
    fun setCallLongPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_CALL, true, true, app)
    fun getCallLongPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_CALL, true, false)
    fun getCallLongPressSplitRightAppName() = getButtonSplitAppName(BTN_CALL, true, false)
    fun setCallLongPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_CALL, true, false, app)

    // Touch Mute
    fun isTouchMuteShortPressRemapped() = isButtonRemapped(BTN_TOUCH_MUTE, false)
    fun setTouchMuteShortPressRemapped(v: Boolean) = setButtonRemapped(BTN_TOUCH_MUTE, false, v)
    fun getTouchMuteShortPressAppPackage() = getButtonAppPackage(BTN_TOUCH_MUTE, false)
    fun getTouchMuteShortPressAppName() = getButtonAppName(BTN_TOUCH_MUTE, false)
    fun setTouchMuteShortPressApp(app: AppInfo?) = setButtonApp(BTN_TOUCH_MUTE, false, app)
    fun isTouchMuteLongPressRemapped() = isButtonRemapped(BTN_TOUCH_MUTE, true)
    fun setTouchMuteLongPressRemapped(v: Boolean) = setButtonRemapped(BTN_TOUCH_MUTE, true, v)
    fun getTouchMuteLongPressAppPackage() = getButtonAppPackage(BTN_TOUCH_MUTE, true)
    fun getTouchMuteLongPressAppName() = getButtonAppName(BTN_TOUCH_MUTE, true)
    fun setTouchMuteLongPressApp(app: AppInfo?) = setButtonApp(BTN_TOUCH_MUTE, true, app)
    fun getTouchMuteShortPressActionType() = getButtonActionType(BTN_TOUCH_MUTE, false)
    fun setTouchMuteShortPressActionType(v: ButtonActionType) = setButtonActionType(BTN_TOUCH_MUTE, false, v)
    fun getTouchMuteShortPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_TOUCH_MUTE, false, true)
    fun getTouchMuteShortPressSplitLeftAppName() = getButtonSplitAppName(BTN_TOUCH_MUTE, false, true)
    fun setTouchMuteShortPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_TOUCH_MUTE, false, true, app)
    fun getTouchMuteShortPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_TOUCH_MUTE, false, false)
    fun getTouchMuteShortPressSplitRightAppName() = getButtonSplitAppName(BTN_TOUCH_MUTE, false, false)
    fun setTouchMuteShortPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_TOUCH_MUTE, false, false, app)
    fun getTouchMuteLongPressActionType() = getButtonActionType(BTN_TOUCH_MUTE, true)
    fun setTouchMuteLongPressActionType(v: ButtonActionType) = setButtonActionType(BTN_TOUCH_MUTE, true, v)
    fun getTouchMuteLongPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_TOUCH_MUTE, true, true)
    fun getTouchMuteLongPressSplitLeftAppName() = getButtonSplitAppName(BTN_TOUCH_MUTE, true, true)
    fun setTouchMuteLongPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_TOUCH_MUTE, true, true, app)
    fun getTouchMuteLongPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_TOUCH_MUTE, true, false)
    fun getTouchMuteLongPressSplitRightAppName() = getButtonSplitAppName(BTN_TOUCH_MUTE, true, false)
    fun setTouchMuteLongPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_TOUCH_MUTE, true, false, app)

    // Touch Power
    fun isTouchPowerShortPressRemapped() = isButtonRemapped(BTN_TOUCH_POWER, false)
    fun setTouchPowerShortPressRemapped(v: Boolean) = setButtonRemapped(BTN_TOUCH_POWER, false, v)
    fun getTouchPowerShortPressAppPackage() = getButtonAppPackage(BTN_TOUCH_POWER, false)
    fun getTouchPowerShortPressAppName() = getButtonAppName(BTN_TOUCH_POWER, false)
    fun setTouchPowerShortPressApp(app: AppInfo?) = setButtonApp(BTN_TOUCH_POWER, false, app)
    fun isTouchPowerLongPressRemapped() = isButtonRemapped(BTN_TOUCH_POWER, true)
    fun setTouchPowerLongPressRemapped(v: Boolean) = setButtonRemapped(BTN_TOUCH_POWER, true, v)
    fun getTouchPowerLongPressAppPackage() = getButtonAppPackage(BTN_TOUCH_POWER, true)
    fun getTouchPowerLongPressAppName() = getButtonAppName(BTN_TOUCH_POWER, true)
    fun setTouchPowerLongPressApp(app: AppInfo?) = setButtonApp(BTN_TOUCH_POWER, true, app)
    fun getTouchPowerShortPressActionType() = getButtonActionType(BTN_TOUCH_POWER, false)
    fun setTouchPowerShortPressActionType(v: ButtonActionType) = setButtonActionType(BTN_TOUCH_POWER, false, v)
    fun getTouchPowerShortPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_TOUCH_POWER, false, true)
    fun getTouchPowerShortPressSplitLeftAppName() = getButtonSplitAppName(BTN_TOUCH_POWER, false, true)
    fun setTouchPowerShortPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_TOUCH_POWER, false, true, app)
    fun getTouchPowerShortPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_TOUCH_POWER, false, false)
    fun getTouchPowerShortPressSplitRightAppName() = getButtonSplitAppName(BTN_TOUCH_POWER, false, false)
    fun setTouchPowerShortPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_TOUCH_POWER, false, false, app)
    fun getTouchPowerLongPressActionType() = getButtonActionType(BTN_TOUCH_POWER, true)
    fun setTouchPowerLongPressActionType(v: ButtonActionType) = setButtonActionType(BTN_TOUCH_POWER, true, v)
    fun getTouchPowerLongPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_TOUCH_POWER, true, true)
    fun getTouchPowerLongPressSplitLeftAppName() = getButtonSplitAppName(BTN_TOUCH_POWER, true, true)
    fun setTouchPowerLongPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_TOUCH_POWER, true, true, app)
    fun getTouchPowerLongPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_TOUCH_POWER, true, false)
    fun getTouchPowerLongPressSplitRightAppName() = getButtonSplitAppName(BTN_TOUCH_POWER, true, false)
    fun setTouchPowerLongPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_TOUCH_POWER, true, false, app)

    // Media Play Pause
    fun isMediaPlayPauseShortPressRemapped() = isButtonRemapped(BTN_MEDIA_PLAY_PAUSE, false)
    fun setMediaPlayPauseShortPressRemapped(v: Boolean) = setButtonRemapped(BTN_MEDIA_PLAY_PAUSE, false, v)
    fun getMediaPlayPauseShortPressAppPackage() = getButtonAppPackage(BTN_MEDIA_PLAY_PAUSE, false)
    fun getMediaPlayPauseShortPressAppName() = getButtonAppName(BTN_MEDIA_PLAY_PAUSE, false)
    fun setMediaPlayPauseShortPressApp(app: AppInfo?) = setButtonApp(BTN_MEDIA_PLAY_PAUSE, false, app)
    fun isMediaPlayPauseLongPressRemapped() = isButtonRemapped(BTN_MEDIA_PLAY_PAUSE, true)
    fun setMediaPlayPauseLongPressRemapped(v: Boolean) = setButtonRemapped(BTN_MEDIA_PLAY_PAUSE, true, v)
    fun getMediaPlayPauseLongPressAppPackage() = getButtonAppPackage(BTN_MEDIA_PLAY_PAUSE, true)
    fun getMediaPlayPauseLongPressAppName() = getButtonAppName(BTN_MEDIA_PLAY_PAUSE, true)
    fun setMediaPlayPauseLongPressApp(app: AppInfo?) = setButtonApp(BTN_MEDIA_PLAY_PAUSE, true, app)
    fun getMediaPlayPauseShortPressActionType() = getButtonActionType(BTN_MEDIA_PLAY_PAUSE, false)
    fun setMediaPlayPauseShortPressActionType(v: ButtonActionType) = setButtonActionType(BTN_MEDIA_PLAY_PAUSE, false, v)
    fun getMediaPlayPauseShortPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_MEDIA_PLAY_PAUSE, false, true)
    fun getMediaPlayPauseShortPressSplitLeftAppName() = getButtonSplitAppName(BTN_MEDIA_PLAY_PAUSE, false, true)
    fun setMediaPlayPauseShortPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_MEDIA_PLAY_PAUSE, false, true, app)
    fun getMediaPlayPauseShortPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_MEDIA_PLAY_PAUSE, false, false)
    fun getMediaPlayPauseShortPressSplitRightAppName() = getButtonSplitAppName(BTN_MEDIA_PLAY_PAUSE, false, false)
    fun setMediaPlayPauseShortPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_MEDIA_PLAY_PAUSE, false, false, app)
    fun getMediaPlayPauseLongPressActionType() = getButtonActionType(BTN_MEDIA_PLAY_PAUSE, true)
    fun setMediaPlayPauseLongPressActionType(v: ButtonActionType) = setButtonActionType(BTN_MEDIA_PLAY_PAUSE, true, v)
    fun getMediaPlayPauseLongPressSplitLeftAppPackage() = getButtonSplitAppPackage(BTN_MEDIA_PLAY_PAUSE, true, true)
    fun getMediaPlayPauseLongPressSplitLeftAppName() = getButtonSplitAppName(BTN_MEDIA_PLAY_PAUSE, true, true)
    fun setMediaPlayPauseLongPressSplitLeftApp(app: AppInfo?) = setButtonSplitApp(BTN_MEDIA_PLAY_PAUSE, true, true, app)
    fun getMediaPlayPauseLongPressSplitRightAppPackage() = getButtonSplitAppPackage(BTN_MEDIA_PLAY_PAUSE, true, false)
    fun getMediaPlayPauseLongPressSplitRightAppName() = getButtonSplitAppName(BTN_MEDIA_PLAY_PAUSE, true, false)
    fun setMediaPlayPauseLongPressSplitRightApp(app: AppInfo?) = setButtonSplitApp(BTN_MEDIA_PLAY_PAUSE, true, false, app)

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
    }
}
