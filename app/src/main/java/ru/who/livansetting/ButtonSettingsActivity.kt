package ru.who.livansetting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.who.livansetting.R
import ru.who.livansetting.ui.theme.LivanSettingTheme
import ru.who.livansetting.utils.BuildPropUtils

class ButtonSettingsActivity : ComponentActivity() {
    
    companion object {
        const val EXTRA_BUTTON_TYPE = "button_type"
        const val BUTTON_TYPE_MODE = "mode"
        const val BUTTON_TYPE_HOME = "home"
        const val BUTTON_TYPE_TRACK_NEXT = "track_next"
        const val BUTTON_TYPE_TRACK_PREV = "track_prev"
        const val BUTTON_TYPE_MUTE = "mute"
        const val BUTTON_TYPE_CALL = "call"
        const val BUTTON_TYPE_TOUCH_MUTE = "touch_mute"
        const val BUTTON_TYPE_TOUCH_POWER = "touch_power"
        const val BUTTON_TYPE_MEDIA_PLAY_PAUSE = "media_play_pause"
        
        fun createIntent(context: Context, buttonType: String): Intent {
            return Intent(context, ButtonSettingsActivity::class.java).apply {
                putExtra(EXTRA_BUTTON_TYPE, buttonType)
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val buttonType = intent.getStringExtra(EXTRA_BUTTON_TYPE) ?: BUTTON_TYPE_MODE
        
        setContent {
            LivanSettingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ButtonSettingsScreen(
                        buttonType = buttonType,
                        modifier = Modifier.padding(innerPadding),
                        onBackPressed = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun ButtonSettingsScreen(
    buttonType: String,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {}
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    
    // Состояние для настроек кнопки
    var shortPressRemapped by remember { mutableStateOf(false) }
    var longPressRemapped by remember { mutableStateOf(false) }
    var selectedShortPressApp by remember { mutableStateOf<AppInfo?>(null) }
    var selectedLongPressApp by remember { mutableStateOf<AppInfo?>(null) }
    var shortPressActionType by remember { mutableStateOf(ButtonActionType.NOTHING) }
    var longPressActionType by remember { mutableStateOf(ButtonActionType.NOTHING) }
    var shortPressSplitLeftApp by remember { mutableStateOf<AppInfo?>(null) }
    var shortPressSplitRightApp by remember { mutableStateOf<AppInfo?>(null) }
    var longPressSplitLeftApp by remember { mutableStateOf<AppInfo?>(null) }
    var longPressSplitRightApp by remember { mutableStateOf<AppInfo?>(null) }
    
    val apps by remember {
        derivedStateOf { getInstalledApps(context) }
    }
    
    // Определяем заголовок и методы для работы с настройками в зависимости от типа кнопки
    val title = when (buttonType) {
        ButtonSettingsActivity.BUTTON_TYPE_MODE -> stringResource(R.string.mode_button)
        ButtonSettingsActivity.BUTTON_TYPE_HOME -> stringResource(R.string.home_button)
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_NEXT -> stringResource(R.string.track_next_button)
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_PREV -> stringResource(R.string.track_prev_button)
        ButtonSettingsActivity.BUTTON_TYPE_MUTE -> stringResource(R.string.mute_button)
        ButtonSettingsActivity.BUTTON_TYPE_CALL -> stringResource(R.string.call_button)
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_MUTE -> stringResource(R.string.touch_mute_button)
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_POWER -> stringResource(R.string.touch_power_button)
        ButtonSettingsActivity.BUTTON_TYPE_MEDIA_PLAY_PAUSE -> stringResource(R.string.media_play_pause_button)
        else -> throw IllegalArgumentException("Unknown button type: $buttonType")
    }
    
    val loadSettings = when (buttonType) {
        ButtonSettingsActivity.BUTTON_TYPE_MODE -> {
            {
                shortPressRemapped = settingsManager.isModeShortPressRemapped()
                longPressRemapped = settingsManager.isModeLongPressRemapped()
                
                // Загрузка типов действий
                shortPressActionType = settingsManager.getModeShortPressActionType()
                longPressActionType = settingsManager.getModeLongPressActionType()
                
                val shortAppPackage = settingsManager.getModeShortPressAppPackage()
                val shortAppName = settingsManager.getModeShortPressAppName()
                if (shortAppPackage != null && shortAppName != null) {
                    selectedShortPressApp = AppInfo(shortAppPackage, shortAppName, null)
                }
                
                val longAppPackage = settingsManager.getModeLongPressAppPackage()
                val longAppName = settingsManager.getModeLongPressAppName()
                if (longAppPackage != null && longAppName != null) {
                    selectedLongPressApp = AppInfo(longAppPackage, longAppName, null)
                }
                
                // Загрузка split screen настроек для Mode
                val shortSplitLeftPackage = settingsManager.getModeShortPressSplitLeftAppPackage()
                val shortSplitLeftName = settingsManager.getModeShortPressSplitLeftAppName()
                if (shortSplitLeftPackage != null && shortSplitLeftName != null) {
                    shortPressSplitLeftApp = AppInfo(shortSplitLeftPackage, shortSplitLeftName, null)
                }
                
                val shortSplitRightPackage = settingsManager.getModeShortPressSplitRightAppPackage()
                val shortSplitRightName = settingsManager.getModeShortPressSplitRightAppName()
                if (shortSplitRightPackage != null && shortSplitRightName != null) {
                    shortPressSplitRightApp = AppInfo(shortSplitRightPackage, shortSplitRightName, null)
                }
                
                val longSplitLeftPackage = settingsManager.getModeLongPressSplitLeftAppPackage()
                val longSplitLeftName = settingsManager.getModeLongPressSplitLeftAppName()
                if (longSplitLeftPackage != null && longSplitLeftName != null) {
                    longPressSplitLeftApp = AppInfo(longSplitLeftPackage, longSplitLeftName, null)
                }
                
                val longSplitRightPackage = settingsManager.getModeLongPressSplitRightAppPackage()
                val longSplitRightName = settingsManager.getModeLongPressSplitRightAppName()
                if (longSplitRightPackage != null && longSplitRightName != null) {
                    longPressSplitRightApp = AppInfo(longSplitRightPackage, longSplitRightName, null)
                }
            }
        }
        ButtonSettingsActivity.BUTTON_TYPE_HOME -> {
            {
                shortPressRemapped = settingsManager.isHomeShortPressRemapped()
                longPressRemapped = settingsManager.isHomeLongPressRemapped()
                
                // Загрузка типов действий
                shortPressActionType = settingsManager.getHomeShortPressActionType()
                longPressActionType = settingsManager.getHomeLongPressActionType()
                
                val shortAppPackage = settingsManager.getHomeShortPressAppPackage()
                val shortAppName = settingsManager.getHomeShortPressAppName()
                if (shortAppPackage != null && shortAppName != null) {
                    selectedShortPressApp = AppInfo(shortAppPackage, shortAppName, null)
                }
                
                val longAppPackage = settingsManager.getHomeLongPressAppPackage()
                val longAppName = settingsManager.getHomeLongPressAppName()
                if (longAppPackage != null && longAppName != null) {
                    selectedLongPressApp = AppInfo(longAppPackage, longAppName, null)
                }
                
                // Загрузка split screen настроек для Home
                val shortSplitLeftPackage = settingsManager.getHomeShortPressSplitLeftAppPackage()
                val shortSplitLeftName = settingsManager.getHomeShortPressSplitLeftAppName()
                if (shortSplitLeftPackage != null && shortSplitLeftName != null) {
                    shortPressSplitLeftApp = AppInfo(shortSplitLeftPackage, shortSplitLeftName, null)
                }
                
                val shortSplitRightPackage = settingsManager.getHomeShortPressSplitRightAppPackage()
                val shortSplitRightName = settingsManager.getHomeShortPressSplitRightAppName()
                if (shortSplitRightPackage != null && shortSplitRightName != null) {
                    shortPressSplitRightApp = AppInfo(shortSplitRightPackage, shortSplitRightName, null)
                }
                
                val longSplitLeftPackage = settingsManager.getHomeLongPressSplitLeftAppPackage()
                val longSplitLeftName = settingsManager.getHomeLongPressSplitLeftAppName()
                if (longSplitLeftPackage != null && longSplitLeftName != null) {
                    longPressSplitLeftApp = AppInfo(longSplitLeftPackage, longSplitLeftName, null)
                }
                
                val longSplitRightPackage = settingsManager.getHomeLongPressSplitRightAppPackage()
                val longSplitRightName = settingsManager.getHomeLongPressSplitRightAppName()
                if (longSplitRightPackage != null && longSplitRightName != null) {
                    longPressSplitRightApp = AppInfo(longSplitRightPackage, longSplitRightName, null)
                }
            }
        }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_NEXT -> {
            {
                shortPressRemapped = settingsManager.isTrackNextShortPressRemapped()
                longPressRemapped = settingsManager.isTrackNextLongPressRemapped()
                
                // Загрузка типов действий
                shortPressActionType = settingsManager.getTrackNextShortPressActionType()
                longPressActionType = settingsManager.getTrackNextLongPressActionType()
                
                val shortAppPackage = settingsManager.getTrackNextShortPressAppPackage()
                val shortAppName = settingsManager.getTrackNextShortPressAppName()
                if (shortAppPackage != null && shortAppName != null) {
                    selectedShortPressApp = AppInfo(shortAppPackage, shortAppName, null)
                }
                
                val longAppPackage = settingsManager.getTrackNextLongPressAppPackage()
                val longAppName = settingsManager.getTrackNextLongPressAppName()
                if (longAppPackage != null && longAppName != null) {
                    selectedLongPressApp = AppInfo(longAppPackage, longAppName, null)
                }
                
                // Загрузка split screen настроек для Track Next
                val shortSplitLeftPackage = settingsManager.getTrackNextShortPressSplitLeftAppPackage()
                val shortSplitLeftName = settingsManager.getTrackNextShortPressSplitLeftAppName()
                if (shortSplitLeftPackage != null && shortSplitLeftName != null) {
                    shortPressSplitLeftApp = AppInfo(shortSplitLeftPackage, shortSplitLeftName, null)
                }
                
                val shortSplitRightPackage = settingsManager.getTrackNextShortPressSplitRightAppPackage()
                val shortSplitRightName = settingsManager.getTrackNextShortPressSplitRightAppName()
                if (shortSplitRightPackage != null && shortSplitRightName != null) {
                    shortPressSplitRightApp = AppInfo(shortSplitRightPackage, shortSplitRightName, null)
                }
                
                val longSplitLeftPackage = settingsManager.getTrackNextLongPressSplitLeftAppPackage()
                val longSplitLeftName = settingsManager.getTrackNextLongPressSplitLeftAppName()
                if (longSplitLeftPackage != null && longSplitLeftName != null) {
                    longPressSplitLeftApp = AppInfo(longSplitLeftPackage, longSplitLeftName, null)
                }
                
                val longSplitRightPackage = settingsManager.getTrackNextLongPressSplitRightAppPackage()
                val longSplitRightName = settingsManager.getTrackNextLongPressSplitRightAppName()
                if (longSplitRightPackage != null && longSplitRightName != null) {
                    longPressSplitRightApp = AppInfo(longSplitRightPackage, longSplitRightName, null)
                }
            }
        }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_PREV -> {
            {
                shortPressRemapped = settingsManager.isTrackPrevShortPressRemapped()
                longPressRemapped = settingsManager.isTrackPrevLongPressRemapped()
                
                // Загрузка типов действий
                shortPressActionType = settingsManager.getTrackPrevShortPressActionType()
                longPressActionType = settingsManager.getTrackPrevLongPressActionType()
                
                val shortAppPackage = settingsManager.getTrackPrevShortPressAppPackage()
                val shortAppName = settingsManager.getTrackPrevShortPressAppName()
                if (shortAppPackage != null && shortAppName != null) {
                    selectedShortPressApp = AppInfo(shortAppPackage, shortAppName, null)
                }
                
                val longAppPackage = settingsManager.getTrackPrevLongPressAppPackage()
                val longAppName = settingsManager.getTrackPrevLongPressAppName()
                if (longAppPackage != null && longAppName != null) {
                    selectedLongPressApp = AppInfo(longAppPackage, longAppName, null)
                }
                
                // Загрузка split screen настроек для Track Prev
                val shortSplitLeftPackage = settingsManager.getTrackPrevShortPressSplitLeftAppPackage()
                val shortSplitLeftName = settingsManager.getTrackPrevShortPressSplitLeftAppName()
                if (shortSplitLeftPackage != null && shortSplitLeftName != null) {
                    shortPressSplitLeftApp = AppInfo(shortSplitLeftPackage, shortSplitLeftName, null)
                }
                
                val shortSplitRightPackage = settingsManager.getTrackPrevShortPressSplitRightAppPackage()
                val shortSplitRightName = settingsManager.getTrackPrevShortPressSplitRightAppName()
                if (shortSplitRightPackage != null && shortSplitRightName != null) {
                    shortPressSplitRightApp = AppInfo(shortSplitRightPackage, shortSplitRightName, null)
                }
                
                val longSplitLeftPackage = settingsManager.getTrackPrevLongPressSplitLeftAppPackage()
                val longSplitLeftName = settingsManager.getTrackPrevLongPressSplitLeftAppName()
                if (longSplitLeftPackage != null && longSplitLeftName != null) {
                    longPressSplitLeftApp = AppInfo(longSplitLeftPackage, longSplitLeftName, null)
                }
                
                val longSplitRightPackage = settingsManager.getTrackPrevLongPressSplitRightAppPackage()
                val longSplitRightName = settingsManager.getTrackPrevLongPressSplitRightAppName()
                if (longSplitRightPackage != null && longSplitRightName != null) {
                    longPressSplitRightApp = AppInfo(longSplitRightPackage, longSplitRightName, null)
                }
            }
        }
        ButtonSettingsActivity.BUTTON_TYPE_MUTE -> {
            {
                shortPressRemapped = settingsManager.isMuteShortPressRemapped()
                longPressRemapped = settingsManager.isMuteLongPressRemapped()
                
                // Загрузка типов действий
                shortPressActionType = settingsManager.getMuteShortPressActionType()
                longPressActionType = settingsManager.getMuteLongPressActionType()
                
                val shortAppPackage = settingsManager.getMuteShortPressAppPackage()
                val shortAppName = settingsManager.getMuteShortPressAppName()
                if (shortAppPackage != null && shortAppName != null) {
                    selectedShortPressApp = AppInfo(shortAppPackage, shortAppName, null)
                }
                
                val longAppPackage = settingsManager.getMuteLongPressAppPackage()
                val longAppName = settingsManager.getMuteLongPressAppName()
                if (longAppPackage != null && longAppName != null) {
                    selectedLongPressApp = AppInfo(longAppPackage, longAppName, null)
                }
                
                // Загрузка split screen настроек для Mute
                val shortSplitLeftPackage = settingsManager.getMuteShortPressSplitLeftAppPackage()
                val shortSplitLeftName = settingsManager.getMuteShortPressSplitLeftAppName()
                if (shortSplitLeftPackage != null && shortSplitLeftName != null) {
                    shortPressSplitLeftApp = AppInfo(shortSplitLeftPackage, shortSplitLeftName, null)
                }
                
                val shortSplitRightPackage = settingsManager.getMuteShortPressSplitRightAppPackage()
                val shortSplitRightName = settingsManager.getMuteShortPressSplitRightAppName()
                if (shortSplitRightPackage != null && shortSplitRightName != null) {
                    shortPressSplitRightApp = AppInfo(shortSplitRightPackage, shortSplitRightName, null)
                }
                
                val longSplitLeftPackage = settingsManager.getMuteLongPressSplitLeftAppPackage()
                val longSplitLeftName = settingsManager.getMuteLongPressSplitLeftAppName()
                if (longSplitLeftPackage != null && longSplitLeftName != null) {
                    longPressSplitLeftApp = AppInfo(longSplitLeftPackage, longSplitLeftName, null)
                }
                
                val longSplitRightPackage = settingsManager.getMuteLongPressSplitRightAppPackage()
                val longSplitRightName = settingsManager.getMuteLongPressSplitRightAppName()
                if (longSplitRightPackage != null && longSplitRightName != null) {
                    longPressSplitRightApp = AppInfo(longSplitRightPackage, longSplitRightName, null)
                }
            }
        }
        ButtonSettingsActivity.BUTTON_TYPE_CALL -> {
            {
                shortPressRemapped = settingsManager.isCallShortPressRemapped()
                longPressRemapped = settingsManager.isCallLongPressRemapped()
                
                // Загрузка типов действий
                shortPressActionType = settingsManager.getCallShortPressActionType()
                longPressActionType = settingsManager.getCallLongPressActionType()
                
                val shortAppPackage = settingsManager.getCallShortPressAppPackage()
                val shortAppName = settingsManager.getCallShortPressAppName()
                if (shortAppPackage != null && shortAppName != null) {
                    selectedShortPressApp = AppInfo(shortAppPackage, shortAppName, null)
                }
                
                val longAppPackage = settingsManager.getCallLongPressAppPackage()
                val longAppName = settingsManager.getCallLongPressAppName()
                if (longAppPackage != null && longAppName != null) {
                    selectedLongPressApp = AppInfo(longAppPackage, longAppName, null)
                }
                
                // Загрузка split screen настроек для Call
                val shortSplitLeftPackage = settingsManager.getCallShortPressSplitLeftAppPackage()
                val shortSplitLeftName = settingsManager.getCallShortPressSplitLeftAppName()
                if (shortSplitLeftPackage != null && shortSplitLeftName != null) {
                    shortPressSplitLeftApp = AppInfo(shortSplitLeftPackage, shortSplitLeftName, null)
                }
                
                val shortSplitRightPackage = settingsManager.getCallShortPressSplitRightAppPackage()
                val shortSplitRightName = settingsManager.getCallShortPressSplitRightAppName()
                if (shortSplitRightPackage != null && shortSplitRightName != null) {
                    shortPressSplitRightApp = AppInfo(shortSplitRightPackage, shortSplitRightName, null)
                }
                
                val longSplitLeftPackage = settingsManager.getCallLongPressSplitLeftAppPackage()
                val longSplitLeftName = settingsManager.getCallLongPressSplitLeftAppName()
                if (longSplitLeftPackage != null && longSplitLeftName != null) {
                    longPressSplitLeftApp = AppInfo(longSplitLeftPackage, longSplitLeftName, null)
                }
                
                val longSplitRightPackage = settingsManager.getCallLongPressSplitRightAppPackage()
                val longSplitRightName = settingsManager.getCallLongPressSplitRightAppName()
                if (longSplitRightPackage != null && longSplitRightName != null) {
                    longPressSplitRightApp = AppInfo(longSplitRightPackage, longSplitRightName, null)
                }
            }
        }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_MUTE -> {
            {
                shortPressRemapped = settingsManager.isTouchMuteShortPressRemapped()
                // Не загружаем настройки длинных нажатий для TOUCH_MUTE
                longPressRemapped = false
                
                // Загрузка типов действий
                shortPressActionType = settingsManager.getTouchMuteShortPressActionType()
                longPressActionType = ButtonActionType.NOTHING
                
                val shortAppPackage = settingsManager.getTouchMuteShortPressAppPackage()
                val shortAppName = settingsManager.getTouchMuteShortPressAppName()
                if (shortAppPackage != null && shortAppName != null) {
                    selectedShortPressApp = AppInfo(shortAppPackage, shortAppName, null)
                }
                
                // Не загружаем приложения для длинных нажатий
                selectedLongPressApp = null
                
                // Загрузка split screen настроек для Touch Mute (только короткие нажатия)
                val shortSplitLeftPackage = settingsManager.getTouchMuteShortPressSplitLeftAppPackage()
                val shortSplitLeftName = settingsManager.getTouchMuteShortPressSplitLeftAppName()
                if (shortSplitLeftPackage != null && shortSplitLeftName != null) {
                    shortPressSplitLeftApp = AppInfo(shortSplitLeftPackage, shortSplitLeftName, null)
                }
                
                val shortSplitRightPackage = settingsManager.getTouchMuteShortPressSplitRightAppPackage()
                val shortSplitRightName = settingsManager.getTouchMuteShortPressSplitRightAppName()
                if (shortSplitRightPackage != null && shortSplitRightName != null) {
                    shortPressSplitRightApp = AppInfo(shortSplitRightPackage, shortSplitRightName, null)
                }
                
                // Не загружаем split screen настройки для длинных нажатий
                longPressSplitLeftApp = null
                longPressSplitRightApp = null
            }
        }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_POWER -> {
            {
                shortPressRemapped = settingsManager.isTouchPowerShortPressRemapped()
                // Не загружаем настройки длинных нажатий для TOUCH_POWER
                longPressRemapped = false
                
                // Загрузка типов действий
                shortPressActionType = settingsManager.getTouchPowerShortPressActionType()
                longPressActionType = ButtonActionType.NOTHING
                
                val shortAppPackage = settingsManager.getTouchPowerShortPressAppPackage()
                val shortAppName = settingsManager.getTouchPowerShortPressAppName()
                if (shortAppPackage != null && shortAppName != null) {
                    selectedShortPressApp = AppInfo(shortAppPackage, shortAppName, null)
                }
                
                // Не загружаем приложения для длинных нажатий
                selectedLongPressApp = null
                
                // Загрузка split screen настроек для Touch Power (только короткие нажатия)
                val shortSplitLeftPackage = settingsManager.getTouchPowerShortPressSplitLeftAppPackage()
                val shortSplitLeftName = settingsManager.getTouchPowerShortPressSplitLeftAppName()
                if (shortSplitLeftPackage != null && shortSplitLeftName != null) {
                    shortPressSplitLeftApp = AppInfo(shortSplitLeftPackage, shortSplitLeftName, null)
                }
                
                val shortSplitRightPackage = settingsManager.getTouchPowerShortPressSplitRightAppPackage()
                val shortSplitRightName = settingsManager.getTouchPowerShortPressSplitRightAppName()
                if (shortSplitRightPackage != null && shortSplitRightName != null) {
                    shortPressSplitRightApp = AppInfo(shortSplitRightPackage, shortSplitRightName, null)
                }
                
                // Не загружаем split screen настройки для длинных нажатий
                longPressSplitLeftApp = null
                longPressSplitRightApp = null
            }
        }
        ButtonSettingsActivity.BUTTON_TYPE_MEDIA_PLAY_PAUSE -> {
            {
                shortPressRemapped = settingsManager.isMediaPlayPauseShortPressRemapped()
                longPressRemapped = settingsManager.isMediaPlayPauseLongPressRemapped()
                
                // Загрузка типов действий
                shortPressActionType = settingsManager.getMediaPlayPauseShortPressActionType()
                longPressActionType = settingsManager.getMediaPlayPauseLongPressActionType()
                
                val shortAppPackage = settingsManager.getMediaPlayPauseShortPressAppPackage()
                val shortAppName = settingsManager.getMediaPlayPauseShortPressAppName()
                if (shortAppPackage != null && shortAppName != null) {
                    selectedShortPressApp = AppInfo(shortAppPackage, shortAppName, null)
                }
                
                val longAppPackage = settingsManager.getMediaPlayPauseLongPressAppPackage()
                val longAppName = settingsManager.getMediaPlayPauseLongPressAppName()
                if (longAppPackage != null && longAppName != null) {
                    selectedLongPressApp = AppInfo(longAppPackage, longAppName, null)
                }
                
                // Загрузка split screen настроек для Media Play Pause
                val shortSplitLeftPackage = settingsManager.getMediaPlayPauseShortPressSplitLeftAppPackage()
                val shortSplitLeftName = settingsManager.getMediaPlayPauseShortPressSplitLeftAppName()
                if (shortSplitLeftPackage != null && shortSplitLeftName != null) {
                    shortPressSplitLeftApp = AppInfo(shortSplitLeftPackage, shortSplitLeftName, null)
                }
                
                val shortSplitRightPackage = settingsManager.getMediaPlayPauseShortPressSplitRightAppPackage()
                val shortSplitRightName = settingsManager.getMediaPlayPauseShortPressSplitRightAppName()
                if (shortSplitRightPackage != null && shortSplitRightName != null) {
                    shortPressSplitRightApp = AppInfo(shortSplitRightPackage, shortSplitRightName, null)
                }
                
                val longSplitLeftPackage = settingsManager.getMediaPlayPauseLongPressSplitLeftAppPackage()
                val longSplitLeftName = settingsManager.getMediaPlayPauseLongPressSplitLeftAppName()
                if (longSplitLeftPackage != null && longSplitLeftName != null) {
                    longPressSplitLeftApp = AppInfo(longSplitLeftPackage, longSplitLeftName, null)
                }
                
                val longSplitRightPackage = settingsManager.getMediaPlayPauseLongPressSplitRightAppPackage()
                val longSplitRightName = settingsManager.getMediaPlayPauseLongPressSplitRightAppName()
                if (longSplitRightPackage != null && longSplitRightName != null) {
                    longPressSplitRightApp = AppInfo(longSplitRightPackage, longSplitRightName, null)
                }
            }
        }
        else -> throw IllegalArgumentException("Unknown button type: $buttonType")
    }
    
    val saveShortPressRemapped = when (buttonType) {
        ButtonSettingsActivity.BUTTON_TYPE_MODE -> { remapped: Boolean -> settingsManager.setModeShortPressRemapped(remapped) }
        ButtonSettingsActivity.BUTTON_TYPE_HOME -> { remapped: Boolean -> settingsManager.setHomeShortPressRemapped(remapped) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_NEXT -> { remapped: Boolean -> settingsManager.setTrackNextShortPressRemapped(remapped) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_PREV -> { remapped: Boolean -> settingsManager.setTrackPrevShortPressRemapped(remapped) }
        ButtonSettingsActivity.BUTTON_TYPE_MUTE -> { remapped: Boolean -> settingsManager.setMuteShortPressRemapped(remapped) }
        ButtonSettingsActivity.BUTTON_TYPE_CALL -> { remapped: Boolean -> settingsManager.setCallShortPressRemapped(remapped) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_MUTE -> { remapped: Boolean -> settingsManager.setTouchMuteShortPressRemapped(remapped) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_POWER -> { remapped: Boolean -> settingsManager.setTouchPowerShortPressRemapped(remapped) }
        ButtonSettingsActivity.BUTTON_TYPE_MEDIA_PLAY_PAUSE -> { remapped: Boolean -> settingsManager.setMediaPlayPauseShortPressRemapped(remapped) }
        else -> throw IllegalArgumentException("Unknown button type: $buttonType")
    }
    
    val saveLongPressRemapped = when (buttonType) {
        ButtonSettingsActivity.BUTTON_TYPE_MODE -> { remapped: Boolean -> settingsManager.setModeLongPressRemapped(remapped) }
        ButtonSettingsActivity.BUTTON_TYPE_HOME -> { remapped: Boolean -> settingsManager.setHomeLongPressRemapped(remapped) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_NEXT -> { remapped: Boolean -> settingsManager.setTrackNextLongPressRemapped(remapped) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_PREV -> { remapped: Boolean -> settingsManager.setTrackPrevLongPressRemapped(remapped) }
        ButtonSettingsActivity.BUTTON_TYPE_MUTE -> { remapped: Boolean -> settingsManager.setMuteLongPressRemapped(remapped) }
        ButtonSettingsActivity.BUTTON_TYPE_CALL -> { remapped: Boolean -> settingsManager.setCallLongPressRemapped(remapped) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_MUTE -> { remapped: Boolean -> { /* Не сохраняем настройки длинных нажатий для TOUCH_MUTE */ } }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_POWER -> { remapped: Boolean -> { /* Не сохраняем настройки длинных нажатий для TOUCH_POWER */ } }
        ButtonSettingsActivity.BUTTON_TYPE_MEDIA_PLAY_PAUSE -> { remapped: Boolean -> settingsManager.setMediaPlayPauseLongPressRemapped(remapped) }
        else -> throw IllegalArgumentException("Unknown button type: $buttonType")
    }
    
    val saveShortPressApp = when (buttonType) {
        ButtonSettingsActivity.BUTTON_TYPE_MODE -> { app: AppInfo? -> settingsManager.setModeShortPressApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_HOME -> { app: AppInfo? -> settingsManager.setHomeShortPressApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_NEXT -> { app: AppInfo? -> settingsManager.setTrackNextShortPressApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_PREV -> { app: AppInfo? -> settingsManager.setTrackPrevShortPressApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_MUTE -> { app: AppInfo? -> settingsManager.setMuteShortPressApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_CALL -> { app: AppInfo? -> settingsManager.setCallShortPressApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_MUTE -> { app: AppInfo? -> settingsManager.setTouchMuteShortPressApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_POWER -> { app: AppInfo? -> settingsManager.setTouchPowerShortPressApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_MEDIA_PLAY_PAUSE -> { app: AppInfo? -> settingsManager.setMediaPlayPauseShortPressApp(app) }
        else -> throw IllegalArgumentException("Unknown button type: $buttonType")
    }
    
    val saveLongPressApp = when (buttonType) {
        ButtonSettingsActivity.BUTTON_TYPE_MODE -> { app: AppInfo? -> settingsManager.setModeLongPressApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_HOME -> { app: AppInfo? -> settingsManager.setHomeLongPressApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_NEXT -> { app: AppInfo? -> settingsManager.setTrackNextLongPressApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_PREV -> { app: AppInfo? -> settingsManager.setTrackPrevLongPressApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_MUTE -> { app: AppInfo? -> settingsManager.setMuteLongPressApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_CALL -> { app: AppInfo? -> settingsManager.setCallLongPressApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_MUTE -> { app: AppInfo? -> { /* Не сохраняем настройки длинных нажатий для TOUCH_MUTE */ } }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_POWER -> { app: AppInfo? -> { /* Не сохраняем настройки длинных нажатий для TOUCH_POWER */ } }
        ButtonSettingsActivity.BUTTON_TYPE_MEDIA_PLAY_PAUSE -> { app: AppInfo? -> settingsManager.setMediaPlayPauseLongPressApp(app) }
        else -> throw IllegalArgumentException("Unknown button type: $buttonType")
    }
    
    val saveShortPressActionType = when (buttonType) {
        ButtonSettingsActivity.BUTTON_TYPE_MODE -> { actionType: ButtonActionType -> settingsManager.setModeShortPressActionType(actionType) }
        ButtonSettingsActivity.BUTTON_TYPE_HOME -> { actionType: ButtonActionType -> settingsManager.setHomeShortPressActionType(actionType) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_NEXT -> { actionType: ButtonActionType -> settingsManager.setTrackNextShortPressActionType(actionType) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_PREV -> { actionType: ButtonActionType -> settingsManager.setTrackPrevShortPressActionType(actionType) }
        ButtonSettingsActivity.BUTTON_TYPE_MUTE -> { actionType: ButtonActionType -> settingsManager.setMuteShortPressActionType(actionType) }
        ButtonSettingsActivity.BUTTON_TYPE_CALL -> { actionType: ButtonActionType -> settingsManager.setCallShortPressActionType(actionType) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_MUTE -> { actionType: ButtonActionType -> settingsManager.setTouchMuteShortPressActionType(actionType) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_POWER -> { actionType: ButtonActionType -> settingsManager.setTouchPowerShortPressActionType(actionType) }
        ButtonSettingsActivity.BUTTON_TYPE_MEDIA_PLAY_PAUSE -> { actionType: ButtonActionType -> settingsManager.setMediaPlayPauseShortPressActionType(actionType) }
        else -> throw IllegalArgumentException("Unknown button type: $buttonType")
    }
    
    val saveLongPressActionType = when (buttonType) {
        ButtonSettingsActivity.BUTTON_TYPE_MODE -> { actionType: ButtonActionType -> settingsManager.setModeLongPressActionType(actionType) }
        ButtonSettingsActivity.BUTTON_TYPE_HOME -> { actionType: ButtonActionType -> settingsManager.setHomeLongPressActionType(actionType) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_NEXT -> { actionType: ButtonActionType -> settingsManager.setTrackNextLongPressActionType(actionType) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_PREV -> { actionType: ButtonActionType -> settingsManager.setTrackPrevLongPressActionType(actionType) }
        ButtonSettingsActivity.BUTTON_TYPE_MUTE -> { actionType: ButtonActionType -> settingsManager.setMuteLongPressActionType(actionType) }
        ButtonSettingsActivity.BUTTON_TYPE_CALL -> { actionType: ButtonActionType -> settingsManager.setCallLongPressActionType(actionType) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_MUTE -> { actionType: ButtonActionType -> { /* Не сохраняем настройки длинных нажатий для TOUCH_MUTE */ } }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_POWER -> { actionType: ButtonActionType -> { /* Не сохраняем настройки длинных нажатий для TOUCH_POWER */ } }
        ButtonSettingsActivity.BUTTON_TYPE_MEDIA_PLAY_PAUSE -> { actionType: ButtonActionType -> settingsManager.setMediaPlayPauseLongPressActionType(actionType) }
        else -> throw IllegalArgumentException("Unknown button type: $buttonType")
    }
    
    // Функции сохранения split screen настроек
    val saveShortPressSplitLeftApp = when (buttonType) {
        ButtonSettingsActivity.BUTTON_TYPE_MODE -> { app: AppInfo? -> settingsManager.setModeShortPressSplitLeftApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_HOME -> { app: AppInfo? -> settingsManager.setHomeShortPressSplitLeftApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_NEXT -> { app: AppInfo? -> settingsManager.setTrackNextShortPressSplitLeftApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_PREV -> { app: AppInfo? -> settingsManager.setTrackPrevShortPressSplitLeftApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_MUTE -> { app: AppInfo? -> settingsManager.setMuteShortPressSplitLeftApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_CALL -> { app: AppInfo? -> settingsManager.setCallShortPressSplitLeftApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_MUTE -> { app: AppInfo? -> settingsManager.setTouchMuteShortPressSplitLeftApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_POWER -> { app: AppInfo? -> settingsManager.setTouchPowerShortPressSplitLeftApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_MEDIA_PLAY_PAUSE -> { app: AppInfo? -> settingsManager.setMediaPlayPauseShortPressSplitLeftApp(app) }
        else -> throw IllegalArgumentException("Unknown button type: $buttonType")
    }
    
    val saveShortPressSplitRightApp = when (buttonType) {
        ButtonSettingsActivity.BUTTON_TYPE_MODE -> { app: AppInfo? -> settingsManager.setModeShortPressSplitRightApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_HOME -> { app: AppInfo? -> settingsManager.setHomeShortPressSplitRightApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_NEXT -> { app: AppInfo? -> settingsManager.setTrackNextShortPressSplitRightApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_PREV -> { app: AppInfo? -> settingsManager.setTrackPrevShortPressSplitRightApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_MUTE -> { app: AppInfo? -> settingsManager.setMuteShortPressSplitRightApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_CALL -> { app: AppInfo? -> settingsManager.setCallShortPressSplitRightApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_MUTE -> { app: AppInfo? -> settingsManager.setTouchMuteShortPressSplitRightApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_POWER -> { app: AppInfo? -> settingsManager.setTouchPowerShortPressSplitRightApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_MEDIA_PLAY_PAUSE -> { app: AppInfo? -> settingsManager.setMediaPlayPauseShortPressSplitRightApp(app) }
        else -> throw IllegalArgumentException("Unknown button type: $buttonType")
    }
    
    val saveLongPressSplitLeftApp = when (buttonType) {
        ButtonSettingsActivity.BUTTON_TYPE_MODE -> { app: AppInfo? -> settingsManager.setModeLongPressSplitLeftApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_HOME -> { app: AppInfo? -> settingsManager.setHomeLongPressSplitLeftApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_NEXT -> { app: AppInfo? -> settingsManager.setTrackNextLongPressSplitLeftApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_PREV -> { app: AppInfo? -> settingsManager.setTrackPrevLongPressSplitLeftApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_MUTE -> { app: AppInfo? -> settingsManager.setMuteLongPressSplitLeftApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_CALL -> { app: AppInfo? -> settingsManager.setCallLongPressSplitLeftApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_MUTE -> { app: AppInfo? -> { /* Не сохраняем настройки длинных нажатий для TOUCH_MUTE */ } }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_POWER -> { app: AppInfo? -> { /* Не сохраняем настройки длинных нажатий для TOUCH_POWER */ } }
        ButtonSettingsActivity.BUTTON_TYPE_MEDIA_PLAY_PAUSE -> { app: AppInfo? -> settingsManager.setMediaPlayPauseLongPressSplitLeftApp(app) }
        else -> throw IllegalArgumentException("Unknown button type: $buttonType")
    }
    
    val saveLongPressSplitRightApp = when (buttonType) {
        ButtonSettingsActivity.BUTTON_TYPE_MODE -> { app: AppInfo? -> settingsManager.setModeLongPressSplitRightApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_HOME -> { app: AppInfo? -> settingsManager.setHomeLongPressSplitRightApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_NEXT -> { app: AppInfo? -> settingsManager.setTrackNextLongPressSplitRightApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TRACK_PREV -> { app: AppInfo? -> settingsManager.setTrackPrevLongPressSplitRightApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_MUTE -> { app: AppInfo? -> settingsManager.setMuteLongPressSplitRightApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_CALL -> { app: AppInfo? -> settingsManager.setCallLongPressSplitRightApp(app) }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_MUTE -> { app: AppInfo? -> { /* Не сохраняем настройки длинных нажатий для TOUCH_MUTE */ } }
        ButtonSettingsActivity.BUTTON_TYPE_TOUCH_POWER -> { app: AppInfo? -> { /* Не сохраняем настройки длинных нажатий для TOUCH_POWER */ } }
        ButtonSettingsActivity.BUTTON_TYPE_MEDIA_PLAY_PAUSE -> { app: AppInfo? -> settingsManager.setMediaPlayPauseLongPressSplitRightApp(app) }
        else -> throw IllegalArgumentException("Unknown button type: $buttonType")
    }
    
    // Загрузка сохраненных настроек при инициализации
    LaunchedEffect(Unit) {
        loadSettings()
        
        // Проверяем и сбрасываем TOGGLE_DRL если оно должно быть скрыто
        if (BuildPropUtils.shouldHideDrlAction()) {
            if (shortPressActionType == ButtonActionType.TOGGLE_DRL) {
                shortPressActionType = ButtonActionType.DEFAULT_ACTION
                saveShortPressActionType(ButtonActionType.DEFAULT_ACTION)
            }
            if (longPressActionType == ButtonActionType.TOGGLE_DRL) {
                longPressActionType = ButtonActionType.DEFAULT_ACTION
                saveLongPressActionType(ButtonActionType.DEFAULT_ACTION)
            }
        }
    }
    
    // Функция для обновления иконок сохраненных приложений
    LaunchedEffect(selectedShortPressApp, selectedLongPressApp) {
        listOf(selectedShortPressApp, selectedLongPressApp).forEach { app ->
            app?.let { appInfo ->
                if (appInfo.icon == null) {
                    try {
                        val packageManager = context.packageManager
                        val androidAppInfo = packageManager.getApplicationInfo(appInfo.packageName, 0)
                        val icon = packageManager.getApplicationIcon(androidAppInfo)
                        // Обновляем соответствующее состояние
                        when (app) {
                            selectedShortPressApp -> selectedShortPressApp = appInfo.copy(icon = icon)
                            selectedLongPressApp -> selectedLongPressApp = appInfo.copy(icon = icon)
                        }
                    } catch (e: Exception) {
                        // Приложение могло быть удалено
                    }
                }
            }
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Заголовок с кнопкой назад
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onBackPressed
            ) {
                Text(
                    text = stringResource(R.string.back),
                    fontSize = 16.sp
                )
            }
            
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            
            // Пустое место для центрирования заголовка
            Spacer(modifier = Modifier.width(60.dp))
        }
        
        // Настройки кнопки
        ButtonSubmenu(
            title = title,
            shortPressRemapped = shortPressRemapped,
            longPressRemapped = longPressRemapped,
            shortPressActionType = shortPressActionType,
            longPressActionType = longPressActionType,
            shortPressApp = selectedShortPressApp,
            longPressApp = selectedLongPressApp,
            shortPressSplitLeftApp = shortPressSplitLeftApp,
            shortPressSplitRightApp = shortPressSplitRightApp,
            longPressSplitLeftApp = longPressSplitLeftApp,
            longPressSplitRightApp = longPressSplitRightApp,
            apps = apps,
            onShortPressRemappedChange = { remapped ->
                shortPressRemapped = remapped
                saveShortPressRemapped(remapped)
            },
            onLongPressRemappedChange = { remapped ->
                longPressRemapped = remapped
                saveLongPressRemapped(remapped)
            },
            onShortPressActionTypeChange = { actionType ->
                shortPressActionType = actionType
                saveShortPressActionType(actionType)
            },
            onLongPressActionTypeChange = { actionType ->
                longPressActionType = actionType
                saveLongPressActionType(actionType)
            },
            onShortPressAppSelected = { app ->
                selectedShortPressApp = app
                saveShortPressApp(app)
            },
            onLongPressAppSelected = { app ->
                selectedLongPressApp = app
                saveLongPressApp(app)
            },
            onShortPressSplitLeftAppSelected = { app ->
                shortPressSplitLeftApp = app
                saveShortPressSplitLeftApp(app)
            },
            onShortPressSplitRightAppSelected = { app ->
                shortPressSplitRightApp = app
                saveShortPressSplitRightApp(app)
            },
            onLongPressSplitLeftAppSelected = { app ->
                longPressSplitLeftApp = app
                saveLongPressSplitLeftApp(app)
            },
            onLongPressSplitRightAppSelected = { app ->
                longPressSplitRightApp = app
                saveLongPressSplitRightApp(app)
            },
            showLongPressSettings = buttonType != ButtonSettingsActivity.BUTTON_TYPE_TOUCH_MUTE && 
                                   buttonType != ButtonSettingsActivity.BUTTON_TYPE_TOUCH_POWER
        )
    }
}
