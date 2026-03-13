package ru.who.livansetting

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.who.livansetting.R
import ru.who.livansetting.ui.theme.LivanSettingTheme

class AutoWarmActivity : ComponentActivity() {
    
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AutoWarmActivity::class.java)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            LivanSettingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AutoWarmScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AutoWarmScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPreferences = remember { 
        context.getSharedPreferences("livan_settings", Context.MODE_PRIVATE) 
    }
    
    // Состояния для водителя
    var isDriverAutoWarmEnabled by remember { 
        mutableStateOf(sharedPreferences.getBoolean("driver_auto_warm_enabled", false)) 
    }
    // Удалено: настройка порога температуры для водителя
    var driverWarmLevel by remember { 
        mutableStateOf(sharedPreferences.getInt("driver_warm_level", 1)) 
    }
    var driverTimeoutMinutes by remember { 
        mutableStateOf(sharedPreferences.getInt("driver_timeout_minutes", 30).toString()) 
    }
    
    // Состояния для пассажира
    var isPassengerAutoWarmEnabled by remember { 
        mutableStateOf(sharedPreferences.getBoolean("passenger_auto_warm_enabled", false)) 
    }
    // Удалено: настройка порога температуры для пассажира
    var passengerWarmLevel by remember { 
        mutableStateOf(sharedPreferences.getInt("passenger_warm_level", 1)) 
    }
    var passengerTimeoutMinutes by remember { 
        mutableStateOf(sharedPreferences.getInt("passenger_timeout_minutes", 30).toString()) 
    }
    
    // Функция для сохранения настроек
    fun saveSettings() {
        with(sharedPreferences.edit()) {
            putBoolean("driver_auto_warm_enabled", isDriverAutoWarmEnabled)
            putInt("driver_warm_level", driverWarmLevel)
            putInt("driver_timeout_minutes", driverTimeoutMinutes.toIntOrNull() ?: 30)
            putBoolean("passenger_auto_warm_enabled", isPassengerAutoWarmEnabled)
            putInt("passenger_warm_level", passengerWarmLevel)
            putInt("passenger_timeout_minutes", passengerTimeoutMinutes.toIntOrNull() ?: 30)
            apply()
        }
        Log.d("AutoWarmActivity", "Settings saved")
    }
    
    // Сохраняем настройки при изменении
    LaunchedEffect(isDriverAutoWarmEnabled, driverWarmLevel, driverTimeoutMinutes,
                   isPassengerAutoWarmEnabled, passengerWarmLevel, passengerTimeoutMinutes) {
        saveSettings()
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { (context as ComponentActivity).finish() },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Text(
                text = stringResource(R.string.auto_warm_settings),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            )
        }
        
        LazyColumn {
            // Настройки водителя
            item {
                Text(
                    text = stringResource(R.string.driver_settings),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, top = 16.dp)
                )
            }
            
            // Переключатель автоподогрева водителя
            item {
                AutoWarmSwitchCard(
                    title = stringResource(R.string.enable_driver_seat_auto_warm),
                    isEnabled = isDriverAutoWarmEnabled,
                    onToggle = { isDriverAutoWarmEnabled = it }
                )
            }
            
            // Удалено: поле ввода порога температуры для водителя
            
            // Выпадающее меню степени подогрева для водителя
            item {
                WarmLevelCard(
                    title = stringResource(R.string.warm_level),
                    selectedLevel = driverWarmLevel,
                    onLevelSelected = { driverWarmLevel = it },
                    enabled = isDriverAutoWarmEnabled
                )
            }
            
            // Поле ввода времени выключения для водителя
            item {
                TimeoutInputCard(
                    title = stringResource(R.string.auto_off_timeout),
                    value = driverTimeoutMinutes,
                    onValueChange = { driverTimeoutMinutes = it },
                    enabled = isDriverAutoWarmEnabled
                )
            }
            
            // Разделитель
            item {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            }
            
            // Настройки пассажира
            item {
                Text(
                    text = stringResource(R.string.passenger_settings),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
            
            // Переключатель автоподогрева пассажира
            item {
                AutoWarmSwitchCard(
                    title = stringResource(R.string.enable_passenger_seat_auto_warm),
                    isEnabled = isPassengerAutoWarmEnabled,
                    onToggle = { isPassengerAutoWarmEnabled = it }
                )
            }
            
            // Удалено: поле ввода порога температуры для пассажира
            
            // Выпадающее меню степени подогрева для пассажира
            item {
                WarmLevelCard(
                    title = stringResource(R.string.passenger_warm_level),
                    selectedLevel = passengerWarmLevel,
                    onLevelSelected = { passengerWarmLevel = it },
                    enabled = isPassengerAutoWarmEnabled
                )
            }
            
            // Поле ввода времени выключения для пассажира
            item {
                TimeoutInputCard(
                    title = stringResource(R.string.passenger_auto_off_timeout),
                    value = passengerTimeoutMinutes,
                    onValueChange = { passengerTimeoutMinutes = it },
                    enabled = isPassengerAutoWarmEnabled
                )
            }
        }
    }
}

@Composable
fun AutoWarmSwitchCard(
    title: String,
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
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggle
            )
        }
    }
}

@Composable
fun TemperatureInputCard(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
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
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                suffix = {
                    Text(
                        text = stringResource(R.string.temperature_unit),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun WarmLevelCard(
    title: String,
    selectedLevel: Int,
    onLevelSelected: (Int) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
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
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Уровень 1
                FilterChip(
                    onClick = { if (enabled) onLevelSelected(1) },
                    label = { Text(stringResource(R.string.warm_level_1)) },
                    selected = selectedLevel == 1,
                    enabled = enabled,
                    modifier = Modifier.weight(1f)
                )
                
                // Уровень 2
                FilterChip(
                    onClick = { if (enabled) onLevelSelected(2) },
                    label = { Text(stringResource(R.string.warm_level_2)) },
                    selected = selectedLevel == 2,
                    enabled = enabled,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TimeoutInputCard(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
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
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                suffix = {
                    Text(
                        text = stringResource(R.string.minutes),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AutoWarmScreenPreview() {
    LivanSettingTheme {
        AutoWarmScreen()
    }
}
