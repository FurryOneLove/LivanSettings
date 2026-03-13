package ru.who.livansetting.utils

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.os.Build
import android.util.Log
import android.view.KeyEvent
import android.view.InputEvent
import com.ecarx.xui.adaptapi.input.KeyCode
import ru.who.livansetting.ButtonActionType
import ru.who.livansetting.services.MainService
import ru.who.livansetting.SettingsManager
import ru.who.livansetting.constants.IICKeyCodes
import ru.who.livansetting.utils.SplitScreenLauncher
import ru.who.livansetting.utils.CarMediaController
import ru.who.livansetting.utils.MediaActionType
import ru.who.livansetting.utils.SeatHeatingManager
import ru.who.livansetting.utils.DrlManager
import ru.who.livansetting.utils.VolumeController

/**
 * Исполнитель действий при нажатии клавиш
 */
class KeyActionExecutor(private val context: Context) {
    
    companion object {
        private const val TAG = "KeyActionExecutor"
        
        // ECARX Power Event Constants
        private const val ECARX_ACTION_ECARX_KEY_POWER_EVENT = "ecarx.intent.action.ECARX_KEY_POWER_EVENT"
        private const val ECARX_EXTRA_ECARX_KEY_EVENT_TYPE = "ecarx.extra.ECARX_KEY_EVENT_TYPE"
        private const val ECARX_EXTRA_ECARX_KEY_ACTION_TYPE = "ecarx.extra.ECARX_KEY_ACTION_TYPE"
        private const val ECARX_KEY_EVENT_TYPE_POWER = 26
        private const val ECARX_KEY_ACTION_TYPE_PRESS = 0
        
        // ECARX Call Event Constants
        private const val ECARX_ACTION_ECARX_KEY_RCALL_EVENT = "ecarx.intent.action.ECARX_KEY_RCALL_EVENT"
        private const val ECARX_KEY_EVENT_TYPE_RCALL = 200005
        private const val ECARX_KEY_ACTION_TYPE_RCALL_SHORT = 0
        private const val ECARX_KEY_ACTION_TYPE_RCALL_LONG = 1
        private const val INTENT_FLAG_ACTIVITY_CLEAR_TOP = 16777216
        
        // ECARX RSRC Event Constants
        private const val ECARX_ACTION_ECARX_KEY_RSRC_EVENT = "ecarx.intent.action.ECARX_KEY_RSRC_EVENT"
        private const val ECARX_KEY_EVENT_TYPE_RSRC = 210004
        
        // VR Exit and Fullscreen Exit Constants
        private const val ECARX_ACTION_VR_EXIT = "ecarx.action.ECARX_VR_EXIT"
        private const val ECARX_ACTION_QUIT_FULLSCREEN_VIEW = "ecarx.intent.action.QUIT_FULLSCREEN_VIEW"
        private const val INTENT_FLAG_ACTIVITY_NEW_TASK = 268435456
        
        // Screensaver Constants
        private const val SCREENSAVER_ACTION = "android.intent.action.SCREENSAVER"
        private const val SCREENSAVER_CATEGORY = "android.intent.category.SCREENSAVER"
        private const val SCREENSAVER_PACKAGE = "com.ecarx.screensaver"
        private const val SCREENSAVER_SERVICE = "com.ecarx.screensaver.ScreensaverService"
    }
    
