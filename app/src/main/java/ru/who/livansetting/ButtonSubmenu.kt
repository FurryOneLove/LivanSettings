package ru.who.livansetting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.DpOffset
import androidx.core.graphics.drawable.toBitmap
import ru.who.livansetting.R
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
    var expandedShortPress by remember { mutableStateOf(false) }
    var expandedLongPress by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Заголовок кнопки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Сегмент "Короткое нажатие"
            PressSegment(
                title = stringResource(R.string.short_press),
                remapped = shortPressRemapped,
                actionType = shortPressActionType,
                selectedApp = shortPressApp,
                splitLeftApp = shortPressSplitLeftApp,
                splitRightApp = shortPressSplitRightApp,
                apps = apps,
                expanded = expandedShortPress,
                onRemappedChange = onShortPressRemappedChange,
                onActionTypeChange = onShortPressActionTypeChange,
                onAppSelected = onShortPressAppSelected,
                onSplitLeftAppSelected = onShortPressSplitLeftAppSelected,
                onSplitRightAppSelected = onShortPressSplitRightAppSelected,
                onExpandedChange = { expandedShortPress = it }
            )
            
            // Сегмент "Длинное нажатие" (показывается только если showLongPressSettings = true)
            if (showLongPressSettings) {
                PressSegment(
                    title = stringResource(R.string.long_press),
                    remapped = longPressRemapped,
                    actionType = longPressActionType,
                    selectedApp = longPressApp,
                    splitLeftApp = longPressSplitLeftApp,
                    splitRightApp = longPressSplitRightApp,
                    apps = apps,
                    expanded = expandedLongPress,
                    onRemappedChange = onLongPressRemappedChange,
                    onActionTypeChange = onLongPressActionTypeChange,
                    onAppSelected = onLongPressAppSelected,
                    onSplitLeftAppSelected = onLongPressSplitLeftAppSelected,
                    onSplitRightAppSelected = onLongPressSplitRightAppSelected,
                    onExpandedChange = { expandedLongPress = it }
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
    expanded: Boolean,
    onRemappedChange: (Boolean) -> Unit,
    onActionTypeChange: (ButtonActionType) -> Unit,
    onAppSelected: (AppInfo?) -> Unit,
    onSplitLeftAppSelected: (AppInfo?) -> Unit,
    onSplitRightAppSelected: (AppInfo?) -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedActionType by remember { mutableStateOf(false) }
    var expandedApp by remember { mutableStateOf(false) }
    var expandedSplitLeft by remember { mutableStateOf(false) }
    var expandedSplitRight by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Заголовок сегмента
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Switch для переназначения действия
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.remap_action),
                fontSize = 14.sp
            )
            Switch(
                checked = remapped,
                onCheckedChange = onRemappedChange
            )
        }
        
        // Выпадающий список действий (показывается только если переназначение включено)
        if (remapped) {
            // Выбор типа действия
            Box {
                OutlinedButton(
                    onClick = { expandedActionType = !expandedActionType },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = when (actionType) {
                            ButtonActionType.NOTHING -> stringResource(R.string.action_nothing)
                            ButtonActionType.OPEN_APP -> stringResource(R.string.action_open_app)
                            ButtonActionType.SPLIT_SCREEN -> stringResource(R.string.action_split_screen)
                            ButtonActionType.TOGGLE_DRL -> stringResource(R.string.action_toggle_drl)
                            ButtonActionType.TOGGLE_DRIVER_SEAT_HEAT -> stringResource(R.string.action_toggle_driver_seat_heat)
                            ButtonActionType.TOGGLE_PASSENGER_SEAT_HEAT -> stringResource(R.string.action_toggle_passenger_seat_heat)
                            ButtonActionType.TOGGLE_MEDIA_PLAY_PAUSE -> stringResource(R.string.action_toggle_media_play_pause)
                            ButtonActionType.DEFAULT_ACTION -> "Действие по умолчанию"
                        },
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                DropdownMenu(
                    expanded = expandedActionType,
                    onDismissRequest = { expandedActionType = false },
                    offset = DpOffset(x = (-200).dp, y = 0.dp),
                    modifier = Modifier
                        .widthIn(min = 200.dp, max = 300.dp)
                        .heightIn(max = 200.dp)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.action_nothing)) },
                        onClick = {
                            onActionTypeChange(ButtonActionType.NOTHING)
                            expandedActionType = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.action_open_app)) },
                        onClick = {
                            onActionTypeChange(ButtonActionType.OPEN_APP)
                            expandedActionType = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.action_split_screen)) },
                        onClick = {
                            onActionTypeChange(ButtonActionType.SPLIT_SCREEN)
                            expandedActionType = false
                        }
                    )
                    // Скрываем "Включить/выключить ДХО" если ro.build.flavor содержит "601"
                    if (!BuildPropUtils.shouldHideDrlAction()) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.action_toggle_drl)) },
                            onClick = {
                                onActionTypeChange(ButtonActionType.TOGGLE_DRL)
                                expandedActionType = false
                            }
                        )
                    }
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.action_toggle_driver_seat_heat)) },
                        onClick = {
                            onActionTypeChange(ButtonActionType.TOGGLE_DRIVER_SEAT_HEAT)
                            expandedActionType = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.action_toggle_passenger_seat_heat)) },
                        onClick = {
                            onActionTypeChange(ButtonActionType.TOGGLE_PASSENGER_SEAT_HEAT)
                            expandedActionType = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.action_toggle_media_play_pause)) },
                        onClick = {
                            onActionTypeChange(ButtonActionType.TOGGLE_MEDIA_PLAY_PAUSE)
                            expandedActionType = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Действие по умолчанию") },
                        onClick = {
                            onActionTypeChange(ButtonActionType.DEFAULT_ACTION)
                            expandedActionType = false
                        }
                    )
                }
            }
            
            // Выбор приложения (только для OPEN_APP)
            if (actionType == ButtonActionType.OPEN_APP) {
                Box {
                    OutlinedButton(
                        onClick = { expandedApp = !expandedApp },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(bottom = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            selectedApp?.icon?.let { icon: android.graphics.drawable.Drawable ->
                                Image(
                                    bitmap = icon.toBitmap(48, 48).asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(end = 8.dp)
                                )
                            }
                            Text(
                                text = selectedApp?.appName ?: stringResource(R.string.select_app),
                                fontSize = 14.sp,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    DropdownMenu(
                        expanded = expandedApp,
                        onDismissRequest = { expandedApp = false },
                        offset = DpOffset(x = (-200).dp, y = 0.dp),
                        modifier = Modifier
                            .widthIn(min = 200.dp, max = 300.dp)
                            .heightIn(max = 200.dp)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        apps.forEach { app: AppInfo ->
                            DropdownMenuItem(
                                text = { 
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        app.icon?.let { icon: android.graphics.drawable.Drawable ->
                                            Image(
                                                bitmap = icon.toBitmap(48, 48).asImageBitmap(),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .padding(end = 8.dp)
                                            )
                                        }
                                        Text(
                                            text = app.appName,
                                            fontSize = 14.sp
                                        )
                                    }
                                },
                                onClick = {
                                    onAppSelected(app)
                                    expandedApp = false
                                }
                            )
                        }
                    }
                }
            }
            
            // Выбор приложений для split-screen (только для SPLIT_SCREEN)
            if (actionType == ButtonActionType.SPLIT_SCREEN) {
                // Приложение слева
                Box {
                    OutlinedButton(
                        onClick = { expandedSplitLeft = !expandedSplitLeft },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(bottom = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            splitLeftApp?.icon?.let { icon: android.graphics.drawable.Drawable ->
                                Image(
                                    bitmap = icon.toBitmap(48, 48).asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(end = 8.dp)
                                )
                            }
                            Text(
                                text = splitLeftApp?.appName ?: stringResource(R.string.select_app_left),
                                fontSize = 14.sp,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    DropdownMenu(
                        expanded = expandedSplitLeft,
                        onDismissRequest = { expandedSplitLeft = false },
                        offset = DpOffset(x = (-200).dp, y = 0.dp),
                        modifier = Modifier
                            .widthIn(min = 200.dp, max = 300.dp)
                            .heightIn(max = 200.dp)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        apps.forEach { app: AppInfo ->
                            DropdownMenuItem(
                                text = { 
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        app.icon?.let { icon: android.graphics.drawable.Drawable ->
                                            Image(
                                                bitmap = icon.toBitmap(48, 48).asImageBitmap(),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .padding(end = 8.dp)
                                            )
                                        }
                                        Text(
                                            text = app.appName,
                                            fontSize = 14.sp
                                        )
                                    }
                                },
                                onClick = {
                                    onSplitLeftAppSelected(app)
                                    expandedSplitLeft = false
                                }
                            )
                        }
                    }
                }
                
                // Приложение справа
                Box {
                    OutlinedButton(
                        onClick = { expandedSplitRight = !expandedSplitRight },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            splitRightApp?.icon?.let { icon: android.graphics.drawable.Drawable ->
                                Image(
                                    bitmap = icon.toBitmap(48, 48).asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(end = 8.dp)
                                )
                            }
                            Text(
                                text = splitRightApp?.appName ?: stringResource(R.string.select_app_right),
                                fontSize = 14.sp,
                                maxLines = 1,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    
                    DropdownMenu(
                        expanded = expandedSplitRight,
                        onDismissRequest = { expandedSplitRight = false },
                        offset = DpOffset(x = (-200).dp, y = 0.dp),
                        modifier = Modifier
                            .widthIn(min = 200.dp, max = 300.dp)
                            .heightIn(max = 200.dp)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        apps.forEach { app: AppInfo ->
                            DropdownMenuItem(
                                text = { 
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        app.icon?.let { icon: android.graphics.drawable.Drawable ->
                                            Image(
                                                bitmap = icon.toBitmap(48, 48).asImageBitmap(),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .padding(end = 8.dp)
                                            )
                                        }
                                        Text(
                                            text = app.appName,
                                            fontSize = 14.sp
                                        )
                                    }
                                },
                                onClick = {
                                    onSplitRightAppSelected(app)
                                    expandedSplitRight = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
