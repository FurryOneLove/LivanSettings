package ru.who.livansetting.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import ru.who.livansetting.R
import ru.who.livansetting.data.AppInfo
import ru.who.livansetting.data.ButtonActionType
import ru.who.livansetting.utils.BuildPropUtils

@Composable
fun ButtonSubmenu(
    title: String,
    shortPressRemapped: Boolean,
    longPressRemapped: Boolean,
    shortPressActionType: ButtonActionType,
    longPressActionType: ButtonActionType,
    shortPressApp: AppInfo?,
    longPressApp: AppInfo?,
    shortPressSplitLeftApp: AppInfo?,
    shortPressSplitRightApp: AppInfo?,
    longPressSplitLeftApp: AppInfo?,
    longPressSplitRightApp: AppInfo?,
    apps: List<AppInfo>,
    onShortPressRemappedChange: (Boolean) -> Unit,
    onLongPressRemappedChange: (Boolean) -> Unit,
    onShortPressActionTypeChange: (ButtonActionType) -> Unit,
    onLongPressActionTypeChange: (ButtonActionType) -> Unit,
    onShortPressAppSelected: (AppInfo?) -> Unit,
    onLongPressAppSelected: (AppInfo?) -> Unit,
    onShortPressSplitLeftAppSelected: (AppInfo?) -> Unit,
    onShortPressSplitRightAppSelected: (AppInfo?) -> Unit,
    onLongPressSplitLeftAppSelected: (AppInfo?) -> Unit,
    onLongPressSplitRightAppSelected: (AppInfo?) -> Unit,
    showLongPressSettings: Boolean = true,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
            
            PressSegment(
                stringResource(R.string.short_press), shortPressRemapped, shortPressActionType, 
                shortPressApp, shortPressSplitLeftApp, shortPressSplitRightApp, apps,
                onShortPressRemappedChange, onShortPressActionTypeChange, onShortPressAppSelected,
                onShortPressSplitLeftAppSelected, onShortPressSplitRightAppSelected
            )
            
            if (showLongPressSettings) {
                PressSegment(
                    stringResource(R.string.long_press), longPressRemapped, longPressActionType,
                    longPressApp, longPressSplitLeftApp, longPressSplitRightApp, apps,
                    onLongPressRemappedChange, onLongPressActionTypeChange, onLongPressAppSelected,
                    onLongPressSplitLeftAppSelected, onLongPressSplitRightAppSelected
                )
            }
        }
    }
}

@Composable
private fun PressSegment(
    title: String,
    remapped: Boolean,
    actionType: ButtonActionType,
    selectedApp: AppInfo?,
    splitLeftApp: AppInfo?,
    splitRightApp: AppInfo?,
    apps: List<AppInfo>,
    onRemappedChange: (Boolean) -> Unit,
    onActionTypeChange: (ButtonActionType) -> Unit,
    onAppSelected: (AppInfo?) -> Unit,
    onSplitLeftAppSelected: (AppInfo?) -> Unit,
    onSplitRightAppSelected: (AppInfo?) -> Unit
) {
    var expandedActionType by remember { mutableStateOf(false) }
    var expandedApp by remember { mutableStateOf(false) }
    var expandedSplitLeft by remember { mutableStateOf(false) }
    var expandedSplitRight by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.remap_action))
            Switch(checked = remapped, onCheckedChange = onRemappedChange)
        }
        
        if (remapped) {
            Box {
                OutlinedButton(onClick = { expandedActionType = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(getActionTypeName(actionType), modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ArrowDropDown, null)
                }
                DropdownMenu(expanded = expandedActionType, onDismissRequest = { expandedActionType = false }) {
                    ButtonActionType.values().forEach { type ->
                        if (type == ButtonActionType.TOGGLE_DRL && BuildPropUtils.shouldHideDrlAction()) return@forEach
                        DropdownMenuItem(
                            text = { Text(getActionTypeName(type)) },
                            onClick = { onActionTypeChange(type); expandedActionType = false }
                        )
                    }
                }
            }

            if (actionType == ButtonActionType.OPEN_APP) {
                AppSelector(selectedApp, apps, onAppSelected, expandedApp, { expandedApp = it })
            }
            
            if (actionType == ButtonActionType.SPLIT_SCREEN) {
                AppSelector(splitLeftApp, apps, onSplitLeftAppSelected, expandedSplitLeft, { expandedSplitLeft = it }, stringResource(R.string.select_app_left))
                Spacer(modifier = Modifier.height(8.dp))
                AppSelector(splitRightApp, apps, onSplitRightAppSelected, expandedSplitRight, { expandedSplitRight = it }, stringResource(R.string.select_app_right))
            }
        }
    }
}

@Composable
private fun getActionTypeName(type: ButtonActionType): String {
    return when (type) {
        ButtonActionType.NOTHING -> stringResource(R.string.action_nothing)
        ButtonActionType.OPEN_APP -> stringResource(R.string.action_open_app)
        ButtonActionType.SPLIT_SCREEN -> stringResource(R.string.action_split_screen)
        ButtonActionType.TOGGLE_DRL -> stringResource(R.string.action_toggle_drl)
        ButtonActionType.TOGGLE_DRIVER_SEAT_HEAT -> stringResource(R.string.action_toggle_driver_seat_heat)
        ButtonActionType.TOGGLE_PASSENGER_SEAT_HEAT -> stringResource(R.string.action_toggle_passenger_seat_heat)
        ButtonActionType.TOGGLE_MEDIA_PLAY_PAUSE -> stringResource(R.string.action_toggle_media_play_pause)
        ButtonActionType.DEFAULT_ACTION -> "Действие по умолчанию"
    }
}

@Composable
private fun AppSelector(selectedApp: AppInfo?, apps: List<AppInfo>, onSelected: (AppInfo?) -> Unit, expanded: Boolean, onExpandChange: (Boolean) -> Unit, label: String = "") {
    Box {
        OutlinedButton(onClick = { onExpandChange(true) }, modifier = Modifier.fillMaxWidth()) {
            Text(selectedApp?.appName ?: label.ifEmpty { stringResource(R.string.select_app) }, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ArrowDropDown, null)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { onExpandChange(false) }) {
            apps.forEach { app ->
                DropdownMenuItem(
                    text = { Text(app.appName) },
                    leadingIcon = { app.icon?.let { Image(it.toBitmap(48, 48).asImageBitmap(), null, modifier = Modifier.size(24.dp)) } },
                    onClick = { onSelected(app); onExpandChange(false) }
                )
            }
        }
    }
}
