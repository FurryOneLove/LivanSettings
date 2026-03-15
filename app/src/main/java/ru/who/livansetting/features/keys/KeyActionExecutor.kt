package ru.who.livansetting.features.keys

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log
import com.ecarx.xui.adaptapi.input.KeyCode
import ru.who.livansetting.data.ButtonActionType
import ru.who.livansetting.data.SettingsManager
import ru.who.livansetting.features.auto.DrlManager
import ru.who.livansetting.features.auto.SeatHeatingManager
import ru.who.livansetting.utils.CarMediaController
import ru.who.livansetting.utils.MediaActionType
import ru.who.livansetting.utils.VolumeController
import ru.who.livansetting.utils.SplitScreenLauncher

/**
 * Исполнитель действий при нажатии клавиш.
 */
class KeyActionExecutor(private val context: Context) {
    
    companion object {
        private const val TAG = "KeyActionExecutor"
        
        private const val ECARX_ACTION_POWER = "ecarx.intent.action.ECARX_KEY_POWER_EVENT"
        private const val ECARX_ACTION_RCALL = "ecarx.intent.action.ECARX_KEY_RCALL_EVENT"
        private const val ECARX_ACTION_RSRC = "ecarx.intent.action.ECARX_KEY_RSRC_EVENT"
        private const val ECARX_ACTION_VR_EXIT = "ecarx.action.ECARX_VR_EXIT"
        private const val ECARX_ACTION_QUIT_FULLSCREEN = "ecarx.intent.action.QUIT_FULLSCREEN_VIEW"
        
        private const val SCREENSAVER_ACTION = "android.intent.action.SCREENSAVER"
        private const val SCREENSAVER_CATEGORY = "android.intent.category.SCREENSAVER"
        private const val SCREENSAVER_PACKAGE = "com.ecarx.screensaver"
        private const val SCREENSAVER_SERVICE = "com.ecarx.screensaver.ScreensaverService"
    }
    
    private val settingsManager = SettingsManager(context)
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val carMediaController = CarMediaController.getInstance(context)
    private val drlManager = DrlManager(context)
    private val seatHeatingManager = SeatHeatingManager(context)
    private val volumeController = VolumeController(context)

    private val keyToButtonId = mapOf(
        KeyCode.KEYCODE_R_SRC to SettingsManager.BTN_MODE,
        KeyCode.KEYCODE_R_HOME to SettingsManager.BTN_HOME,
        KeyCode.KEYCODE_R_MEDIA_NEXT to SettingsManager.BTN_TRACK_NEXT,
        KeyCode.KEYCODE_R_MEDIA_PREVIOUS to SettingsManager.BTN_TRACK_PREV,
        KeyCode.KEYCODE_R_MEDIA_PLAY_PAUSE to SettingsManager.BTN_MEDIA_PLAY_PAUSE,
        KeyCode.KEYCODE_R_VOLUME_MUTE to SettingsManager.BTN_MUTE,
        IICKeyCodes.KEY_CODE_IIC_MUTE to SettingsManager.BTN_MUTE,
        KeyCode.KEYCODE_R_CALL to SettingsManager.BTN_CALL,
        IICKeyCodes.KEY_CODE_IIC_POWER to SettingsManager.BTN_TOUCH_POWER
    )

    fun handleKeyPressWithRemapping(keyCode: Int, isLongPress: Boolean) {
        val buttonId = keyToButtonId[keyCode] ?: run {
            executeDefaultAction(keyCode, isLongPress)
            return
        }

        if (!settingsManager.isButtonRemapped(buttonId, isLongPress)) {
            executeDefaultAction(keyCode, isLongPress)
            return
        }

        when (settingsManager.getButtonActionType(buttonId, isLongPress)) {
            ButtonActionType.OPEN_APP -> launchAppForButton(buttonId, isLongPress)
            ButtonActionType.SPLIT_SCREEN -> launchSplitScreenForButton(buttonId, isLongPress)
            ButtonActionType.TOGGLE_DRL -> drlManager.toggleDrl()
            ButtonActionType.TOGGLE_DRIVER_SEAT_HEAT -> seatHeatingManager.toggleDriverSeatHeat()
            ButtonActionType.TOGGLE_PASSENGER_SEAT_HEAT -> seatHeatingManager.togglePassengerSeatHeat()
            ButtonActionType.TOGGLE_MEDIA_PLAY_PAUSE -> carMediaController.performCurrentMediaSessionAction(MediaActionType.PLAY_PAUSE)
            ButtonActionType.DEFAULT_ACTION -> executeDefaultAction(keyCode, isLongPress)
            ButtonActionType.NOTHING -> Log.d(TAG, "No action")
        }
    }

