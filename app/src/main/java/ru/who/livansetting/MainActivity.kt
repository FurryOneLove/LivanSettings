package ru.who.livansetting

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import ru.who.livansetting.R
import ru.who.livansetting.ui.theme.LivanSettingTheme
import ru.who.livansetting.utils.WelcomeLightManager
import ru.who.livansetting.utils.FirstRunManager
import ru.who.livansetting.utils.CarMediaController
import ru.who.livansetting.utils.BuildPropUtils
import android.content.SharedPreferences

// Модель данных для приложения
data class AppInfo(
    val packageName: String,
    val appName: String,
    val icon: Drawable?
)

// Типы действий для кнопок
enum class ButtonActionType {
    NOTHING,
    OPEN_APP,
    SPLIT_SCREEN,
    TOGGLE_DRL,
    TOGGLE_DRIVER_SEAT_HEAT,
    TOGGLE_PASSENGER_SEAT_HEAT,
    TOGGLE_MEDIA_PLAY_PAUSE,
    DEFAULT_ACTION
}


// Функция для получения списка установленных приложений
fun getInstalledApps(context: Context): List<AppInfo> {
    val packageManager = context.packageManager
    val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
    
    return installedApps
        .filter { appInfo ->
            // Исключаем системные приложения и само приложение
            (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0 && 
            appInfo.packageName != context.packageName
        }
        .map { appInfo ->
            AppInfo(
                packageName = appInfo.packageName,
                appName = packageManager.getApplicationLabel(appInfo).toString(),
                icon = packageManager.getApplicationIcon(appInfo)
            )
        }
        .sortedBy { it.appName }
}

class MainActivity : ComponentActivity() {
    
    private var keyInputHandler: KeyInputHandler? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Проверяем первый запуск и выполняем алгоритм предоставления разрешения
        checkFirstRunAndGrantPermission()
        
        // Инициализируем обработчик клавиш
        keyInputHandler = KeyInputHandler(this)
        keyInputHandler?.initialize()
        
        setContent {
            LivanSettingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SettingsScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    
    /**
     * Проверяет первый запуск приложения и запрашивает необходимые разрешения
     */
    private fun checkFirstRunAndGrantPermission() {
        val firstRunManager = FirstRunManager(this)
        
        if (firstRunManager.isFirstRun()) {
            Log.d("MainActivity", "First run detected")
            
            // Отмечаем, что первый запуск выполнен
            firstRunManager.markFirstRunCompleted()
        } else {
            Log.d("MainActivity", "Not first run")
        }
        
        // Проверяем и запрашиваем разрешение на доступ к уведомлениям
        if (firstRunManager.shouldRequestNotificationPermission()) {
            Log.d("MainActivity", "Requesting notification listener permission")
            firstRunManager.requestNotificationPermission(this)
        } else {
            Log.d("MainActivity", "Notification permission already granted or not needed")
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Освобождаем ресурсы обработчика клавиш
        keyInputHandler?.release()
        keyInputHandler = null
    }
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val welcomeLightManager = remember { WelcomeLightManager(context) }
    var isWelcomeLightEnabled by remember { mutableStateOf(false) }
    
    val sharedPreferences = remember { 
        context.getSharedPreferences("livan_settings", Context.MODE_PRIVATE) 
    }
    
    // Состояние для выбора режима вождения
    var selectedDriveMode by remember { mutableStateOf(DriveModeSelection.NONE) }
    
    // Запускаем сервис при инициализации
    LaunchedEffect(Unit) {
        try {
            Log.d("MainActivity", "Starting MainService from MainActivity")
            ru.who.livansetting.services.MainService.startService(context)
            
            // Проверяем, что сервис запустился
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                val serviceInstance = ru.who.livansetting.services.MainService.getInstance()
                if (serviceInstance != null) {
                    Log.d("MainActivity", "MainService is running")
                } else {
                    Log.w("MainActivity", "MainService failed to start, retrying...")
                    ru.who.livansetting.services.MainService.startService(context)
                }
            }, 2000)
            
        } catch (e: Exception) {
            Log.e("MainActivity", "Failed to start MainService", e)
        }
        
        // Проверяем состояние провожающего света при первом запуске
        val welcomeLightState = welcomeLightManager.isWelcomeLightEnabled()
        isWelcomeLightEnabled = welcomeLightState ?: false
        Log.d("MainActivity", "Initial welcome light state: $welcomeLightState, UI state: $isWelcomeLightEnabled")
        
        // Инициализируем состояние выбора режима вождения
        val settingsManager = SettingsManager(context)
        val initialDriveMode = settingsManager.getDriveModeSelection()
        
        // Если выбран ADAPTIVE, но он должен быть скрыт, сбрасываем на NONE
        selectedDriveMode = if (initialDriveMode == DriveModeSelection.ADAPTIVE && BuildPropUtils.shouldHideAdaptiveMode()) {
            settingsManager.setDriveModeSelection(DriveModeSelection.NONE)
            DriveModeSelection.NONE
        } else {
            initialDriveMode
        }
        
        Log.d("MainActivity", "Initial drive mode selection: $selectedDriveMode")
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Приветствие пользователя
        Text(
            text = stringResource(R.string.welcome_message),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
        
        // Список кнопок для настройки
        LazyColumn {
            // Кнопка Mode
            item {
                ButtonSettingCard(
                    title = stringResource(R.string.mode_button),
                    onClick = {
                        val intent = ButtonSettingsActivity.createIntent(context, ButtonSettingsActivity.BUTTON_TYPE_MODE)
                        context.startActivity(intent)
                    }
                )
            }
            
            // Кнопка Home
            item {
                ButtonSettingCard(
                    title = stringResource(R.string.home_button),
                    onClick = {
                        val intent = ButtonSettingsActivity.createIntent(context, ButtonSettingsActivity.BUTTON_TYPE_HOME)
                        context.startActivity(intent)
                    }
                )
            }
            
            // Кнопка трэк вперёд
            item {
                ButtonSettingCard(
                    title = stringResource(R.string.track_next_button),
                    onClick = {
                        val intent = ButtonSettingsActivity.createIntent(context, ButtonSettingsActivity.BUTTON_TYPE_TRACK_NEXT)
                        context.startActivity(intent)
                    }
                )
            }
            
            // Кнопка трэк назад
            item {
                ButtonSettingCard(
                    title = stringResource(R.string.track_prev_button),
                    onClick = {
                        val intent = ButtonSettingsActivity.createIntent(context, ButtonSettingsActivity.BUTTON_TYPE_TRACK_PREV)
                        context.startActivity(intent)
                    }
                )
            }
            
            // Кнопка Mute
            item {
                ButtonSettingCard(
                    title = stringResource(R.string.mute_button),
                    onClick = {
                        val intent = ButtonSettingsActivity.createIntent(context, ButtonSettingsActivity.BUTTON_TYPE_MUTE)
                        context.startActivity(intent)
                    }
                )
            }
            
            // Кнопка вызова
            item {
                ButtonSettingCard(
                    title = stringResource(R.string.call_button),
                    onClick = {
                        val intent = ButtonSettingsActivity.createIntent(context, ButtonSettingsActivity.BUTTON_TYPE_CALL)
                        context.startActivity(intent)
                    }
                )
            }
            
            // Сенсорная кнопка Mute
            item {
                ButtonSettingCard(
                    title = stringResource(R.string.touch_mute_button),
                    onClick = {
                        val intent = ButtonSettingsActivity.createIntent(context, ButtonSettingsActivity.BUTTON_TYPE_TOUCH_MUTE)
                        context.startActivity(intent)
                    }
                )
            }
            
            // Сенсорная кнопка выключения
            item {
                ButtonSettingCard(
                    title = stringResource(R.string.touch_power_button),
                    onClick = {
                        val intent = ButtonSettingsActivity.createIntent(context, ButtonSettingsActivity.BUTTON_TYPE_TOUCH_POWER)
                        context.startActivity(intent)
                    }
                )
            }
            
            /*
            // Кнопка Play/Pause
            item {
                ButtonSettingCard(
                    title = stringResource(R.string.media_play_pause_button),
                    onClick = {
                        val intent = ButtonSettingsActivity.createIntent(context, ButtonSettingsActivity.BUTTON_TYPE_MEDIA_PLAY_PAUSE)
                        context.startActivity(intent)
                    }
                )
            }
            */

            // Разделитель
            item {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            }
            
            // Подзаголовок "Другие настройки"
            item {
                Text(
                    text = stringResource(R.string.other_settings),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
            
            /* 
            // Кнопка Function ID меню
            item {
                ButtonSettingCard(
                    title = stringResource(R.string.function_id_menu),
                    onClick = {
                        val intent = FunctionIdActivity.createIntent(context)
                        context.startActivity(intent)
                    }
                )
            }
            
            // Кнопка Sensor меню
            item {
                ButtonSettingCard(
                    title = stringResource(R.string.sensor_menu),
                    onClick = {
                        val intent = SensorActivity.createIntent(context)
                        context.startActivity(intent)
                    }
                )
            }
            */
            
            // Switch-button для провожающего света - скрыт
            /*
            item {
                WelcomeLightSwitchCard(
                    isEnabled = isWelcomeLightEnabled,
                    onToggle = { enabled ->
                        isWelcomeLightEnabled = enabled
                        if (enabled) {
                            // Включаем провожающий свет с восстановлением сохраненного значения
                            welcomeLightManager.enableWelcomeLightWithRestore()
                        } else {
                            // Выключаем провожающий свет с сохранением текущего значения
                            welcomeLightManager.disableWelcomeLightWithSave()
                        }
                    }
                )
            }
            */
            
            // Кнопка "Автоподогревы"
            item {
                ButtonSettingCard(
                    title = stringResource(R.string.auto_warm),
                    onClick = {
                        val intent = AutoWarmActivity.createIntent(context)
                        context.startActivity(intent)
                    }
                )
            }
            
            // Выбор режима вождения
            item {
                DriveModeSelectionCard(
                    selectedMode = selectedDriveMode,
                    onModeSelected = { mode ->
                        selectedDriveMode = mode
                        val settingsManager = SettingsManager(context)
                        settingsManager.setDriveModeSelection(mode)
                        Log.d("MainActivity", "Drive mode selection changed to: $mode")
                    }
                )
            }
            
        }
    }
}

@Composable
fun ButtonSettingCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
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
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            
            IconButton(
                onClick = onClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = stringResource(R.string.configure_button),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}



@Composable
fun WelcomeLightSwitchCard(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.disable_welcome_light),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            
            Switch(
                checked = !isEnabled, // Инвертируем логику: если провожающий свет включен, то switch выключен
                onCheckedChange = { checked ->
                    onToggle(!checked) // Инвертируем обратно при сохранении
                }
            )
        }
    }
}



@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DriveModeSelectionCard(
    selectedMode: DriveModeSelection,
    onModeSelected: (DriveModeSelection) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.remember_drive_mode),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            
            Box {
                OutlinedTextField(
                    value = when (selectedMode) {
                        DriveModeSelection.NONE -> stringResource(R.string.drive_mode_none)
                        DriveModeSelection.ADAPTIVE -> if (BuildPropUtils.shouldHideAdaptiveMode()) {
                            stringResource(R.string.drive_mode_none) // Показываем "Не запоминать" если Адаптив скрыт
                        } else {
                            stringResource(R.string.drive_mode_adaptive)
                        }
                        DriveModeSelection.SPORT -> stringResource(R.string.drive_mode_sport)
                        DriveModeSelection.COMFORT -> stringResource(R.string.drive_mode_comfort)
                        DriveModeSelection.ECO -> stringResource(R.string.drive_mode_eco)
                    },
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Выберите режим") },
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = "Открыть меню"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DriveModeSelection.values().forEach { mode ->
                        // Скрываем "Адаптив" если ro.build.flavor содержит "602"
                        if (mode == DriveModeSelection.ADAPTIVE && BuildPropUtils.shouldHideAdaptiveMode()) {
                            return@forEach
                        }
                        
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    when (mode) {
                                        DriveModeSelection.NONE -> stringResource(R.string.drive_mode_none)
                                        DriveModeSelection.ADAPTIVE -> stringResource(R.string.drive_mode_adaptive)
                                        DriveModeSelection.SPORT -> stringResource(R.string.drive_mode_sport)
                                        DriveModeSelection.COMFORT -> stringResource(R.string.drive_mode_comfort)
                                        DriveModeSelection.ECO -> stringResource(R.string.drive_mode_eco)
                                    }
                                )
                            },
                            onClick = {
                                onModeSelected(mode)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    LivanSettingTheme {
        SettingsScreen()
    }
}