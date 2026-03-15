package ru.who.livansetting.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.who.livansetting.R
import ru.who.livansetting.data.DriveModeSelection
import ru.who.livansetting.data.SettingsManager
import ru.who.livansetting.core.MainService
import ru.who.livansetting.ui.theme.LivanSettingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MainService.startService(this)
        
        setContent {
            LivanSettingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SettingsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    var selectedDriveMode by remember { mutableStateOf(DriveModeSelection.NONE) }
    
    LaunchedEffect(Unit) {
        selectedDriveMode = settingsManager.getDriveModeSelection()
    }
    
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = stringResource(R.string.welcome_message),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
        )
        
        LazyColumn {
            val buttons = listOf(
                R.string.mode_button to SettingsManager.BTN_MODE,
                R.string.home_button to SettingsManager.BTN_HOME,
                R.string.track_next_button to SettingsManager.BTN_TRACK_NEXT,
                R.string.track_prev_button to SettingsManager.BTN_TRACK_PREV,
                R.string.mute_button to SettingsManager.BTN_MUTE,
                R.string.call_button to SettingsManager.BTN_CALL,
                R.string.touch_mute_button to SettingsManager.BTN_TOUCH_MUTE,
                R.string.touch_power_button to SettingsManager.BTN_TOUCH_POWER
            )

            buttons.forEach { (resId, type) ->
                item {
                    ButtonSettingCard(stringResource(resId)) {
                        context.startActivity(ButtonSettingsActivity.createIntent(context, type))
                    }
                }
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp)) }
            
            item {
                DriveModeSelectionCard(selectedDriveMode) { mode ->
                    selectedDriveMode = mode
                    settingsManager.setDriveModeSelection(mode)
                }
            }
        }
    }
}

@Composable
fun ButtonSettingCard(title: String, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = title, fontSize = 16.sp, modifier = Modifier.weight(1f))
            IconButton(onClick = onClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        }
    }
}

@Composable
fun DriveModeSelectionCard(selectedMode: DriveModeSelection, onModeSelected: (DriveModeSelection) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(R.string.remember_drive_mode), fontWeight = FontWeight.Medium)
            Box {
                OutlinedButton(onClick = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) {
                    Text(getDriveModeName(selectedMode), modifier = Modifier.weight(1f))
                    Icon(Icons.Default.KeyboardArrowDown, null)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DriveModeSelection.values().forEach { mode ->
                        DropdownMenuItem(
                            text = { Text(getDriveModeName(mode)) },
                            onClick = { onModeSelected(mode); expanded = false }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun getDriveModeName(mode: DriveModeSelection): String {
    return when (mode) {
        DriveModeSelection.NONE -> stringResource(R.string.drive_mode_none)
        DriveModeSelection.ADAPTIVE -> stringResource(R.string.drive_mode_adaptive)
        DriveModeSelection.SPORT -> stringResource(R.string.drive_mode_sport)
        DriveModeSelection.COMFORT -> stringResource(R.string.drive_mode_comfort)
        DriveModeSelection.ECO -> stringResource(R.string.drive_mode_eco)
    }
}