    private fun executeDefaultAction(keyCode: Int, isLongPress: Boolean) {
        when (keyCode) {
            KeyCode.KEYCODE_R_VOLUME_UP, IICKeyCodes.KEY_CODE_IIC_VOLUME_UP -> handleVolumeUp(isLongPress)
            KeyCode.KEYCODE_R_VOLUME_DOWN, IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN -> handleVolumeDown(isLongPress)
            KeyCode.KEYCODE_R_VOLUME_MUTE, IICKeyCodes.KEY_CODE_IIC_MUTE -> handleMute()
            KeyCode.KEYCODE_R_MEDIA_NEXT -> handleMediaAction(MediaActionType.NEXT)
            KeyCode.KEYCODE_R_MEDIA_PREVIOUS -> handleMediaAction(MediaActionType.PREVIOUS)
            KeyCode.KEYCODE_R_MEDIA_PLAY_PAUSE -> handleMediaAction(MediaActionType.PLAY_PAUSE)
            KeyCode.KEYCODE_R_CALL -> handleCallDefault(isLongPress)
            KeyCode.KEYCODE_R_SRC -> handleRsrcDefault()
            KeyCode.KEYCODE_R_HOME -> handleHomeDefault()
            IICKeyCodes.KEY_CODE_IIC_POWER -> handlePower()
        }
    }

    private fun launchAppForButton(buttonId: String, isLongPress: Boolean) {
        settingsManager.getButtonAppPackage(buttonId, isLongPress)?.let { pkg ->
            context.packageManager.getLaunchIntentForPackage(pkg)?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(it)
            }
        } ?: if (buttonId == SettingsManager.BTN_CALL) handleCallDefault(isLongPress) else Unit
    }

    private fun launchSplitScreenForButton(buttonId: String, isLongPress: Boolean) {
        val left = settingsManager.getButtonSplitAppPackage(buttonId, isLongPress, true)
        val right = settingsManager.getButtonSplitAppPackage(buttonId, isLongPress, false)
        if (left != null && right != null) SplitScreenLauncher.launchSplitScreenModeByPackage(context, left, right)
    }

    private fun handleVolumeUp(long: Boolean) = if (long) volumeController.startVolumeUp() else volumeController.adjustVolumeUpOnce()
    private fun handleVolumeDown(long: Boolean) = if (long) volumeController.startVolumeDown() else volumeController.adjustVolumeDownOnce()
    fun handleVolumeUpRelease() = volumeController.stopVolumeUp()
    fun handleVolumeDownRelease() = volumeController.stopVolumeDown()

    private fun handleMediaAction(type: MediaActionType) = carMediaController.performCurrentMediaSessionAction(type)

    private fun handleMute() {
        val action = if (audioManager.isStreamMute(AudioManager.STREAM_MUSIC)) AudioManager.ADJUST_UNMUTE else AudioManager.ADJUST_MUTE
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, action, AudioManager.FLAG_SHOW_UI)
    }

    private fun handleCallDefault(long: Boolean) {
        context.sendBroadcast(Intent(ECARX_ACTION_RCALL).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra("ecarx.extra.ECARX_KEY_EVENT_TYPE", 200005)
            putExtra("ecarx.extra.ECARX_KEY_ACTION_TYPE", if (long) 1 else 0)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    private fun handleHomeDefault() {
        context.startActivity(Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    private fun handleRsrcDefault() {
        context.sendBroadcast(Intent(ECARX_ACTION_QUIT_FULLSCREEN).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP })
        context.sendBroadcast(Intent(ECARX_ACTION_VR_EXIT).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP })
        context.sendBroadcast(Intent(ECARX_ACTION_RSRC).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra("ecarx.extra.ECARX_KEY_EVENT_TYPE", 210004)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    private fun handlePower() {
        if (!isScreensaverRunning()) {
            context.startService(Intent(SCREENSAVER_ACTION).apply { setPackage(SCREENSAVER_PACKAGE); addCategory(SCREENSAVER_CATEGORY) })
            return
        }
        context.sendBroadcast(Intent(ECARX_ACTION_POWER).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra("ecarx.extra.ECARX_KEY_EVENT_TYPE", 26)
            putExtra("ecarx.extra.ECARX_KEY_ACTION_TYPE", 0)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
        context.startService(Intent(SCREENSAVER_ACTION).apply { setPackage(SCREENSAVER_PACKAGE); addCategory(SCREENSAVER_CATEGORY) })
    }

    private fun isScreensaverRunning(): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return am.getRunningServices(Int.MAX_VALUE).any { it.service.className == SCREENSAVER_SERVICE }
    }
}
