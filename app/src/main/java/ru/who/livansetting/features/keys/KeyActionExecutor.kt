package ru.who.livansetting.features.keys

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log
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
class KeyActionExecutor(
    private val context: Context,
    private val drlManager: DrlManager,
    private val seatHeatingManager: SeatHeatingManager,
    private val settingsManager: SettingsManager,
    private val volumeController: VolumeController
) {

    companion object {
        private const val TAG = "KeyActionExecutor"

        // eCarX KeyCode constants inlined to avoid NoClassDefFoundError on emulator
        private const val KEYCODE_R_SRC = 210004
        private const val KEYCODE_R_HOME = 200003
        private const val KEYCODE_R_MEDIA_NEXT = 200087
        private const val KEYCODE_R_MEDIA_PREVIOUS = 200088
        private const val KEYCODE_R_MEDIA_PLAY_PAUSE = 200085
        private const val KEYCODE_R_VOLUME_MUTE = 200164
        private const val KEYCODE_R_CALL = 200005
        private const val KEYCODE_R_VOLUME_UP = 200024
        private const val KEYCODE_R_VOLUME_DOWN = 200025

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

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val carMediaController = CarMediaController.getInstance(context)

    private val keyToButtonId = mapOf(
        KEYCODE_R_SRC to SettingsManager.BTN_MODE,
        KEYCODE_R_HOME to SettingsManager.BTN_HOME,
        KEYCODE_R_MEDIA_NEXT to SettingsManager.BTN_TRACK_NEXT,
        KEYCODE_R_MEDIA_PREVIOUS to SettingsManager.BTN_TRACK_PREV,
        KEYCODE_R_MEDIA_PLAY_PAUSE to SettingsManager.BTN_MEDIA_PLAY_PAUSE,
        KEYCODE_R_VOLUME_MUTE to SettingsManager.BTN_MUTE,
        IICKeyCodes.KEY_CODE_IIC_MUTE to SettingsManager.BTN_MUTE,
        KEYCODE_R_CALL to SettingsManager.BTN_CALL,
        IICKeyCodes.KEY_CODE_IIC_POWER to SettingsManager.BTN_TOUCH_POWER
    )

    fun handleKeyPressWithRemapping(keyCode: Int, isLongPress: Boolean) {
        val buttonId = keyToButtonId[keyCode] ?: run {
            Log.d(TAG, "Handling key: buttonId=null isLongPress=$isLongPress action=DEFAULT")
            executeDefaultAction(keyCode, isLongPress)
            return
        }

        val actionType = settingsManager.getButtonActionType(buttonId, isLongPress)
        Log.d(TAG, "Handling key: buttonId=$buttonId isLongPress=$isLongPress action=$actionType")

        when (actionType) {
            ButtonActionType.OPEN_APP -> launchAppForButton(keyCode, buttonId, isLongPress)
            ButtonActionType.SPLIT_SCREEN -> launchSplitScreenForButton(buttonId, isLongPress)
            ButtonActionType.TOGGLE_DRL -> drlManager.toggleDrl()
            ButtonActionType.TOGGLE_DRIVER_SEAT_HEAT -> seatHeatingManager.toggleDriverSeatHeat()
            ButtonActionType.TOGGLE_PASSENGER_SEAT_HEAT -> seatHeatingManager.togglePassengerSeatHeat()
            ButtonActionType.TOGGLE_MEDIA_PLAY_PAUSE -> carMediaController.performCurrentMediaSessionAction(MediaActionType.PLAY_PAUSE)
            ButtonActionType.TOGGLE_MEDIA_NEXT -> carMediaController.performCurrentMediaSessionAction(MediaActionType.NEXT)
            ButtonActionType.TOGGLE_MEDIA_PREVIOUS -> carMediaController.performCurrentMediaSessionAction(MediaActionType.PREVIOUS)
            ButtonActionType.SEND_INTENT -> sendIntentForButton(buttonId, isLongPress)
            ButtonActionType.DEFAULT_ACTION -> executeDefaultAction(keyCode, isLongPress)
            ButtonActionType.NOTHING -> { /* intentionally suppressed — user configured no action */ }
        }
    }

    private fun executeDefaultAction(keyCode: Int, isLongPress: Boolean) {
        when (keyCode) {
            KEYCODE_R_VOLUME_UP, IICKeyCodes.KEY_CODE_IIC_VOLUME_UP -> handleVolumeUp(isLongPress)
            KEYCODE_R_VOLUME_DOWN, IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN -> handleVolumeDown(isLongPress)
            KEYCODE_R_VOLUME_MUTE, IICKeyCodes.KEY_CODE_IIC_MUTE -> handleMute()
            KEYCODE_R_MEDIA_NEXT -> handleMediaAction(MediaActionType.NEXT)
            KEYCODE_R_MEDIA_PREVIOUS -> handleMediaAction(MediaActionType.PREVIOUS)
            KEYCODE_R_MEDIA_PLAY_PAUSE -> handleMediaAction(MediaActionType.PLAY_PAUSE)
            KEYCODE_R_CALL -> handleCallDefault(isLongPress)
            KEYCODE_R_SRC -> handleRsrcDefault()
            KEYCODE_R_HOME -> handleHomeDefault()
            IICKeyCodes.KEY_CODE_IIC_POWER -> handlePower()
        }
    }

    private fun launchAppForButton(keyCode: Int, buttonId: String, isLongPress: Boolean) {
        settingsManager.getButtonAppPackage(buttonId, isLongPress)?.let { pkg ->
            context.packageManager.getLaunchIntentForPackage(pkg)?.let { intent ->
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                return
            }
        }
        // No app configured or not found — fall back to hardware default
        executeDefaultAction(keyCode, isLongPress)
    }

    private fun launchSplitScreenForButton(buttonId: String, isLongPress: Boolean) {
        val left = settingsManager.getButtonSplitAppPackage(buttonId, isLongPress, true)
        val right = settingsManager.getButtonSplitAppPackage(buttonId, isLongPress, false)
        if (left.isNullOrEmpty() || right.isNullOrEmpty()) {
            Log.w(TAG, "SPLIT_SCREEN: package not configured left=$left right=$right")
            return
        }
        val result = SplitScreenLauncher.launchSplitScreenModeByPackage(context, left, right)
        Log.d(TAG, "SPLIT_SCREEN result: success=${result.success} error=${result.errorMessage}")
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
            setPackage("com.ecarx.btphone")
            putExtra("ecarx.extra.ECARX_KEY_EVENT_TYPE", 200005)
            putExtra("ecarx.extra.ECARX_KEY_ACTION_TYPE", if (long) 1 else 0)
        })
    }

    private fun handleHomeDefault() {
        context.startActivity(Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    private fun handleRsrcDefault() {
        context.sendBroadcast(Intent(ECARX_ACTION_QUIT_FULLSCREEN))
        context.sendBroadcast(Intent(ECARX_ACTION_VR_EXIT))
        context.sendBroadcast(Intent(ECARX_ACTION_RSRC).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra("ecarx.extra.ECARX_KEY_EVENT_TYPE", 210004)
        })
    }

    private fun handlePower() {
        val wasActive = settingsManager.isScreensaverActive()
        if (!wasActive) {
            settingsManager.setScreensaverActive(true)
            context.startService(Intent(SCREENSAVER_ACTION).apply {
                setPackage(SCREENSAVER_PACKAGE)
                addCategory(SCREENSAVER_CATEGORY)
            })
        } else {
            settingsManager.setScreensaverActive(false)
            context.sendBroadcast(Intent(ECARX_ACTION_POWER).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                putExtra("ecarx.extra.ECARX_KEY_EVENT_TYPE", 26)
                putExtra("ecarx.extra.ECARX_KEY_ACTION_TYPE", 0)
            })
        }
    }

    private fun sendIntentForButton(buttonId: String, isLongPress: Boolean) {
        val action = settingsManager.getButtonIntentAction(buttonId, isLongPress)
        if (action.isNullOrEmpty()) {
            Log.w(TAG, "SEND_INTENT: no action configured for button=$buttonId isLongPress=$isLongPress")
            return
        }
        Log.d(TAG, "SEND_INTENT: sending broadcast action=$action")
        context.sendBroadcast(Intent(action))
    }
}