    private val settingsManager = SettingsManager(context)
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val mediaSessionManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        context.getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
    } else {
        null
    }
    private val carMediaController = CarMediaController.getInstance(context)
    private val drlManager = DrlManager(context)
    private val seatHeatingManager = SeatHeatingManager(context)
    private val volumeController = VolumeController(context)
    
    // Ссылка на MainService для доступа к CarService
    private val mainService: MainService? get() = MainService.getInstance()
    
    /**
     * Проверяет, работает ли сервис screensaver
     */
    private fun isScreensaverServiceRunning(): Boolean {
        try {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningServices = activityManager.getRunningServices(Integer.MAX_VALUE)
            
            for (serviceInfo in runningServices) {
                if (serviceInfo.service.className == SCREENSAVER_SERVICE) {
                    return true
                }
            }
            return false
        } catch (e: Exception) {
            Log.e(TAG, "Error checking screensaver service status", e)
            return false
        }
    }
    
    /**
     * Универсальная функция для получения пакета приложения кнопки
     * @param keyCode код кнопки
     * @param isLongPress true если это длительное нажатие
     * @return пакет приложения или null если не настроен
     */
    private fun getButtonAppPackage(keyCode: Int, isLongPress: Boolean): String? {
        return when (keyCode) {
            KeyCode.KEYCODE_R_MEDIA_NEXT -> {
                if (isLongPress) {
                    if (settingsManager.isTrackNextLongPressRemapped()) {
                        settingsManager.getTrackNextLongPressAppPackage()
                    } else null
                } else {
                    if (settingsManager.isTrackNextShortPressRemapped()) {
                        settingsManager.getTrackNextShortPressAppPackage()
                    } else null
                }
            }
            KeyCode.KEYCODE_R_MEDIA_PREVIOUS -> {
                if (isLongPress) {
                    if (settingsManager.isTrackPrevLongPressRemapped()) {
                        settingsManager.getTrackPrevLongPressAppPackage()
                    } else null
                } else {
                    if (settingsManager.isTrackPrevShortPressRemapped()) {
                        settingsManager.getTrackPrevShortPressAppPackage()
                    } else null
                }
            }
            KeyCode.KEYCODE_R_MEDIA_PLAY_PAUSE -> {
                if (isLongPress) {
                    if (settingsManager.isMediaPlayPauseLongPressRemapped()) {
                        settingsManager.getMediaPlayPauseLongPressAppPackage()
                    } else null
                } else {
                    if (settingsManager.isMediaPlayPauseShortPressRemapped()) {
                        settingsManager.getMediaPlayPauseShortPressAppPackage()
                    } else null
                }
            }
            KeyCode.KEYCODE_R_VOLUME_MUTE, IICKeyCodes.KEY_CODE_IIC_MUTE -> {
                if (isLongPress) {
                    if (settingsManager.isMuteLongPressRemapped()) {
                        settingsManager.getMuteLongPressAppPackage()
                    } else null
                } else {
                    if (settingsManager.isMuteShortPressRemapped()) {
                        settingsManager.getMuteShortPressAppPackage()
                    } else null
                }
            }
            KeyCode.KEYCODE_R_CALL -> {
                if (isLongPress) {
                    if (settingsManager.isCallLongPressRemapped()) {
                        settingsManager.getCallLongPressAppPackage()
                    } else null
                } else {
                    if (settingsManager.isCallShortPressRemapped()) {
                        settingsManager.getCallShortPressAppPackage()
                    } else null
                }
            }
            IICKeyCodes.KEY_CODE_IIC_POWER -> {
                if (isLongPress) {
                    if (settingsManager.isTouchPowerLongPressRemapped()) {
                        settingsManager.getTouchPowerLongPressAppPackage()
                    } else null
                } else {
                    if (settingsManager.isTouchPowerShortPressRemapped()) {
                        settingsManager.getTouchPowerShortPressAppPackage()
                    } else null
                }
            }
            KeyCode.KEYCODE_R_SRC -> {
                if (isLongPress) {
                    if (settingsManager.isModeLongPressRemapped()) {
                        settingsManager.getModeLongPressAppPackage()
                    } else null
                } else {
                    if (settingsManager.isModeShortPressRemapped()) {
                        settingsManager.getModeShortPressAppPackage()
                    } else null
                }
            }
            KeyCode.KEYCODE_R_HOME -> {
                if (isLongPress) {
                    if (settingsManager.isHomeLongPressRemapped()) {
                        settingsManager.getHomeLongPressAppPackage()
                    } else null
                } else {
                    if (settingsManager.isHomeShortPressRemapped()) {
                        settingsManager.getHomeShortPressAppPackage()
                    } else null
                }
            }
            else -> null
        }
    }
    
    /**
     * Универсальный метод для запуска приложения в зависимости от кнопки и типа нажатия
     * @param keyCode код кнопки
     * @param isLongPress true если это длительное нажатие
     */
    fun launchAppForButton(keyCode: Int, isLongPress: Boolean) {
        val buttonName = getButtonName(keyCode)
        val pressType = if (isLongPress) "long" else "short"
        val action = "$buttonName $pressType press"
        
        val packageName = getButtonAppPackage(keyCode, isLongPress)
        
        if (packageName != null) {
            launchApp(packageName, action)
        } else {
            if (keyCode == KeyCode.KEYCODE_R_CALL) {
                handleCallDefault(isLongPress)
            } else {
                Log.w(TAG, "No app selected for $action")
            }
        }
    }
    
    /**
     * Получает название кнопки по её коду
     */
    private fun getButtonName(keyCode: Int): String {
        return when (keyCode) {
            KeyCode.KEYCODE_R_MEDIA_NEXT -> "track next"
            KeyCode.KEYCODE_R_MEDIA_PREVIOUS -> "track previous"
            KeyCode.KEYCODE_R_MEDIA_PLAY_PAUSE -> "media play pause"
            KeyCode.KEYCODE_R_VOLUME_MUTE, IICKeyCodes.KEY_CODE_IIC_MUTE -> "mute"
            KeyCode.KEYCODE_R_CALL -> "call"
            IICKeyCodes.KEY_CODE_IIC_POWER -> "touch power"
            KeyCode.KEYCODE_R_SRC -> "mode"
            KeyCode.KEYCODE_R_HOME -> "home"
            KeyCode.KEYCODE_R_VOLUME_DOWN -> "volume down steering"
            KeyCode.KEYCODE_R_VOLUME_UP -> "volume up steering"
            IICKeyCodes.KEY_CODE_IIC_VOLUME_UP -> "volume up touch"
            IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN -> "volume down touch"
            else -> "unknown button"
        }
    }
    
    /**
     * Общий метод для запуска приложения
     */
    private fun launchApp(packageName: String, action: String) {
        try {
            val packageManager = context.packageManager
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launchIntent)
                Log.d(TAG, "Launched app for $action: $packageName")
            } else {
                Log.w(TAG, "No launch intent found for package: $packageName")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error launching app for $action: $packageName", e)
        }
    }
    
    /**
     * Обрабатывает нажатие кнопки громкости вверх
     * @param isLongPress true если это длительное нажатие
     */
    fun handleVolumeUp(isLongPress: Boolean) {
        if (isLongPress) {
            volumeController.startVolumeUp()
        } else {
            volumeController.adjustVolumeUpOnce()
        }
    }
    
    /**
     * Обрабатывает нажатие кнопки громкости вниз
     * @param isLongPress true если это длительное нажатие
     */
    fun handleVolumeDown(isLongPress: Boolean) {
        if (isLongPress) {
            volumeController.startVolumeDown()
        } else {
            volumeController.adjustVolumeDownOnce()
        }
    }
    
    /**
     * Обрабатывает отпускание клавиши громкости
     * @param keyCode код клавиши
     */
    fun handleVolumeKeyRelease(keyCode: Int) {
        when (keyCode) {
            KeyCode.KEYCODE_R_VOLUME_UP, IICKeyCodes.KEY_CODE_IIC_VOLUME_UP -> {
                volumeController.stopVolumeUp()
            }
            KeyCode.KEYCODE_R_VOLUME_DOWN, IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN -> {
                volumeController.stopVolumeDown()
            }
        }
    }
    
    /**
     * Обрабатывает нажатие кнопки "Следующий трек"
     * @param isLongPress true если это длительное нажатие
     */
    fun handleMediaNext(isLongPress: Boolean) {
        try {
            Log.d(TAG, "Media next: ${if (isLongPress) "long" else "short"} press - sending media next command")
            carMediaController.performCurrentMediaSessionAction(MediaActionType.NEXT, settingsManager.getSharedPreferences())
        } catch (e: Exception) {
            Log.e(TAG, "Error sending media next command", e)
        }
    }
    
    /**
     * Обрабатывает нажатие кнопки "Предыдущий трек"
     * @param isLongPress true если это длительное нажатие
     */
    fun handleMediaPrevious(isLongPress: Boolean) {
        try {
            Log.d(TAG, "Media previous: ${if (isLongPress) "long" else "short"} press - sending media previous command")
            carMediaController.performCurrentMediaSessionAction(MediaActionType.PREVIOUS, settingsManager.getSharedPreferences())
        } catch (e: Exception) {
            Log.e(TAG, "Error sending media previous command", e)
        }
    }
    
    /**
     * Обрабатывает нажатие кнопки "Play/Pause"
     * @param isLongPress true если это длительное нажатие
     */
    fun handleMediaPlayPause(isLongPress: Boolean) {
        try {
            Log.d(TAG, "Media play/pause: ${if (isLongPress) "long" else "short"} press - sending media play/pause command")
            carMediaController.performCurrentMediaSessionAction(MediaActionType.PLAY_PAUSE, settingsManager.getSharedPreferences())
        } catch (e: Exception) {
            Log.e(TAG, "Error sending media play/pause command", e)
        }
    }
    
    /**
     * Обрабатывает нажатие кнопки MUTE
     * @param isLongPress true если это длительное нажатие
     */
    fun handleMute(isLongPress: Boolean) {
        try {
            val isMuted = audioManager.isStreamMute(AudioManager.STREAM_MUSIC)
            if (isMuted) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_SHOW_UI)
                Log.d(TAG, "Mute: ${if (isLongPress) "long" else "short"} press - unmuted")
            } else {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, AudioManager.FLAG_SHOW_UI)
                Log.d(TAG, "Mute: ${if (isLongPress) "long" else "short"} press - muted")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error toggling mute", e)
        }
    }
    
    /**
     * Обрабатывает нажатие кнопки вызова по умолчанию (ECARX call event)
     * @param isLongPress true если это длительное нажатие
     */
    fun handleCallDefault(isLongPress: Boolean) {
        try {
            val pressType = if (isLongPress) "long" else "short"
            val actionType = if (isLongPress) ECARX_KEY_ACTION_TYPE_RCALL_LONG else ECARX_KEY_ACTION_TYPE_RCALL_SHORT
            
            Log.i(TAG, "ECarXRCallAction sendBroadcast RCALL on${if (isLongPress) "LongPressTriggered" else "ShortClick"}")
            
            val intent = Intent(ECARX_ACTION_ECARX_KEY_RCALL_EVENT).apply {
                addCategory("android.intent.category.DEFAULT")
                putExtra("ecarx.extra.ECARX_KEY_EVENT_TYPE", ECARX_KEY_EVENT_TYPE_RCALL)
                putExtra("ecarx.extra.ECARX_KEY_ACTION_TYPE", actionType)
                flags = INTENT_FLAG_ACTIVITY_NEW_TASK
                addFlags(INTENT_FLAG_ACTIVITY_CLEAR_TOP)
            }
            
            context.sendBroadcast(intent)
            Log.d(TAG, "ECARX call event broadcast sent for $pressType press with action type: $actionType")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error sending ECARX call event", e)
        }
    }
    
    /**
     * Отправляет broadcast для выхода из VR режима
     */
    private fun sendVRExitBroadCast() {
        Log.d(TAG, "sendVRExitBroadCast")
        val intent = Intent(ECARX_ACTION_VR_EXIT).apply {
            flags = INTENT_FLAG_ACTIVITY_NEW_TASK
            addFlags(INTENT_FLAG_ACTIVITY_CLEAR_TOP)
        }
        context.sendBroadcast(intent)
    }

    /**
     * Отправляет broadcast для выхода из полноэкранного режима
     */
    private fun sendExitFullScreenBroadCast() {
        Log.d(TAG, "sendExitFullScreenBroadCast")
        val intent = Intent(ECARX_ACTION_QUIT_FULLSCREEN_VIEW).apply {
            flags = INTENT_FLAG_ACTIVITY_NEW_TASK
            addFlags(INTENT_FLAG_ACTIVITY_CLEAR_TOP)
        }
        context.sendBroadcast(intent)
    }

    /**
     * Обрабатывает нажатие кнопки RSRC по умолчанию (ECARX RSRC event)
     * @param isLongPress true если это длительное нажатие
     */
    fun handleRsrcDefault(isLongPress: Boolean) {
        try {
            Log.i(TAG, "sendBroadcast SourceKey")
            doPerformCustom()
        } catch (e: Exception) {
            Log.e(TAG, "Error handling RSRC default action", e)
        }
    }

    /**
     * Выполняет кастомное действие для RSRC кнопки
     */
    private fun doPerformCustom() {
        sendExitFullScreenBroadCast()
        sendVRExitBroadCast()
        
        val intent = Intent(ECARX_ACTION_ECARX_KEY_RSRC_EVENT).apply {
            addCategory("android.intent.category.DEFAULT")
            putExtra("ecarx.extra.ECARX_KEY_EVENT_TYPE", ECARX_KEY_EVENT_TYPE_RSRC)
            flags = INTENT_FLAG_ACTIVITY_NEW_TASK
            addFlags(INTENT_FLAG_ACTIVITY_CLEAR_TOP)
        }
        context.sendBroadcast(intent)
    }
    
    /**
     * Обрабатывает нажатие кнопки HOME по умолчанию (выход на главный экран)
     * @param isLongPress true если это длительное нажатие
     */
    fun handleHomeDefault(isLongPress: Boolean) {
        try {
            Log.d(TAG, "Home default: ${if (isLongPress) "long" else "short"} press - going to home screen")
            
            // Создаем Intent для выхода на главный экран
            val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            
            context.startActivity(homeIntent)
            Log.d(TAG, "Home screen intent sent")
            
        } catch (e: Exception) {
            Log.e(TAG, "Error going to home screen", e)
        }
    }
    
    /**
     * Обрабатывает нажатие кнопки POWER
     * @param isLongPress true если это длительное нажатие
     */
    fun handlePower(isLongPress: Boolean) {
        try {
            Log.i(TAG, "sendBroadcast Power onShortClick")
            
            if (!isScreensaverServiceRunning()) {
                Log.i(TAG, "screen_service_notwork")
                
                // Запускаем screensaver сервис
                val screensaverIntent = Intent(SCREENSAVER_ACTION).apply {
                    addCategory(SCREENSAVER_CATEGORY)
                    setPackage(SCREENSAVER_PACKAGE)
                }
                context.startService(screensaverIntent)
                return
            }
            
            Log.i(TAG, "screen_service_work")
            
            // Отправляем ECARX power event
            val powerEventIntent = Intent(ECARX_ACTION_ECARX_KEY_POWER_EVENT).apply {
                addCategory("android.intent.category.DEFAULT")
                putExtra(ECARX_EXTRA_ECARX_KEY_EVENT_TYPE, ECARX_KEY_EVENT_TYPE_POWER)
                putExtra(ECARX_EXTRA_ECARX_KEY_ACTION_TYPE, ECARX_KEY_ACTION_TYPE_PRESS)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            
            // Запускаем screensaver сервис
            val screensaverIntent = Intent(SCREENSAVER_ACTION).apply {
                addCategory(SCREENSAVER_CATEGORY)
                setPackage(SCREENSAVER_PACKAGE)
            }
            
            // Отправляем broadcast и запускаем сервис
            context.sendBroadcast(powerEventIntent)
            context.startService(screensaverIntent)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error handling power button", e)
        }
    }
    
    /**
     * Универсальная функция для получения типа действия кнопки
     * @param keyCode код кнопки
     * @param isLongPress true если это длительное нажатие
     * @return тип действия кнопки
     */
    private fun getButtonActionType(keyCode: Int, isLongPress: Boolean): ButtonActionType {
        return when (keyCode) {
            KeyCode.KEYCODE_R_SRC -> {
                if (isLongPress) settingsManager.getModeLongPressActionType()
                else settingsManager.getModeShortPressActionType()
            }
            KeyCode.KEYCODE_R_HOME -> {
                if (isLongPress) settingsManager.getHomeLongPressActionType()
                else settingsManager.getHomeShortPressActionType()
            }
            KeyCode.KEYCODE_R_MEDIA_NEXT -> {
                if (isLongPress) settingsManager.getTrackNextLongPressActionType()
                else settingsManager.getTrackNextShortPressActionType()
            }
            KeyCode.KEYCODE_R_MEDIA_PREVIOUS -> {
                if (isLongPress) settingsManager.getTrackPrevLongPressActionType()
                else settingsManager.getTrackPrevShortPressActionType()
            }
            KeyCode.KEYCODE_R_MEDIA_PLAY_PAUSE -> {
                if (isLongPress) settingsManager.getMediaPlayPauseLongPressActionType()
                else settingsManager.getMediaPlayPauseShortPressActionType()
            }
            KeyCode.KEYCODE_R_VOLUME_MUTE, IICKeyCodes.KEY_CODE_IIC_MUTE -> {
                if (isLongPress) settingsManager.getMuteLongPressActionType()
                else settingsManager.getMuteShortPressActionType()
            }
            KeyCode.KEYCODE_R_CALL -> {
                if (isLongPress) settingsManager.getCallLongPressActionType()
                else settingsManager.getCallShortPressActionType()
            }
            IICKeyCodes.KEY_CODE_IIC_POWER -> {
                if (isLongPress) settingsManager.getTouchPowerLongPressActionType()
                else settingsManager.getTouchPowerShortPressActionType()
            }
            // Для кнопок громкости всегда возвращаем DEFAULT_ACTION, чтобы они выполняли действие по умолчанию
            KeyCode.KEYCODE_R_VOLUME_UP, IICKeyCodes.KEY_CODE_IIC_VOLUME_UP,
            KeyCode.KEYCODE_R_VOLUME_DOWN, IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN -> {
                ButtonActionType.DEFAULT_ACTION
            }
            else -> ButtonActionType.OPEN_APP // Для остальных кнопок по умолчанию OPEN_APP
        }
    }
    
    /**
     * Универсальная функция для проверки переназначения кнопки
     * @param keyCode код кнопки
     * @param isLongPress true если это длительное нажатие
     * @return true если кнопка переназначена
     */
    private fun isButtonRemapped(keyCode: Int, isLongPress: Boolean): Boolean {
        return when (keyCode) {
            KeyCode.KEYCODE_R_MEDIA_NEXT -> {
                if (isLongPress) settingsManager.isTrackNextLongPressRemapped()
                else settingsManager.isTrackNextShortPressRemapped()
            }
            KeyCode.KEYCODE_R_MEDIA_PREVIOUS -> {
                if (isLongPress) settingsManager.isTrackPrevLongPressRemapped()
                else settingsManager.isTrackPrevShortPressRemapped()
            }
            KeyCode.KEYCODE_R_MEDIA_PLAY_PAUSE -> {
                if (isLongPress) settingsManager.isMediaPlayPauseLongPressRemapped()
                else settingsManager.isMediaPlayPauseShortPressRemapped()
            }
            KeyCode.KEYCODE_R_VOLUME_MUTE, IICKeyCodes.KEY_CODE_IIC_MUTE -> {
                if (isLongPress) settingsManager.isMuteLongPressRemapped()
                else settingsManager.isMuteShortPressRemapped()
            }
            KeyCode.KEYCODE_R_CALL -> {
                if (isLongPress) settingsManager.isCallLongPressRemapped()
                else settingsManager.isCallShortPressRemapped()
            }
            IICKeyCodes.KEY_CODE_IIC_POWER -> {
                if (isLongPress) settingsManager.isTouchPowerLongPressRemapped()
                else settingsManager.isTouchPowerShortPressRemapped()
            }
            KeyCode.KEYCODE_R_SRC -> {
                if (isLongPress) settingsManager.isModeLongPressRemapped()
                else settingsManager.isModeShortPressRemapped()
            }
            KeyCode.KEYCODE_R_HOME -> {
                if (isLongPress) settingsManager.isHomeLongPressRemapped()
                else settingsManager.isHomeShortPressRemapped()
            }
            else -> false
        }
    }
    
    /**
     * Универсальный метод для обработки нажатия клавиши с учетом настроек переназначения
     * @param keyCode код кнопки
     * @param isLongPress true если это длительное нажатие
     */
    fun handleKeyPressWithRemapping(keyCode: Int, isLongPress: Boolean) {
        val buttonName = getButtonName(keyCode)
        val pressType = if (isLongPress) "long" else "short"
        val action = "$buttonName $pressType press"
        
        Log.d(TAG, "Handling $action with remapping logic")
        
        val isRemapped = isButtonRemapped(keyCode, isLongPress)
        
        if (!isRemapped) {
            // Переключатель выключен - выполняем действие по умолчанию
            Log.d(TAG, "Remapping disabled for $action, executing default action")
            executeDefaultAction(keyCode, isLongPress)
        } else {
            // Переключатель включен - проверяем тип действия
            val actionType = getButtonActionType(keyCode, isLongPress)
            
            when (actionType) {
                ButtonActionType.OPEN_APP -> {
                    Log.d(TAG, "Action type: OPEN_APP for $action")
                    launchAppForButton(keyCode, isLongPress)
                }
                ButtonActionType.SPLIT_SCREEN -> {
                    Log.d(TAG, "Action type: SPLIT_SCREEN for $action")
                    launchSplitScreenForButton(keyCode, isLongPress)
                }
                ButtonActionType.TOGGLE_DRL -> {
                    Log.d(TAG, "Action type: TOGGLE_DRL for $action")
                    drlManager.toggleDrl()
                }
                ButtonActionType.TOGGLE_DRIVER_SEAT_HEAT -> {
                    Log.d(TAG, "Action type: TOGGLE_DRIVER_SEAT_HEAT for $action")
                    seatHeatingManager.toggleDriverSeatHeat()
                }
                ButtonActionType.TOGGLE_PASSENGER_SEAT_HEAT -> {
                    Log.d(TAG, "Action type: TOGGLE_PASSENGER_SEAT_HEAT for $action")
                    seatHeatingManager.togglePassengerSeatHeat()
                }
                ButtonActionType.TOGGLE_MEDIA_PLAY_PAUSE -> {
                    Log.d(TAG, "Action type: TOGGLE_MEDIA_PLAY_PAUSE for $action")
                    val carMediaController = CarMediaController.getInstance(context)
                    carMediaController.performCurrentMediaSessionAction(MediaActionType.PLAY_PAUSE, settingsManager.getSharedPreferences())
                }
                ButtonActionType.DEFAULT_ACTION -> {
                    Log.d(TAG, "Action type: DEFAULT_ACTION for $action")
                    executeDefaultAction(keyCode, isLongPress)
                }
                ButtonActionType.NOTHING -> {
                    Log.d(TAG, "Action type: NOTHING for $action, no action taken")
                }
            }
        }
    }
    
    /**
     * Выполняет действие по умолчанию для кнопки
     * @param keyCode код кнопки
     * @param isLongPress true если это длительное нажатие
     */
    private fun executeDefaultAction(keyCode: Int, isLongPress: Boolean) {
        when (keyCode) {
            KeyCode.KEYCODE_R_VOLUME_UP, IICKeyCodes.KEY_CODE_IIC_VOLUME_UP -> {
                handleVolumeUp(isLongPress)
            }
            KeyCode.KEYCODE_R_VOLUME_DOWN, IICKeyCodes.KEY_CODE_IIC_VOLUME_DOWN -> {
                handleVolumeDown(isLongPress)
            }
            KeyCode.KEYCODE_R_VOLUME_MUTE, IICKeyCodes.KEY_CODE_IIC_MUTE -> {
                handleMute(isLongPress)
            }
            KeyCode.KEYCODE_R_MEDIA_NEXT -> handleMediaNext(isLongPress)
            KeyCode.KEYCODE_R_MEDIA_PREVIOUS -> handleMediaPrevious(isLongPress)
            KeyCode.KEYCODE_R_MEDIA_PLAY_PAUSE -> handleMediaPlayPause(isLongPress)
            KeyCode.KEYCODE_R_CALL -> handleCallDefault(isLongPress)
            KeyCode.KEYCODE_R_SRC -> handleRsrcDefault(isLongPress)
            KeyCode.KEYCODE_R_HOME -> handleHomeDefault(isLongPress)
            IICKeyCodes.KEY_CODE_IIC_POWER -> handlePower(isLongPress)
        }
    }
    
    /**
     * Получает менеджер DRL для внешнего использования
     */
    fun getDrlManager(): DrlManager {
        return drlManager
    }
    
    /**
     * Получает менеджер подогрева сидений для внешнего использования
     */
    fun getSeatHeatingManager(): SeatHeatingManager {
        return seatHeatingManager
    }
    
    /**
     * Публичный метод для запуска split screen с указанной позицией
     * @param leftAppPackage Имя пакета левого приложения
     * @param rightAppPackage Имя пакета правого приложения
     * @param position Позиция (0 = TOP_OR_LEFT, 1 = BOTTOM_OR_RIGHT)
     * @return LaunchResult с результатом операции
     */
    fun launchSplitScreenWithPosition(
        leftAppPackage: String,
        rightAppPackage: String,
        position: Int
    ): SplitScreenLauncher.LaunchResult {
        if (!SplitScreenLauncher.isSplitScreenSupported()) {
            return SplitScreenLauncher.LaunchResult(false, "Split screen mode is not supported on this device")
        }
        
        return SplitScreenLauncher.launchSplitScreenWithPosition(
            context = context,
            package1 = leftAppPackage,
            package2 = rightAppPackage,
            position = position
        )
    }
    
    /**
     * Запускает приложения в режиме разделенного экрана по именам пакетов
     * @param primaryPackageName Имя пакета основного приложения
     * @param secondaryPackageName Имя пакета вторичного приложения
     * @return LaunchResult с результатом операции
     */
    fun launchSplitScreenModeByPackage(
        primaryPackageName: String,
        secondaryPackageName: String
    ): SplitScreenLauncher.LaunchResult {
        if (!SplitScreenLauncher.isSplitScreenSupported()) {
            return SplitScreenLauncher.LaunchResult(false, "Split screen mode is not supported on this device")
        }
        
        return SplitScreenLauncher.launchSplitScreenModeByPackage(
            context = context,
            primaryPackageName = primaryPackageName,
            secondaryPackageName = secondaryPackageName
        )
    }
    
    /**
     * Проверяет, поддерживает ли приложение split screen режим
     * @param packageName имя пакета приложения
     * @return true если приложение поддерживает split screen
     */
    fun isPackageSupportedSplitScreen(packageName: String): Boolean {
        return SplitScreenLauncher.isPackageSupportedSplitScreen(context, packageName)
    }
    
    /**
     * Получает список всех установленных приложений, поддерживающих split screen
     * @return список пакетов приложений
     */
    fun getSupportedSplitScreenApps(): List<String> {
        return SplitScreenLauncher.getSupportedSplitScreenApps(context)
    }
    
    /**
     * Обрабатывает отпускание кнопки громкости вверх
     */
    fun handleVolumeUpRelease() {
        volumeController.stopVolumeUp()
    }
    
    /**
     * Обрабатывает отпускание кнопки громкости вниз
     */
    fun handleVolumeDownRelease() {
        volumeController.stopVolumeDown()
    }
    
    /**
     * Останавливает все операции с громкостью
     */
    fun stopAllVolumeOperations() {
        volumeController.stopAll()
    }
    
    /**
     * Универсальная функция для получения пакетов приложений для split screen
     * @param keyCode код кнопки
     * @param isLongPress true если это длительное нажатие
     * @return пара (левое приложение, правое приложение) или null если не настроено
     */
    private fun getButtonSplitScreenPackages(keyCode: Int, isLongPress: Boolean): Pair<String?, String?>? {
        val leftAppPackage: String?
        val rightAppPackage: String?
        
        when (keyCode) {
            KeyCode.KEYCODE_R_MEDIA_NEXT -> {
                if (isLongPress) {
                    leftAppPackage = settingsManager.getTrackNextLongPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getTrackNextLongPressSplitRightAppPackage()
                } else {
                    leftAppPackage = settingsManager.getTrackNextShortPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getTrackNextShortPressSplitRightAppPackage()
                }
            }
            KeyCode.KEYCODE_R_MEDIA_PREVIOUS -> {
                if (isLongPress) {
                    leftAppPackage = settingsManager.getTrackPrevLongPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getTrackPrevLongPressSplitRightAppPackage()
                } else {
                    leftAppPackage = settingsManager.getTrackPrevShortPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getTrackPrevShortPressSplitRightAppPackage()
                }
            }
            KeyCode.KEYCODE_R_MEDIA_PLAY_PAUSE -> {
                if (isLongPress) {
                    leftAppPackage = settingsManager.getMediaPlayPauseLongPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getMediaPlayPauseLongPressSplitRightAppPackage()
                } else {
                    leftAppPackage = settingsManager.getMediaPlayPauseShortPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getMediaPlayPauseShortPressSplitRightAppPackage()
                }
            }
            KeyCode.KEYCODE_R_VOLUME_MUTE, IICKeyCodes.KEY_CODE_IIC_MUTE -> {
                if (isLongPress) {
                    leftAppPackage = settingsManager.getMuteLongPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getMuteLongPressSplitRightAppPackage()
                } else {
                    leftAppPackage = settingsManager.getMuteShortPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getMuteShortPressSplitRightAppPackage()
                }
            }
            KeyCode.KEYCODE_R_CALL -> {
                if (isLongPress) {
                    leftAppPackage = settingsManager.getCallLongPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getCallLongPressSplitRightAppPackage()
                } else {
                    leftAppPackage = settingsManager.getCallShortPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getCallShortPressSplitRightAppPackage()
                }
            }
            IICKeyCodes.KEY_CODE_IIC_POWER -> {
                if (isLongPress) {
                    leftAppPackage = settingsManager.getTouchPowerLongPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getTouchPowerLongPressSplitRightAppPackage()
                } else {
                    leftAppPackage = settingsManager.getTouchPowerShortPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getTouchPowerShortPressSplitRightAppPackage()
                }
            }
            KeyCode.KEYCODE_R_SRC -> {
                if (isLongPress) {
                    leftAppPackage = settingsManager.getModeLongPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getModeLongPressSplitRightAppPackage()
                } else {
                    leftAppPackage = settingsManager.getModeShortPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getModeShortPressSplitRightAppPackage()
                }
            }
            KeyCode.KEYCODE_R_HOME -> {
                if (isLongPress) {
                    leftAppPackage = settingsManager.getHomeLongPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getHomeLongPressSplitRightAppPackage()
                } else {
                    leftAppPackage = settingsManager.getHomeShortPressSplitLeftAppPackage()
                    rightAppPackage = settingsManager.getHomeShortPressSplitRightAppPackage()
                }
            }
            else -> return null
        }
        
        return Pair(leftAppPackage, rightAppPackage)
    }
    
    /**
     * Запускает приложения в режиме разделения экрана для конкретной кнопки
     * @param keyCode код кнопки
     * @param isLongPress true если это длительное нажатие
     */
    private fun launchSplitScreenForButton(keyCode: Int, isLongPress: Boolean) {
        // Проверяем поддержку split screen режима
        if (!SplitScreenLauncher.isSplitScreenSupported()) {
            Log.w(TAG, "Split screen mode is not supported on this device")
            return
        }
        
        val packages = getButtonSplitScreenPackages(keyCode, isLongPress)
        
        if (packages == null) {
            Log.w(TAG, "Split screen not supported for key code: $keyCode")
            return
        }
        
        val (leftAppPackage, rightAppPackage) = packages
        
        if (leftAppPackage != null && rightAppPackage != null) {
            val buttonName = getButtonName(keyCode)
            val pressType = if (isLongPress) "long" else "short"
            Log.d(TAG, "Launching split screen for $buttonName $pressType press: $leftAppPackage + $rightAppPackage")
            
            // Используем новый API с улучшенной обработкой ошибок
            val result = SplitScreenLauncher.launchSplitScreenModeByPackage(
                context = context,
                primaryPackageName = leftAppPackage,
                secondaryPackageName = rightAppPackage
            )
            
            if (result.success) {
                Log.d(TAG, "Successfully launched split screen for $buttonName $pressType press")
                Log.d(TAG, "Primary app: ${result.primaryApp}, Secondary app: ${result.secondaryApp}")
            } else {
                Log.e(TAG, "Failed to launch split screen for $buttonName $pressType press: ${result.errorMessage}")
                
                // Дополнительная обработка ошибок
                when {
                    result.errorMessage?.contains("not installed") == true -> {
                        Log.e(TAG, "One or both apps are not installed")
                    }
                    result.errorMessage?.contains("same") == true -> {
                        Log.e(TAG, "Cannot launch the same app in split screen mode")
                    }
                    result.errorMessage?.contains("empty") == true -> {
                        Log.e(TAG, "Package names cannot be empty")
                    }
                    else -> {
                        Log.e(TAG, "Unknown error occurred: ${result.errorMessage}")
                    }
                }
            }
        } else {
            val buttonName = getButtonName(keyCode)
            val pressType = if (isLongPress) "long" else "short"
            Log.w(TAG, "Split screen apps not configured for $buttonName $pressType press")
        }
    }
}
