package ru.who.livansetting.data

object MigrationManager {
    private const val KEY_MIGRATION_V2 = "migration_v2_nothing_to_default"

    fun runIfNeeded(settingsManager: SettingsManager) {
        val prefs = settingsManager.getSharedPreferences()
        if (prefs.getBoolean(KEY_MIGRATION_V2, false)) return

        val buttons = listOf(
            SettingsManager.BTN_MODE, SettingsManager.BTN_HOME,
            SettingsManager.BTN_TRACK_NEXT, SettingsManager.BTN_TRACK_PREV,
            SettingsManager.BTN_MUTE, SettingsManager.BTN_CALL,
            SettingsManager.BTN_TOUCH_MUTE, SettingsManager.BTN_TOUCH_POWER,
            SettingsManager.BTN_MEDIA_PLAY_PAUSE
        )
        for (btn in buttons) {
            for (isLong in listOf(false, true)) {
                val actionType = settingsManager.getButtonActionType(btn, isLong)
                val remapped = settingsManager.isButtonRemapped(btn, isLong)
                if (actionType == ButtonActionType.NOTHING && !remapped) {
                    settingsManager.setButtonActionType(btn, isLong, ButtonActionType.DEFAULT_ACTION)
                }
            }
        }
        prefs.edit().putBoolean(KEY_MIGRATION_V2, true).apply()
    }
}
