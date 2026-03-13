package ru.who.livansetting

import android.content.Context
import android.content.SharedPreferences

/**
 * Manages persistent settings for the app using SharedPreferences.
 */
class SettingsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    /**
     * Получает SharedPreferences для использования в других компонентах
     */
    fun getSharedPreferences(): SharedPreferences = prefs

    // Настройки для короткого нажатия Mode
    fun isModeShortPressRemapped(): Boolean =
        prefs.getBoolean(KEY_MODE_SHORT_PRESS_REMAPPED, false)

    fun setModeShortPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_MODE_SHORT_PRESS_REMAPPED, remapped).apply()
    }

    fun getModeShortPressAppPackage(): String? =
        prefs.getString(KEY_MODE_SHORT_PRESS_APP_PACKAGE, null)

    fun getModeShortPressAppName(): String? =
        prefs.getString(KEY_MODE_SHORT_PRESS_APP_NAME, null)

    fun setModeShortPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MODE_SHORT_PRESS_APP_PACKAGE)
                .remove(KEY_MODE_SHORT_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MODE_SHORT_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_MODE_SHORT_PRESS_APP_NAME, app.appName)
            .apply()
    }

    // Настройки для длинного нажатия Mode
    fun isModeLongPressRemapped(): Boolean =
        prefs.getBoolean(KEY_MODE_LONG_PRESS_REMAPPED, false)

    fun setModeLongPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_MODE_LONG_PRESS_REMAPPED, remapped).apply()
    }

    fun getModeLongPressAppPackage(): String? =
        prefs.getString(KEY_MODE_LONG_PRESS_APP_PACKAGE, null)

    fun getModeLongPressAppName(): String? =
        prefs.getString(KEY_MODE_LONG_PRESS_APP_NAME, null)

    fun setModeLongPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MODE_LONG_PRESS_APP_PACKAGE)
                .remove(KEY_MODE_LONG_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MODE_LONG_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_MODE_LONG_PRESS_APP_NAME, app.appName)
            .apply()
    }

    // Настройки для короткого нажатия Home
    fun isHomeShortPressRemapped(): Boolean =
        prefs.getBoolean(KEY_HOME_SHORT_PRESS_REMAPPED, false)

    fun setHomeShortPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_HOME_SHORT_PRESS_REMAPPED, remapped).apply()
    }

    fun getHomeShortPressAppPackage(): String? =
        prefs.getString(KEY_HOME_SHORT_PRESS_APP_PACKAGE, null)

    fun getHomeShortPressAppName(): String? =
        prefs.getString(KEY_HOME_SHORT_PRESS_APP_NAME, null)

    fun setHomeShortPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_HOME_SHORT_PRESS_APP_PACKAGE)
                .remove(KEY_HOME_SHORT_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_HOME_SHORT_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_HOME_SHORT_PRESS_APP_NAME, app.appName)
            .apply()
    }

    // Настройки для длинного нажатия Home
    fun isHomeLongPressRemapped(): Boolean =
        prefs.getBoolean(KEY_HOME_LONG_PRESS_REMAPPED, false)

    fun setHomeLongPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_HOME_LONG_PRESS_REMAPPED, remapped).apply()
    }

    fun getHomeLongPressAppPackage(): String? =
        prefs.getString(KEY_HOME_LONG_PRESS_APP_PACKAGE, null)

    fun getHomeLongPressAppName(): String? =
        prefs.getString(KEY_HOME_LONG_PRESS_APP_NAME, null)

    fun setHomeLongPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_HOME_LONG_PRESS_APP_PACKAGE)
                .remove(KEY_HOME_LONG_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_HOME_LONG_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_HOME_LONG_PRESS_APP_NAME, app.appName)
            .apply()
    }

    // Методы для работы с типами действий Mode Short Press
    fun getModeShortPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_MODE_SHORT_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setModeShortPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_MODE_SHORT_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun getModeShortPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_MODE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getModeShortPressSplitLeftAppName(): String? =
        prefs.getString(KEY_MODE_SHORT_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setModeShortPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MODE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_MODE_SHORT_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MODE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_MODE_SHORT_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getModeShortPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_MODE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getModeShortPressSplitRightAppName(): String? =
        prefs.getString(KEY_MODE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setModeShortPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MODE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_MODE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MODE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_MODE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    // Методы для работы с типами действий Mode Long Press
    fun getModeLongPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_MODE_LONG_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setModeLongPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_MODE_LONG_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun getModeLongPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_MODE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getModeLongPressSplitLeftAppName(): String? =
        prefs.getString(KEY_MODE_LONG_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setModeLongPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MODE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_MODE_LONG_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MODE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_MODE_LONG_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getModeLongPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_MODE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getModeLongPressSplitRightAppName(): String? =
        prefs.getString(KEY_MODE_LONG_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setModeLongPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MODE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_MODE_LONG_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MODE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_MODE_LONG_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    // Методы для работы с типами действий Home Short Press
    fun getHomeShortPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_HOME_SHORT_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setHomeShortPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_HOME_SHORT_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun getHomeShortPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_HOME_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getHomeShortPressSplitLeftAppName(): String? =
        prefs.getString(KEY_HOME_SHORT_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setHomeShortPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_HOME_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_HOME_SHORT_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_HOME_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_HOME_SHORT_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getHomeShortPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_HOME_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getHomeShortPressSplitRightAppName(): String? =
        prefs.getString(KEY_HOME_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setHomeShortPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_HOME_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_HOME_SHORT_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_HOME_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_HOME_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    // Методы для работы с типами действий Home Long Press
    fun getHomeLongPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_HOME_LONG_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setHomeLongPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_HOME_LONG_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun getHomeLongPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_HOME_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getHomeLongPressSplitLeftAppName(): String? =
        prefs.getString(KEY_HOME_LONG_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setHomeLongPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_HOME_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_HOME_LONG_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_HOME_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_HOME_LONG_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getHomeLongPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_HOME_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getHomeLongPressSplitRightAppName(): String? =
        prefs.getString(KEY_HOME_LONG_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setHomeLongPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_HOME_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_HOME_LONG_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_HOME_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_HOME_LONG_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    // Track Next button split screen methods
    fun getTrackNextShortPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getTrackNextShortPressSplitLeftAppName(): String? =
        prefs.getString(KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setTrackNextShortPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getTrackNextShortPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getTrackNextShortPressSplitRightAppName(): String? =
        prefs.getString(KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setTrackNextShortPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    fun getTrackNextLongPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_TRACK_NEXT_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getTrackNextLongPressSplitLeftAppName(): String? =
        prefs.getString(KEY_TRACK_NEXT_LONG_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setTrackNextLongPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TRACK_NEXT_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_TRACK_NEXT_LONG_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TRACK_NEXT_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_TRACK_NEXT_LONG_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getTrackNextLongPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_TRACK_NEXT_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getTrackNextLongPressSplitRightAppName(): String? =
        prefs.getString(KEY_TRACK_NEXT_LONG_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setTrackNextLongPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TRACK_NEXT_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_TRACK_NEXT_LONG_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TRACK_NEXT_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_TRACK_NEXT_LONG_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    // Track Prev button split screen methods
    fun getTrackPrevShortPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_TRACK_PREV_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getTrackPrevShortPressSplitLeftAppName(): String? =
        prefs.getString(KEY_TRACK_PREV_SHORT_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setTrackPrevShortPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TRACK_PREV_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_TRACK_PREV_SHORT_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TRACK_PREV_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_TRACK_PREV_SHORT_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getTrackPrevShortPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_TRACK_PREV_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getTrackPrevShortPressSplitRightAppName(): String? =
        prefs.getString(KEY_TRACK_PREV_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setTrackPrevShortPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TRACK_PREV_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_TRACK_PREV_SHORT_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TRACK_PREV_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_TRACK_PREV_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    fun getTrackPrevLongPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_TRACK_PREV_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getTrackPrevLongPressSplitLeftAppName(): String? =
        prefs.getString(KEY_TRACK_PREV_LONG_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setTrackPrevLongPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TRACK_PREV_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_TRACK_PREV_LONG_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TRACK_PREV_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_TRACK_PREV_LONG_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getTrackPrevLongPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_TRACK_PREV_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getTrackPrevLongPressSplitRightAppName(): String? =
        prefs.getString(KEY_TRACK_PREV_LONG_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setTrackPrevLongPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TRACK_PREV_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_TRACK_PREV_LONG_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TRACK_PREV_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_TRACK_PREV_LONG_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    // Mute button split screen methods
    fun getMuteShortPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getMuteShortPressSplitLeftAppName(): String? =
        prefs.getString(KEY_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setMuteShortPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getMuteShortPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getMuteShortPressSplitRightAppName(): String? =
        prefs.getString(KEY_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setMuteShortPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    fun getMuteLongPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_MUTE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getMuteLongPressSplitLeftAppName(): String? =
        prefs.getString(KEY_MUTE_LONG_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setMuteLongPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MUTE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_MUTE_LONG_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MUTE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_MUTE_LONG_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getMuteLongPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getMuteLongPressSplitRightAppName(): String? =
        prefs.getString(KEY_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setMuteLongPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    // Call button split screen methods
    fun getCallShortPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_CALL_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getCallShortPressSplitLeftAppName(): String? =
        prefs.getString(KEY_CALL_SHORT_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setCallShortPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_CALL_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_CALL_SHORT_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_CALL_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_CALL_SHORT_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getCallShortPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_CALL_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getCallShortPressSplitRightAppName(): String? =
        prefs.getString(KEY_CALL_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setCallShortPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_CALL_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_CALL_SHORT_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_CALL_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_CALL_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    fun getCallLongPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_CALL_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getCallLongPressSplitLeftAppName(): String? =
        prefs.getString(KEY_CALL_LONG_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setCallLongPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_CALL_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_CALL_LONG_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_CALL_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_CALL_LONG_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getCallLongPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_CALL_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getCallLongPressSplitRightAppName(): String? =
        prefs.getString(KEY_CALL_LONG_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setCallLongPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_CALL_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_CALL_LONG_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_CALL_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_CALL_LONG_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    // Touch Mute button split screen methods
    fun getTouchMuteShortPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getTouchMuteShortPressSplitLeftAppName(): String? =
        prefs.getString(KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setTouchMuteShortPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getTouchMuteShortPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getTouchMuteShortPressSplitRightAppName(): String? =
        prefs.getString(KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setTouchMuteShortPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    fun getTouchMuteLongPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getTouchMuteLongPressSplitLeftAppName(): String? =
        prefs.getString(KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setTouchMuteLongPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getTouchMuteLongPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getTouchMuteLongPressSplitRightAppName(): String? =
        prefs.getString(KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setTouchMuteLongPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    // Touch Power button split screen methods
    fun getTouchPowerShortPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getTouchPowerShortPressSplitLeftAppName(): String? =
        prefs.getString(KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setTouchPowerShortPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getTouchPowerShortPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getTouchPowerShortPressSplitRightAppName(): String? =
        prefs.getString(KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setTouchPowerShortPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    fun getTouchPowerLongPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_TOUCH_POWER_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getTouchPowerLongPressSplitLeftAppName(): String? =
        prefs.getString(KEY_TOUCH_POWER_LONG_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setTouchPowerLongPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TOUCH_POWER_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_TOUCH_POWER_LONG_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TOUCH_POWER_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_TOUCH_POWER_LONG_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getTouchPowerLongPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_TOUCH_POWER_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getTouchPowerLongPressSplitRightAppName(): String? =
        prefs.getString(KEY_TOUCH_POWER_LONG_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setTouchPowerLongPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TOUCH_POWER_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_TOUCH_POWER_LONG_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TOUCH_POWER_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_TOUCH_POWER_LONG_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    // Track Next button methods
    fun getTrackNextShortPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_TRACK_NEXT_SHORT_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setTrackNextShortPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_TRACK_NEXT_SHORT_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun getTrackNextLongPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_TRACK_NEXT_LONG_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setTrackNextLongPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_TRACK_NEXT_LONG_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun isTrackNextShortPressRemapped(): Boolean =
        prefs.getBoolean(KEY_TRACK_NEXT_SHORT_PRESS_REMAPPED, false)

    fun setTrackNextShortPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_TRACK_NEXT_SHORT_PRESS_REMAPPED, remapped).apply()
    }

    fun getTrackNextShortPressAppPackage(): String? =
        prefs.getString(KEY_TRACK_NEXT_SHORT_PRESS_APP_PACKAGE, null)

    fun getTrackNextShortPressAppName(): String? =
        prefs.getString(KEY_TRACK_NEXT_SHORT_PRESS_APP_NAME, null)

    fun setTrackNextShortPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TRACK_NEXT_SHORT_PRESS_APP_PACKAGE)
                .remove(KEY_TRACK_NEXT_SHORT_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TRACK_NEXT_SHORT_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_TRACK_NEXT_SHORT_PRESS_APP_NAME, app.appName)
            .apply()
    }

    fun isTrackNextLongPressRemapped(): Boolean =
        prefs.getBoolean(KEY_TRACK_NEXT_LONG_PRESS_REMAPPED, false)

    fun setTrackNextLongPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_TRACK_NEXT_LONG_PRESS_REMAPPED, remapped).apply()
    }

    fun getTrackNextLongPressAppPackage(): String? =
        prefs.getString(KEY_TRACK_NEXT_LONG_PRESS_APP_PACKAGE, null)

    fun getTrackNextLongPressAppName(): String? =
        prefs.getString(KEY_TRACK_NEXT_LONG_PRESS_APP_NAME, null)

    fun setTrackNextLongPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TRACK_NEXT_LONG_PRESS_APP_PACKAGE)
                .remove(KEY_TRACK_NEXT_LONG_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TRACK_NEXT_LONG_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_TRACK_NEXT_LONG_PRESS_APP_NAME, app.appName)
            .apply()
    }

    // Track Prev button methods
    fun getTrackPrevShortPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_TRACK_PREV_SHORT_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setTrackPrevShortPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_TRACK_PREV_SHORT_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun getTrackPrevLongPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_TRACK_PREV_LONG_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setTrackPrevLongPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_TRACK_PREV_LONG_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun isTrackPrevShortPressRemapped(): Boolean =
        prefs.getBoolean(KEY_TRACK_PREV_SHORT_PRESS_REMAPPED, false)

    fun setTrackPrevShortPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_TRACK_PREV_SHORT_PRESS_REMAPPED, remapped).apply()
    }

    fun getTrackPrevShortPressAppPackage(): String? =
        prefs.getString(KEY_TRACK_PREV_SHORT_PRESS_APP_PACKAGE, null)

    fun getTrackPrevShortPressAppName(): String? =
        prefs.getString(KEY_TRACK_PREV_SHORT_PRESS_APP_NAME, null)

    fun setTrackPrevShortPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TRACK_PREV_SHORT_PRESS_APP_PACKAGE)
                .remove(KEY_TRACK_PREV_SHORT_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TRACK_PREV_SHORT_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_TRACK_PREV_SHORT_PRESS_APP_NAME, app.appName)
            .apply()
    }

    fun isTrackPrevLongPressRemapped(): Boolean =
        prefs.getBoolean(KEY_TRACK_PREV_LONG_PRESS_REMAPPED, false)

    fun setTrackPrevLongPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_TRACK_PREV_LONG_PRESS_REMAPPED, remapped).apply()
    }

    fun getTrackPrevLongPressAppPackage(): String? =
        prefs.getString(KEY_TRACK_PREV_LONG_PRESS_APP_PACKAGE, null)

    fun getTrackPrevLongPressAppName(): String? =
        prefs.getString(KEY_TRACK_PREV_LONG_PRESS_APP_NAME, null)

    fun setTrackPrevLongPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TRACK_PREV_LONG_PRESS_APP_PACKAGE)
                .remove(KEY_TRACK_PREV_LONG_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TRACK_PREV_LONG_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_TRACK_PREV_LONG_PRESS_APP_NAME, app.appName)
            .apply()
    }

    // Mute button methods
    fun getMuteShortPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_MUTE_SHORT_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setMuteShortPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_MUTE_SHORT_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun getMuteLongPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_MUTE_LONG_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setMuteLongPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_MUTE_LONG_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun isMuteShortPressRemapped(): Boolean =
        prefs.getBoolean(KEY_MUTE_SHORT_PRESS_REMAPPED, false)

    fun setMuteShortPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_MUTE_SHORT_PRESS_REMAPPED, remapped).apply()
    }

    fun getMuteShortPressAppPackage(): String? =
        prefs.getString(KEY_MUTE_SHORT_PRESS_APP_PACKAGE, null)

    fun getMuteShortPressAppName(): String? =
        prefs.getString(KEY_MUTE_SHORT_PRESS_APP_NAME, null)

    fun setMuteShortPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MUTE_SHORT_PRESS_APP_PACKAGE)
                .remove(KEY_MUTE_SHORT_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MUTE_SHORT_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_MUTE_SHORT_PRESS_APP_NAME, app.appName)
            .apply()
    }

    fun isMuteLongPressRemapped(): Boolean =
        prefs.getBoolean(KEY_MUTE_LONG_PRESS_REMAPPED, false)

    fun setMuteLongPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_MUTE_LONG_PRESS_REMAPPED, remapped).apply()
    }

    fun getMuteLongPressAppPackage(): String? =
        prefs.getString(KEY_MUTE_LONG_PRESS_APP_PACKAGE, null)

    fun getMuteLongPressAppName(): String? =
        prefs.getString(KEY_MUTE_LONG_PRESS_APP_NAME, null)

    fun setMuteLongPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MUTE_LONG_PRESS_APP_PACKAGE)
                .remove(KEY_MUTE_LONG_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MUTE_LONG_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_MUTE_LONG_PRESS_APP_NAME, app.appName)
            .apply()
    }

    // Call button methods
    fun getCallShortPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_CALL_SHORT_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setCallShortPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_CALL_SHORT_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun getCallLongPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_CALL_LONG_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setCallLongPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_CALL_LONG_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun isCallShortPressRemapped(): Boolean =
        prefs.getBoolean(KEY_CALL_SHORT_PRESS_REMAPPED, false)

    fun setCallShortPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_CALL_SHORT_PRESS_REMAPPED, remapped).apply()
    }

    fun getCallShortPressAppPackage(): String? =
        prefs.getString(KEY_CALL_SHORT_PRESS_APP_PACKAGE, null)

    fun getCallShortPressAppName(): String? =
        prefs.getString(KEY_CALL_SHORT_PRESS_APP_NAME, null)

    fun setCallShortPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_CALL_SHORT_PRESS_APP_PACKAGE)
                .remove(KEY_CALL_SHORT_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_CALL_SHORT_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_CALL_SHORT_PRESS_APP_NAME, app.appName)
            .apply()
    }

    fun isCallLongPressRemapped(): Boolean =
        prefs.getBoolean(KEY_CALL_LONG_PRESS_REMAPPED, false)

    fun setCallLongPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_CALL_LONG_PRESS_REMAPPED, remapped).apply()
    }

    fun getCallLongPressAppPackage(): String? =
        prefs.getString(KEY_CALL_LONG_PRESS_APP_PACKAGE, null)

    fun getCallLongPressAppName(): String? =
        prefs.getString(KEY_CALL_LONG_PRESS_APP_NAME, null)

    fun setCallLongPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_CALL_LONG_PRESS_APP_PACKAGE)
                .remove(KEY_CALL_LONG_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_CALL_LONG_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_CALL_LONG_PRESS_APP_NAME, app.appName)
            .apply()
    }

    // Touch Mute button methods
    fun getTouchMuteShortPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_TOUCH_MUTE_SHORT_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setTouchMuteShortPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_TOUCH_MUTE_SHORT_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun getTouchMuteLongPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_TOUCH_MUTE_LONG_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setTouchMuteLongPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_TOUCH_MUTE_LONG_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun isTouchMuteShortPressRemapped(): Boolean =
        prefs.getBoolean(KEY_TOUCH_MUTE_SHORT_PRESS_REMAPPED, false)

    fun setTouchMuteShortPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_TOUCH_MUTE_SHORT_PRESS_REMAPPED, remapped).apply()
    }

    fun getTouchMuteShortPressAppPackage(): String? =
        prefs.getString(KEY_TOUCH_MUTE_SHORT_PRESS_APP_PACKAGE, null)

    fun getTouchMuteShortPressAppName(): String? =
        prefs.getString(KEY_TOUCH_MUTE_SHORT_PRESS_APP_NAME, null)

    fun setTouchMuteShortPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TOUCH_MUTE_SHORT_PRESS_APP_PACKAGE)
                .remove(KEY_TOUCH_MUTE_SHORT_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TOUCH_MUTE_SHORT_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_TOUCH_MUTE_SHORT_PRESS_APP_NAME, app.appName)
            .apply()
    }

    fun isTouchMuteLongPressRemapped(): Boolean =
        prefs.getBoolean(KEY_TOUCH_MUTE_LONG_PRESS_REMAPPED, false)

    fun setTouchMuteLongPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_TOUCH_MUTE_LONG_PRESS_REMAPPED, remapped).apply()
    }

    fun getTouchMuteLongPressAppPackage(): String? =
        prefs.getString(KEY_TOUCH_MUTE_LONG_PRESS_APP_PACKAGE, null)

    fun getTouchMuteLongPressAppName(): String? =
        prefs.getString(KEY_TOUCH_MUTE_LONG_PRESS_APP_NAME, null)

    fun setTouchMuteLongPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TOUCH_MUTE_LONG_PRESS_APP_PACKAGE)
                .remove(KEY_TOUCH_MUTE_LONG_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TOUCH_MUTE_LONG_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_TOUCH_MUTE_LONG_PRESS_APP_NAME, app.appName)
            .apply()
    }

    // Touch Power button methods
    fun getTouchPowerShortPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_TOUCH_POWER_SHORT_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setTouchPowerShortPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_TOUCH_POWER_SHORT_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun getTouchPowerLongPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_TOUCH_POWER_LONG_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setTouchPowerLongPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_TOUCH_POWER_LONG_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun isTouchPowerShortPressRemapped(): Boolean =
        prefs.getBoolean(KEY_TOUCH_POWER_SHORT_PRESS_REMAPPED, false)

    fun setTouchPowerShortPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_TOUCH_POWER_SHORT_PRESS_REMAPPED, remapped).apply()
    }

    fun getTouchPowerShortPressAppPackage(): String? =
        prefs.getString(KEY_TOUCH_POWER_SHORT_PRESS_APP_PACKAGE, null)

    fun getTouchPowerShortPressAppName(): String? =
        prefs.getString(KEY_TOUCH_POWER_SHORT_PRESS_APP_NAME, null)

    fun setTouchPowerShortPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TOUCH_POWER_SHORT_PRESS_APP_PACKAGE)
                .remove(KEY_TOUCH_POWER_SHORT_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TOUCH_POWER_SHORT_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_TOUCH_POWER_SHORT_PRESS_APP_NAME, app.appName)
            .apply()
    }

    fun isTouchPowerLongPressRemapped(): Boolean =
        prefs.getBoolean(KEY_TOUCH_POWER_LONG_PRESS_REMAPPED, false)

    fun setTouchPowerLongPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_TOUCH_POWER_LONG_PRESS_REMAPPED, remapped).apply()
    }

    fun getTouchPowerLongPressAppPackage(): String? =
        prefs.getString(KEY_TOUCH_POWER_LONG_PRESS_APP_PACKAGE, null)

    fun getTouchPowerLongPressAppName(): String? =
        prefs.getString(KEY_TOUCH_POWER_LONG_PRESS_APP_NAME, null)

    fun setTouchPowerLongPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_TOUCH_POWER_LONG_PRESS_APP_PACKAGE)
                .remove(KEY_TOUCH_POWER_LONG_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_TOUCH_POWER_LONG_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_TOUCH_POWER_LONG_PRESS_APP_NAME, app.appName)
            .apply()
    }

    // Media Play Pause button methods
    fun getMediaPlayPauseShortPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setMediaPlayPauseShortPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun getMediaPlayPauseLongPressActionType(): ButtonActionType {
        val actionType = prefs.getString(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_ACTION_TYPE, ButtonActionType.NOTHING.name)
        return try {
            ButtonActionType.valueOf(actionType ?: ButtonActionType.NOTHING.name)
        } catch (e: IllegalArgumentException) {
            ButtonActionType.NOTHING
        }
    }

    fun setMediaPlayPauseLongPressActionType(actionType: ButtonActionType) {
        prefs.edit().putString(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_ACTION_TYPE, actionType.name).apply()
    }

    fun isMediaPlayPauseShortPressRemapped(): Boolean =
        prefs.getBoolean(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_REMAPPED, false)

    fun setMediaPlayPauseShortPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_REMAPPED, remapped).apply()
    }

    fun getMediaPlayPauseShortPressAppPackage(): String? =
        prefs.getString(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_APP_PACKAGE, null)

    fun getMediaPlayPauseShortPressAppName(): String? =
        prefs.getString(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_APP_NAME, null)

    fun setMediaPlayPauseShortPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_APP_PACKAGE)
                .remove(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_APP_NAME, app.appName)
            .apply()
    }

    fun isMediaPlayPauseLongPressRemapped(): Boolean =
        prefs.getBoolean(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_REMAPPED, false)

    fun setMediaPlayPauseLongPressRemapped(remapped: Boolean) {
        prefs.edit().putBoolean(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_REMAPPED, remapped).apply()
    }

    fun getMediaPlayPauseLongPressAppPackage(): String? =
        prefs.getString(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_APP_PACKAGE, null)

    fun getMediaPlayPauseLongPressAppName(): String? =
        prefs.getString(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_APP_NAME, null)

    fun setMediaPlayPauseLongPressApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_APP_PACKAGE)
                .remove(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_APP_PACKAGE, app.packageName)
            .putString(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_APP_NAME, app.appName)
            .apply()
    }

    // Split screen methods for Media Play Pause Short Press
    fun getMediaPlayPauseShortPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getMediaPlayPauseShortPressSplitLeftAppName(): String? =
        prefs.getString(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setMediaPlayPauseShortPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getMediaPlayPauseShortPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getMediaPlayPauseShortPressSplitRightAppName(): String? =
        prefs.getString(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setMediaPlayPauseShortPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    // Split screen methods for Media Play Pause Long Press
    fun getMediaPlayPauseLongPressSplitLeftAppPackage(): String? =
        prefs.getString(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, null)

    fun getMediaPlayPauseLongPressSplitLeftAppName(): String? =
        prefs.getString(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_LEFT_APP_NAME, null)

    fun setMediaPlayPauseLongPressSplitLeftApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE)
                .remove(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_LEFT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE, app.packageName)
            .putString(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_LEFT_APP_NAME, app.appName)
            .apply()
    }

    fun getMediaPlayPauseLongPressSplitRightAppPackage(): String? =
        prefs.getString(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, null)

    fun getMediaPlayPauseLongPressSplitRightAppName(): String? =
        prefs.getString(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_RIGHT_APP_NAME, null)

    fun setMediaPlayPauseLongPressSplitRightApp(app: AppInfo?) {
        if (app == null) {
            prefs.edit()
                .remove(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE)
                .remove(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_RIGHT_APP_NAME)
                .apply()
            return
        }
        prefs.edit()
            .putString(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE, app.packageName)
            .putString(KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_RIGHT_APP_NAME, app.appName)
            .apply()
    }

    // Welcome Light settings
    fun isWelcomeLightEnabled(): Boolean =
        prefs.getBoolean(KEY_WELCOME_LIGHT_ENABLED, true)

    fun setWelcomeLightEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_WELCOME_LIGHT_ENABLED, enabled).apply()
    }
    
    // Drive mode settings
    fun getDriveModeSelection(): DriveModeSelection {
        val mode = prefs.getString(KEY_DRIVE_MODE_SELECTION, DriveModeSelection.NONE.name)
        return try {
            DriveModeSelection.valueOf(mode ?: DriveModeSelection.NONE.name)
        } catch (e: IllegalArgumentException) {
            DriveModeSelection.NONE
        }
    }

    fun setDriveModeSelection(mode: DriveModeSelection) {
        prefs.edit().putString(KEY_DRIVE_MODE_SELECTION, mode.name).apply()
    }

    companion object {
        private const val PREFS_NAME = "ru.who.livansetting.prefs"
        private const val KEY_SERVICE_ENABLED = "service_enabled"
        private const val KEY_MODE_APP_PACKAGE = "mode_app_package"
        private const val KEY_MODE_APP_NAME = "mode_app_name"
        private const val KEY_HOME_APP_PACKAGE = "home_app_package"
        private const val KEY_HOME_APP_NAME = "home_app_name"
        private const val KEY_AUTO_START_ENABLED = "auto_start_enabled"
        
        // Mode button settings
        private const val KEY_MODE_SHORT_PRESS_REMAPPED = "mode_short_press_remapped"
        private const val KEY_MODE_SHORT_PRESS_APP_PACKAGE = "mode_short_press_app_package"
        private const val KEY_MODE_SHORT_PRESS_APP_NAME = "mode_short_press_app_name"
        private const val KEY_MODE_LONG_PRESS_REMAPPED = "mode_long_press_remapped"
        private const val KEY_MODE_LONG_PRESS_APP_PACKAGE = "mode_long_press_app_package"
        private const val KEY_MODE_LONG_PRESS_APP_NAME = "mode_long_press_app_name"
        
        // Home button settings
        private const val KEY_HOME_SHORT_PRESS_REMAPPED = "home_short_press_remapped"
        private const val KEY_HOME_SHORT_PRESS_APP_PACKAGE = "home_short_press_app_package"
        private const val KEY_HOME_SHORT_PRESS_APP_NAME = "home_short_press_app_name"
        private const val KEY_HOME_LONG_PRESS_REMAPPED = "home_long_press_remapped"
        private const val KEY_HOME_LONG_PRESS_APP_PACKAGE = "home_long_press_app_package"
        private const val KEY_HOME_LONG_PRESS_APP_NAME = "home_long_press_app_name"
        
        // Action type keys
        private const val KEY_MODE_SHORT_PRESS_ACTION_TYPE = "mode_short_press_action_type"
        private const val KEY_MODE_LONG_PRESS_ACTION_TYPE = "mode_long_press_action_type"
        private const val KEY_HOME_SHORT_PRESS_ACTION_TYPE = "home_short_press_action_type"
        private const val KEY_HOME_LONG_PRESS_ACTION_TYPE = "home_long_press_action_type"
        private const val KEY_TRACK_NEXT_SHORT_PRESS_ACTION_TYPE = "track_next_short_press_action_type"
        private const val KEY_TRACK_NEXT_LONG_PRESS_ACTION_TYPE = "track_next_long_press_action_type"
        private const val KEY_TRACK_PREV_SHORT_PRESS_ACTION_TYPE = "track_prev_short_press_action_type"
        private const val KEY_TRACK_PREV_LONG_PRESS_ACTION_TYPE = "track_prev_long_press_action_type"
        private const val KEY_MUTE_SHORT_PRESS_ACTION_TYPE = "mute_short_press_action_type"
        private const val KEY_MUTE_LONG_PRESS_ACTION_TYPE = "mute_long_press_action_type"
        private const val KEY_CALL_SHORT_PRESS_ACTION_TYPE = "call_short_press_action_type"
        private const val KEY_CALL_LONG_PRESS_ACTION_TYPE = "call_long_press_action_type"
        private const val KEY_TOUCH_MUTE_SHORT_PRESS_ACTION_TYPE = "touch_mute_short_press_action_type"
        private const val KEY_TOUCH_MUTE_LONG_PRESS_ACTION_TYPE = "touch_mute_long_press_action_type"
        private const val KEY_TOUCH_POWER_SHORT_PRESS_ACTION_TYPE = "touch_power_short_press_action_type"
        private const val KEY_TOUCH_POWER_LONG_PRESS_ACTION_TYPE = "touch_power_long_press_action_type"
        
        // Split screen app keys for Mode Short Press
        private const val KEY_MODE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE = "mode_short_press_split_left_app_package"
        private const val KEY_MODE_SHORT_PRESS_SPLIT_LEFT_APP_NAME = "mode_short_press_split_left_app_name"
        private const val KEY_MODE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE = "mode_short_press_split_right_app_package"
        private const val KEY_MODE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME = "mode_short_press_split_right_app_name"
        
        // Split screen app keys for Mode Long Press
        private const val KEY_MODE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE = "mode_long_press_split_left_app_package"
        private const val KEY_MODE_LONG_PRESS_SPLIT_LEFT_APP_NAME = "mode_long_press_split_left_app_name"
        private const val KEY_MODE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE = "mode_long_press_split_right_app_package"
        private const val KEY_MODE_LONG_PRESS_SPLIT_RIGHT_APP_NAME = "mode_long_press_split_right_app_name"
        
        // Split screen app keys for Home Short Press
        private const val KEY_HOME_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE = "home_short_press_split_left_app_package"
        private const val KEY_HOME_SHORT_PRESS_SPLIT_LEFT_APP_NAME = "home_short_press_split_left_app_name"
        private const val KEY_HOME_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE = "home_short_press_split_right_app_package"
        private const val KEY_HOME_SHORT_PRESS_SPLIT_RIGHT_APP_NAME = "home_short_press_split_right_app_name"
        
        // Split screen app keys for Home Long Press
        private const val KEY_HOME_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE = "home_long_press_split_left_app_package"
        private const val KEY_HOME_LONG_PRESS_SPLIT_LEFT_APP_NAME = "home_long_press_split_left_app_name"
        private const val KEY_HOME_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE = "home_long_press_split_right_app_package"
        private const val KEY_HOME_LONG_PRESS_SPLIT_RIGHT_APP_NAME = "home_long_press_split_right_app_name"
        
        // Track Next button settings
        private const val KEY_TRACK_NEXT_SHORT_PRESS_REMAPPED = "track_next_short_press_remapped"
        private const val KEY_TRACK_NEXT_SHORT_PRESS_APP_PACKAGE = "track_next_short_press_app_package"
        private const val KEY_TRACK_NEXT_SHORT_PRESS_APP_NAME = "track_next_short_press_app_name"
        private const val KEY_TRACK_NEXT_LONG_PRESS_REMAPPED = "track_next_long_press_remapped"
        private const val KEY_TRACK_NEXT_LONG_PRESS_APP_PACKAGE = "track_next_long_press_app_package"
        private const val KEY_TRACK_NEXT_LONG_PRESS_APP_NAME = "track_next_long_press_app_name"
        
        // Track Prev button settings
        private const val KEY_TRACK_PREV_SHORT_PRESS_REMAPPED = "track_prev_short_press_remapped"
        private const val KEY_TRACK_PREV_SHORT_PRESS_APP_PACKAGE = "track_prev_short_press_app_package"
        private const val KEY_TRACK_PREV_SHORT_PRESS_APP_NAME = "track_prev_short_press_app_name"
        private const val KEY_TRACK_PREV_LONG_PRESS_REMAPPED = "track_prev_long_press_remapped"
        private const val KEY_TRACK_PREV_LONG_PRESS_APP_PACKAGE = "track_prev_long_press_app_package"
        private const val KEY_TRACK_PREV_LONG_PRESS_APP_NAME = "track_prev_long_press_app_name"
        
        // Mute button settings
        private const val KEY_MUTE_SHORT_PRESS_REMAPPED = "mute_short_press_remapped"
        private const val KEY_MUTE_SHORT_PRESS_APP_PACKAGE = "mute_short_press_app_package"
        private const val KEY_MUTE_SHORT_PRESS_APP_NAME = "mute_short_press_app_name"
        private const val KEY_MUTE_LONG_PRESS_REMAPPED = "mute_long_press_remapped"
        private const val KEY_MUTE_LONG_PRESS_APP_PACKAGE = "mute_long_press_app_package"
        private const val KEY_MUTE_LONG_PRESS_APP_NAME = "mute_long_press_app_name"
        
        // Call button settings
        private const val KEY_CALL_SHORT_PRESS_REMAPPED = "call_short_press_remapped"
        private const val KEY_CALL_SHORT_PRESS_APP_PACKAGE = "call_short_press_app_package"
        private const val KEY_CALL_SHORT_PRESS_APP_NAME = "call_short_press_app_name"
        private const val KEY_CALL_LONG_PRESS_REMAPPED = "call_long_press_remapped"
        private const val KEY_CALL_LONG_PRESS_APP_PACKAGE = "call_long_press_app_package"
        private const val KEY_CALL_LONG_PRESS_APP_NAME = "call_long_press_app_name"
        
        // Touch Mute button settings
        private const val KEY_TOUCH_MUTE_SHORT_PRESS_REMAPPED = "touch_mute_short_press_remapped"
        private const val KEY_TOUCH_MUTE_SHORT_PRESS_APP_PACKAGE = "touch_mute_short_press_app_package"
        private const val KEY_TOUCH_MUTE_SHORT_PRESS_APP_NAME = "touch_mute_short_press_app_name"
        private const val KEY_TOUCH_MUTE_LONG_PRESS_REMAPPED = "touch_mute_long_press_remapped"
        private const val KEY_TOUCH_MUTE_LONG_PRESS_APP_PACKAGE = "touch_mute_long_press_app_package"
        private const val KEY_TOUCH_MUTE_LONG_PRESS_APP_NAME = "touch_mute_long_press_app_name"
        
        // Touch Power button settings
        private const val KEY_TOUCH_POWER_SHORT_PRESS_REMAPPED = "touch_power_short_press_remapped"
        private const val KEY_TOUCH_POWER_SHORT_PRESS_APP_PACKAGE = "touch_power_short_press_app_package"
        private const val KEY_TOUCH_POWER_SHORT_PRESS_APP_NAME = "touch_power_short_press_app_name"
        private const val KEY_TOUCH_POWER_LONG_PRESS_REMAPPED = "touch_power_long_press_remapped"
        private const val KEY_TOUCH_POWER_LONG_PRESS_APP_PACKAGE = "touch_power_long_press_app_package"
        private const val KEY_TOUCH_POWER_LONG_PRESS_APP_NAME = "touch_power_long_press_app_name"
        
        // Split screen app keys for Track Next Short Press
        private const val KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE = "track_next_short_press_split_left_app_package"
        private const val KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_LEFT_APP_NAME = "track_next_short_press_split_left_app_name"
        private const val KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE = "track_next_short_press_split_right_app_package"
        private const val KEY_TRACK_NEXT_SHORT_PRESS_SPLIT_RIGHT_APP_NAME = "track_next_short_press_split_right_app_name"
        
        // Split screen app keys for Track Next Long Press
        private const val KEY_TRACK_NEXT_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE = "track_next_long_press_split_left_app_package"
        private const val KEY_TRACK_NEXT_LONG_PRESS_SPLIT_LEFT_APP_NAME = "track_next_long_press_split_left_app_name"
        private const val KEY_TRACK_NEXT_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE = "track_next_long_press_split_right_app_package"
        private const val KEY_TRACK_NEXT_LONG_PRESS_SPLIT_RIGHT_APP_NAME = "track_next_long_press_split_right_app_name"
        
        // Split screen app keys for Track Prev Short Press
        private const val KEY_TRACK_PREV_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE = "track_prev_short_press_split_left_app_package"
        private const val KEY_TRACK_PREV_SHORT_PRESS_SPLIT_LEFT_APP_NAME = "track_prev_short_press_split_left_app_name"
        private const val KEY_TRACK_PREV_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE = "track_prev_short_press_split_right_app_package"
        private const val KEY_TRACK_PREV_SHORT_PRESS_SPLIT_RIGHT_APP_NAME = "track_prev_short_press_split_right_app_name"
        
        // Split screen app keys for Track Prev Long Press
        private const val KEY_TRACK_PREV_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE = "track_prev_long_press_split_left_app_package"
        private const val KEY_TRACK_PREV_LONG_PRESS_SPLIT_LEFT_APP_NAME = "track_prev_long_press_split_left_app_name"
        private const val KEY_TRACK_PREV_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE = "track_prev_long_press_split_right_app_package"
        private const val KEY_TRACK_PREV_LONG_PRESS_SPLIT_RIGHT_APP_NAME = "track_prev_long_press_split_right_app_name"
        
        // Split screen app keys for Mute Short Press
        private const val KEY_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE = "mute_short_press_split_left_app_package"
        private const val KEY_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_NAME = "mute_short_press_split_left_app_name"
        private const val KEY_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE = "mute_short_press_split_right_app_package"
        private const val KEY_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME = "mute_short_press_split_right_app_name"
        
        // Split screen app keys for Mute Long Press
        private const val KEY_MUTE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE = "mute_long_press_split_left_app_package"
        private const val KEY_MUTE_LONG_PRESS_SPLIT_LEFT_APP_NAME = "mute_long_press_split_left_app_name"
        private const val KEY_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE = "mute_long_press_split_right_app_package"
        private const val KEY_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_NAME = "mute_long_press_split_right_app_name"
        
        // Split screen app keys for Call Short Press
        private const val KEY_CALL_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE = "call_short_press_split_left_app_package"
        private const val KEY_CALL_SHORT_PRESS_SPLIT_LEFT_APP_NAME = "call_short_press_split_left_app_name"
        private const val KEY_CALL_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE = "call_short_press_split_right_app_package"
        private const val KEY_CALL_SHORT_PRESS_SPLIT_RIGHT_APP_NAME = "call_short_press_split_right_app_name"
        
        // Split screen app keys for Call Long Press
        private const val KEY_CALL_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE = "call_long_press_split_left_app_package"
        private const val KEY_CALL_LONG_PRESS_SPLIT_LEFT_APP_NAME = "call_long_press_split_left_app_name"
        private const val KEY_CALL_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE = "call_long_press_split_right_app_package"
        private const val KEY_CALL_LONG_PRESS_SPLIT_RIGHT_APP_NAME = "call_long_press_split_right_app_name"
        
        // Split screen app keys for Touch Mute Short Press
        private const val KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE = "touch_mute_short_press_split_left_app_package"
        private const val KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_LEFT_APP_NAME = "touch_mute_short_press_split_left_app_name"
        private const val KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE = "touch_mute_short_press_split_right_app_package"
        private const val KEY_TOUCH_MUTE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME = "touch_mute_short_press_split_right_app_name"
        
        // Split screen app keys for Touch Mute Long Press
        private const val KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE = "touch_mute_long_press_split_left_app_package"
        private const val KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_LEFT_APP_NAME = "touch_mute_long_press_split_left_app_name"
        private const val KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE = "touch_mute_long_press_split_right_app_package"
        private const val KEY_TOUCH_MUTE_LONG_PRESS_SPLIT_RIGHT_APP_NAME = "touch_mute_long_press_split_right_app_name"
        
        // Split screen app keys for Touch Power Short Press
        private const val KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE = "touch_power_short_press_split_left_app_package"
        private const val KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_LEFT_APP_NAME = "touch_power_short_press_split_left_app_name"
        private const val KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE = "touch_power_short_press_split_right_app_package"
        private const val KEY_TOUCH_POWER_SHORT_PRESS_SPLIT_RIGHT_APP_NAME = "touch_power_short_press_split_right_app_name"
        
        // Split screen app keys for Touch Power Long Press
        private const val KEY_TOUCH_POWER_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE = "touch_power_long_press_split_left_app_package"
        private const val KEY_TOUCH_POWER_LONG_PRESS_SPLIT_LEFT_APP_NAME = "touch_power_long_press_split_left_app_name"
        private const val KEY_TOUCH_POWER_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE = "touch_power_long_press_split_right_app_package"
        private const val KEY_TOUCH_POWER_LONG_PRESS_SPLIT_RIGHT_APP_NAME = "touch_power_long_press_split_right_app_name"
        
        // Media Play Pause button settings
        private const val KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_REMAPPED = "media_play_pause_short_press_remapped"
        private const val KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_APP_PACKAGE = "media_play_pause_short_press_app_package"
        private const val KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_APP_NAME = "media_play_pause_short_press_app_name"
        private const val KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_REMAPPED = "media_play_pause_long_press_remapped"
        private const val KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_APP_PACKAGE = "media_play_pause_long_press_app_package"
        private const val KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_APP_NAME = "media_play_pause_long_press_app_name"
        private const val KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_ACTION_TYPE = "media_play_pause_short_press_action_type"
        private const val KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_ACTION_TYPE = "media_play_pause_long_press_action_type"
        
        // Split screen app keys for Media Play Pause Short Press
        private const val KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_LEFT_APP_PACKAGE = "media_play_pause_short_press_split_left_app_package"
        private const val KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_LEFT_APP_NAME = "media_play_pause_short_press_split_left_app_name"
        private const val KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_RIGHT_APP_PACKAGE = "media_play_pause_short_press_split_right_app_package"
        private const val KEY_MEDIA_PLAY_PAUSE_SHORT_PRESS_SPLIT_RIGHT_APP_NAME = "media_play_pause_short_press_split_right_app_name"
        
        // Split screen app keys for Media Play Pause Long Press
        private const val KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_LEFT_APP_PACKAGE = "media_play_pause_long_press_split_left_app_package"
        private const val KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_LEFT_APP_NAME = "media_play_pause_long_press_split_left_app_name"
        private const val KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_RIGHT_APP_PACKAGE = "media_play_pause_long_press_split_right_app_package"
        private const val KEY_MEDIA_PLAY_PAUSE_LONG_PRESS_SPLIT_RIGHT_APP_NAME = "media_play_pause_long_press_split_right_app_name"
        
        // Welcome Light settings
        private const val KEY_WELCOME_LIGHT_ENABLED = "welcome_light_enabled"
        
        // Drive mode settings
        private const val KEY_DRIVE_MODE_SELECTION = "drive_mode_selection"
    }
}


