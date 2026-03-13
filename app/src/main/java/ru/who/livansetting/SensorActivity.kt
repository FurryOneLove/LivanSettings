package ru.who.livansetting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ecarx.xui.adaptapi.FunctionStatus
import com.ecarx.xui.adaptapi.car.sensor.ISensor
import com.ecarx.xui.adaptapi.car.sensor.ISensorGroupValue
import ru.who.livansetting.R
import ru.who.livansetting.services.CarService
import ru.who.livansetting.ui.theme.LivanSettingTheme

class SensorActivity : ComponentActivity() {
    
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, SensorActivity::class.java)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            LivanSettingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SensorScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SensorScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val carService = remember { CarService(context) }
    
    var sensorId by remember { mutableStateOf("") }
    var sensorRate by remember { mutableStateOf("3") }
    var sensorGroupType by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var selectedCommonSensor by remember { mutableStateOf(ISensor.SENSOR_TYPE_TEMPERATURE_INDOOR) }
    
    // Инициализация CarService
    LaunchedEffect(Unit) {
        carService.createCar()
        carService.connectToCarInterface()
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Заголовок с кнопкой назад
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { (context as ComponentActivity).finish() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
            
            Text(
                text = stringResource(R.string.sensor_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // Секция распространенных датчиков
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.sensor_common_types),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                // Кнопки для тестирования распространенных датчиков
                val commonSensors = listOf(
                    ISensor.SENSOR_TYPE_TEMPERATURE_INDOOR to stringResource(R.string.temperature_indoor),
                    ISensor.SENSOR_TYPE_TEMPERATURE_AMBIENT to stringResource(R.string.temperature_ambient),
                    ISensor.SENSOR_TYPE_CAR_SPEED to stringResource(R.string.car_speed),
                    ISensor.SENSOR_TYPE_FUEL_LEVEL to stringResource(R.string.fuel_level),
                    ISensor.SENSOR_TYPE_RPM to stringResource(R.string.rpm),
                    ISensor.SENSOR_TYPE_GEAR to stringResource(R.string.gear),
                    ISensor.SENSOR_TYPE_HANDBRAKE_STATE to stringResource(R.string.handbrake),
                    ISensor.SENSOR_TYPE_IGNITION_STATE to stringResource(R.string.ignition)
                )
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(200.dp)
                ) {
                    items(commonSensors) { (sensorType, sensorName) ->
                        Button(
                            onClick = {
                                selectedCommonSensor = sensorType
                                sensorId = sensorType.toString()
                                resultText = "Выбран датчик: $sensorName (ID: $sensorType)"
                            },
                            enabled = !isLoading && carService.isConnected(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = sensorName,
                                fontSize = 10.sp,
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
        
        // Поле для ввода Sensor ID
        OutlinedTextField(
            value = sensorId,
            onValueChange = { sensorId = it },
            label = { Text(stringResource(R.string.sensor_id_hint)) },
            placeholder = { Text("Например: ${ISensor.SENSOR_TYPE_TEMPERATURE_INDOOR}") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        // Поле для ввода частоты обновления
        OutlinedTextField(
            value = sensorRate,
            onValueChange = { sensorRate = it },
            label = { Text(stringResource(R.string.sensor_rate_hint)) },
            placeholder = { Text("0-5 (3 = нормальная)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        // Поле для ввода типа группы датчиков
        OutlinedTextField(
            value = sensorGroupType,
            onValueChange = { sensorGroupType = it },
            label = { Text(stringResource(R.string.sensor_group_type_hint)) },
            placeholder = { Text("Опционально") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
        
        // Кнопки операций с датчиками
        Text(
            text = stringResource(R.string.sensor_operations),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Кнопка isSensorSupported
            Button(
                onClick = {
                    Log.d("SensorActivity", "isSensorSupported clicked")
                    if (sensorId.isBlank()) {
                        resultText = "Неверный ID датчика"
                        return@Button
                    }
                    
                    isLoading = true
                    resultText = "Выполняется isSensorSupported..."
                    
                    try {
                        val id = sensorId.toIntOrNull()
                        if (id == null) {
                            resultText = "Неверный ID датчика"
                            return@Button
                        }
                        
                        val sensor = carService.getISensor()
                        if (sensor == null) {
                            resultText = "Датчики не инициализированы"
                            return@Button
                        }
                        
                        val status = sensor.isSensorSupported(id)
                        val statusText = when (status) {
                            FunctionStatus.active -> "Датчик поддерживается"
                            FunctionStatus.notactive -> "Датчик не поддерживается"
                            FunctionStatus.notavailable -> "Датчик недоступен"
                            FunctionStatus.error -> "Ошибка датчика"
                            else -> "Неизвестный статус: $status"
                        }
                        resultText = "isSensorSupported($id) = $statusText"
                    } catch (e: Exception) {
                        resultText = "Ошибка: ${e.message}"
                        Log.e("SensorActivity", "Error in isSensorSupported", e)
                    } finally {
                        isLoading = false
                    }
                },
                enabled = !isLoading && carService.isConnected(),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.is_sensor_supported),
                    fontSize = 12.sp
                )
            }
            
            // Кнопка getSensorValue
            Button(
                onClick = {
                    Log.d("SensorActivity", "getSensorValue clicked")
                    if (sensorId.isBlank()) {
                        resultText = "Неверный ID датчика"
                        return@Button
                    }
                    
                    isLoading = true
                    resultText = "Выполняется getSensorValue..."
                    
                    try {
                        val id = sensorId.toIntOrNull()
                        if (id == null) {
                            resultText = "Неверный ID датчика"
                            return@Button
                        }
                        
                        val sensor = carService.getISensor()
                        if (sensor == null) {
                            resultText = "Датчики не инициализированы"
                            return@Button
                        }
                        
                        val value = sensor.getSensorLatestValue(id)
                        resultText = "getSensorValue($id) = $value"
                    } catch (e: Exception) {
                        resultText = "Ошибка: ${e.message}"
                        Log.e("SensorActivity", "Error in getSensorValue", e)
                    } finally {
                        isLoading = false
                    }
                },
                enabled = !isLoading && carService.isConnected(),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.get_sensor_value),
                    fontSize = 12.sp
                )
            }
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Кнопка getSensorGroupValue
            Button(
                onClick = {
                    Log.d("SensorActivity", "getSensorGroupValue clicked")
                    if (sensorId.isBlank()) {
                        resultText = "Неверный ID датчика"
                        return@Button
                    }
                    
                    isLoading = true
                    resultText = "Выполняется getSensorGroupValue..."
                    
                    try {
                        val id = sensorId.toIntOrNull()
                        if (id == null) {
                            resultText = "Неверный ID датчика"
                            return@Button
                        }
                        
                        val sensor = carService.getISensor()
                        if (sensor == null) {
                            resultText = "Датчики не инициализированы"
                            return@Button
                        }
                        
                        val groupValue = sensor.getSensorGroupLatestValue(id)
                        if (groupValue != null) {
                            resultText = "getSensorGroupValue($id) = GroupType: ${groupValue.sensorGroupType}, Time: ${groupValue.tickTime}, Interval: ${groupValue.interval}"
                        } else {
                            resultText = "getSensorGroupValue($id) = null"
                        }
                    } catch (e: Exception) {
                        resultText = "Ошибка: ${e.message}"
                        Log.e("SensorActivity", "Error in getSensorGroupValue", e)
                    } finally {
                        isLoading = false
                    }
                },
                enabled = !isLoading && carService.isConnected(),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.get_sensor_group_value),
                    fontSize = 12.sp
                )
            }
            
            // Кнопка getSensorEvent
            Button(
                onClick = {
                    Log.d("SensorActivity", "getSensorEvent clicked")
                    if (sensorId.isBlank()) {
                        resultText = "Неверный ID датчика"
                        return@Button
                    }
                    
                    isLoading = true
                    resultText = "Выполняется getSensorEvent..."
                    
                    try {
                        val id = sensorId.toIntOrNull()
                        if (id == null) {
                            resultText = "Неверный ID датчика"
                            return@Button
                        }
                        
                        val sensor = carService.getISensor()
                        if (sensor == null) {
                            resultText = "Датчики не инициализированы"
                            return@Button
                        }
                        
                        val event = sensor.getSensorEvent(id)
                        resultText = "getSensorEvent($id) = $event"
                    } catch (e: Exception) {
                        resultText = "Ошибка: ${e.message}"
                        Log.e("SensorActivity", "Error in getSensorEvent", e)
                    } finally {
                        isLoading = false
                    }
                },
                enabled = !isLoading && carService.isConnected(),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.get_sensor_event),
                    fontSize = 12.sp
                )
            }
        }
        
        // Кнопки управления слушателями
        Text(
            text = stringResource(R.string.sensor_listeners),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Кнопка registerListener
            Button(
                onClick = {
                    Log.d("SensorActivity", "registerListener clicked")
                    if (sensorId.isBlank() || sensorRate.isBlank()) {
                        resultText = "Заполните все поля"
                        return@Button
                    }
                    
                    isLoading = true
                    resultText = "Выполняется registerListener..."
                    
                    try {
                        val id = sensorId.toIntOrNull()
                        val rate = sensorRate.toIntOrNull()
                        
                        if (id == null) {
                            resultText = "Неверный ID датчика"
                            return@Button
                        }
                        
                        if (rate == null || rate < 0 || rate > 5) {
                            resultText = "Неверная частота обновления"
                            return@Button
                        }
                        
                        val sensor = carService.getISensor()
                        if (sensor == null) {
                            resultText = "Датчики не инициализированы"
                            return@Button
                        }
                        
                        val success = sensor.registerListener(carService.getSensorListener()!!, id, rate)
                        resultText = "registerListener($id, $rate) = $success"
                    } catch (e: Exception) {
                        resultText = "Ошибка: ${e.message}"
                        Log.e("SensorActivity", "Error in registerListener", e)
                    } finally {
                        isLoading = false
                    }
                },
                enabled = !isLoading && carService.isConnected(),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.register_sensor_listener),
                    fontSize = 12.sp
                )
            }
            
            // Кнопка unregisterListener
            Button(
                onClick = {
                    Log.d("SensorActivity", "unregisterListener clicked")
                    
                    isLoading = true
                    resultText = "Выполняется unregisterListener..."
                    
                    try {
                        val sensor = carService.getISensor()
                        if (sensor == null) {
                            resultText = "Датчики не инициализированы"
                            return@Button
                        }
                        
                        sensor.unregisterListener(carService.getSensorListener()!!)
                        resultText = "unregisterListener() выполнено"
                    } catch (e: Exception) {
                        resultText = "Ошибка: ${e.message}"
                        Log.e("SensorActivity", "Error in unregisterListener", e)
                    } finally {
                        isLoading = false
                    }
                },
                enabled = !isLoading && carService.isConnected(),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.unregister_sensor_listener),
                    fontSize = 12.sp
                )
            }
        }
        
        // Индикатор состояния подключения
        if (!carService.isConnected()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = stringResource(R.string.not_connected),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        
        // Окно вывода результата
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.result_output),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Text(
                        text = resultText.ifBlank { "Результат выполнения операций с датчиками будет отображаться здесь" },
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
    
    // Очистка ресурсов при уничтожении
    DisposableEffect(Unit) {
        onDispose {
            carService.cleanup()
        }
    }
}
