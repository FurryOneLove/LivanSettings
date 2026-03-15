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
import ru.who.livansetting.ui.theme.LivanSettingTheme
import ru.who.livansetting.utils.BuildPropUtils

class ButtonSettingsActivity : ComponentActivity() {
    
    companion object {
        const val EXTRA_BUTTON_TYPE = "button_type"
        const val BUTTON_TYPE_MODE = SettingsManager.BTN_MODE
        const val BUTTON_TYPE_HOME = SettingsManager.BTN_HOME
        const val BUTTON_TYPE_TRACK_NEXT = SettingsManager.BTN_TRACK_NEXT
        const val BUTTON_TYPE_TRACK_PREV = SettingsManager.BTN_TRACK_PREV
        const val BUTTON_TYPE_MUTE = SettingsManager.BTN_MUTE
        const val BUTTON_TYPE_CALL = SettingsManager.BTN_CALL
        const val BUTTON_TYPE_TOUCH_MUTE = SettingsManager.BTN_TOUCH_MUTE
        const val BUTTON_TYPE_TOUCH_POWER = SettingsManager.BTN_TOUCH_POWER
        const val BUTTON_TYPE_MEDIA_PLAY_PAUSE = SettingsManager.BTN_MEDIA_PLAY_PAUSE
        
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
    
    val title = when (buttonType) {
        SettingsManager.BTN_MODE -> stringResource(R.string.mode_button)
        SettingsManager.BTN_HOME -> stringResource(R.string.home_button)
        SettingsManager.BTN_TRACK_NEXT -> stringResource(R.string.track_next_button)
        SettingsManager.BTN_TRACK_PREV -> stringResource(R.string.track_prev_button)
        SettingsManager.BTN_MUTE -> stringResource(R.string.mute_button)
        SettingsManager.BTN_CALL -> stringResource(R.string.call_button)
        SettingsManager.BTN_TOUCH_MUTE -> stringResource(R.string.touch_mute_button)
        SettingsManager.BTN_TOUCH_POWER -> stringResource(R.string.touch_power_button)
        SettingsManager.BTN_MEDIA_PLAY_PAUSE -> stringResource(R.string.media_play_pause_button)
        else -> "Button Settings"
    }

    // Generalized loading logic
    LaunchedEffect(buttonType) {
        // Short Press
        shortPressRemapped = settingsManager.isButtonRemapped(buttonType, false)
        shortPressActionType = settingsManager.getButtonActionType(buttonType, false)
        
        val shortPkg = settingsManager.getButtonAppPackage(buttonType, false)
        val shortName = settingsManager.getButtonAppName(buttonType, false)
        if (shortPkg != null && shortName != null) {
            selectedShortPressApp = AppInfo(shortPkg, shortName, null)
        }

        val shortLeftPkg = settingsManager.getButtonSplitAppPackage(buttonType, false, true)
        val shortLeftName = settingsManager.getButtonSplitAppName(buttonType, false, true)
        if (shortLeftPkg != null && shortLeftName != null) {
            shortPressSplitLeftApp = AppInfo(shortLeftPkg, shortLeftName, null)
        }

        val shortRightPkg = settingsManager.getButtonSplitAppPackage(buttonType, false, false)
        val shortRightName = settingsManager.getButtonSplitAppName(buttonType, false, false)
        if (shortRightPkg != null && shortRightName != null) {
            shortPressSplitRightApp = AppInfo(shortRightPkg, shortRightName, null)
        }

        // Long Press
        longPressRemapped = settingsManager.isButtonRemapped(buttonType, true)
        longPressActionType = settingsManager.getButtonActionType(buttonType, true)

        val longPkg = settingsManager.getButtonAppPackage(buttonType, true)
        val longName = settingsManager.getButtonAppName(buttonType, true)
        if (longPkg != null && longName != null) {
            selectedLongPressApp = AppInfo(longPkg, longName, null)
        }

        val longLeftPkg = settingsManager.getButtonSplitAppPackage(buttonType, true, true)
        val longLeftName = settingsManager.getButtonSplitAppName(buttonType, true, true)
        if (longLeftPkg != null && longLeftName != null) {
            longPressSplitLeftApp = AppInfo(longLeftPkg, longLeftName, null)
        }

        val longRightPkg = settingsManager.getButtonSplitAppPackage(buttonType, true, false)
        val longRightName = settingsManager.getButtonSplitAppName(buttonType, true, false)
        if (longRightPkg != null && longRightName != null) {
            longPressSplitRightApp = AppInfo(longRightPkg, longRightName, null)
        }

        // Feature-specific overrides
        if (BuildPropUtils.shouldHideDrlAction()) {
            if (shortPressActionType == ButtonActionType.TOGGLE_DRL) {
                shortPressActionType = ButtonActionType.DEFAULT_ACTION
                settingsManager.setButtonActionType(buttonType, false, ButtonActionType.DEFAULT_ACTION)
            }
            if (longPressActionType == ButtonActionType.TOGGLE_DRL) {
                longPressActionType = ButtonActionType.DEFAULT_ACTION
                settingsManager.setButtonActionType(buttonType, true, ButtonActionType.DEFAULT_ACTION)
            }
        }
    }
    
    // Icon loading logic
    LaunchedEffect(selectedShortPressApp, selectedLongPressApp) {
        listOf(selectedShortPressApp, selectedLongPressApp).forEach { app ->
            app?.let { appInfo ->
                if (appInfo.icon == null) {
                    try {
                        val packageManager = context.packageManager
                        val androidAppInfo = packageManager.getApplicationInfo(appInfo.packageName, 0)
                        val icon = packageManager.getApplicationIcon(androidAppInfo)
                        if (app == selectedShortPressApp) selectedShortPressApp = appInfo.copy(icon = icon)
                        else if (app == selectedLongPressApp) selectedLongPressApp = appInfo.copy(icon = icon)
                    } catch (e: Exception) { }
                }
            }
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBackPressed) {
                Text(text = stringResource(R.string.back), fontSize = 16.sp)
            }
            Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(60.dp))
        }
        
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
                settingsManager.setButtonRemapped(buttonType, false, remapped)
            },
            onLongPressRemappedChange = { remapped ->
                longPressRemapped = remapped
                settingsManager.setButtonRemapped(buttonType, true, remapped)
            },
            onShortPressActionTypeChange = { actionType ->
                shortPressActionType = actionType
                settingsManager.setButtonActionType(buttonType, false, actionType)
            },
            onLongPressActionTypeChange = { actionType ->
                longPressActionType = actionType
                settingsManager.setButtonActionType(buttonType, true, actionType)
            },
            onShortPressAppSelected = { app ->
                selectedShortPressApp = app
                settingsManager.setButtonApp(buttonType, false, app)
            },
            onLongPressAppSelected = { app ->
                selectedLongPressApp = app
                settingsManager.setButtonApp(buttonType, true, app)
            },
            onShortPressSplitLeftAppSelected = { app ->
                shortPressSplitLeftApp = app
                settingsManager.setButtonSplitApp(buttonType, false, true, app)
            },
            onShortPressSplitRightAppSelected = { app ->
                shortPressSplitRightApp = app
                settingsManager.setButtonSplitApp(buttonType, false, false, app)
            },
            onLongPressSplitLeftAppSelected = { app ->
                longPressSplitLeftApp = app
                settingsManager.setButtonSplitApp(buttonType, true, true, app)
            },
            onLongPressSplitRightAppSelected = { app ->
                longPressSplitRightApp = app
                settingsManager.setButtonSplitApp(buttonType, true, false, app)
            },
            showLongPressSettings = buttonType != SettingsManager.BTN_TOUCH_MUTE && 
                                   buttonType != SettingsManager.BTN_TOUCH_POWER
        )
    }
}
