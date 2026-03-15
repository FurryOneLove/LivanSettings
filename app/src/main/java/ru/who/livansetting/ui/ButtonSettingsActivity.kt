package ru.who.livansetting.ui

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
import ru.who.livansetting.data.AppInfo
import ru.who.livansetting.data.ButtonActionType
import ru.who.livansetting.data.SettingsManager
import ru.who.livansetting.ui.theme.LivanSettingTheme
import ru.who.livansetting.utils.BuildPropUtils

class ButtonSettingsActivity : ComponentActivity() {
    
    companion object {
        const val EXTRA_BUTTON_TYPE = "button_type"
        const val BUTTON_TYPE_MODE = SettingsManager.BTN_MODE
        
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
    
    val apps by remember { derivedStateOf { getInstalledApps(context).map { AppInfo(it.packageName, it.appName, it.icon) } } }
    
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

    LaunchedEffect(buttonType) {
        shortPressRemapped = settingsManager.isButtonRemapped(buttonType, false)
        shortPressActionType = settingsManager.getButtonActionType(buttonType, false)
        settingsManager.getButtonAppPackage(buttonType, false)?.let { pkg ->
            selectedShortPressApp = AppInfo(pkg, settingsManager.getButtonAppName(buttonType, false) ?: "", null)
        }
        
        longPressRemapped = settingsManager.isButtonRemapped(buttonType, true)
        longPressActionType = settingsManager.getButtonActionType(buttonType, true)
        settingsManager.getButtonAppPackage(buttonType, true)?.let { pkg ->
            selectedLongPressApp = AppInfo(pkg, settingsManager.getButtonAppName(buttonType, true) ?: "", null)
        }
    }
    
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBackPressed) { Text(stringResource(R.string.back)) }
            Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
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
            onShortPressRemappedChange = { shortPressRemapped = it; settingsManager.setButtonRemapped(buttonType, false, it) },
            onLongPressRemappedChange = { longPressRemapped = it; settingsManager.setButtonRemapped(buttonType, true, it) },
            onShortPressActionTypeChange = { shortPressActionType = it; settingsManager.setButtonActionType(buttonType, false, it) },
            onLongPressActionTypeChange = { longPressActionType = it; settingsManager.setButtonActionType(buttonType, true, it) },
            onShortPressAppSelected = { selectedShortPressApp = it; settingsManager.setButtonApp(buttonType, false, it) },
            onLongPressAppSelected = { selectedLongPressApp = it; settingsManager.setButtonApp(buttonType, true, it) },
            onShortPressSplitLeftAppSelected = { shortPressSplitLeftApp = it; settingsManager.setButtonSplitApp(buttonType, false, true, it) },
            onShortPressSplitRightAppSelected = { shortPressSplitRightApp = it; settingsManager.setButtonSplitApp(buttonType, false, false, it) },
            onLongPressSplitLeftAppSelected = { longPressSplitLeftApp = it; settingsManager.setButtonSplitApp(buttonType, true, true, it) },
            onLongPressSplitRightAppSelected = { longPressSplitRightApp = it; settingsManager.setButtonSplitApp(buttonType, true, false, it) },
            showLongPressSettings = buttonType != SettingsManager.BTN_TOUCH_MUTE && buttonType != SettingsManager.BTN_TOUCH_POWER
        )
    }
}

fun getInstalledApps(context: Context): List<AppInfo> {
    val pm = context.packageManager
    return pm.getInstalledApplications(0)
        .filter { (it.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0 }
        .map { AppInfo(it.packageName, pm.getApplicationLabel(it).toString(), pm.getApplicationIcon(it)) }
}
