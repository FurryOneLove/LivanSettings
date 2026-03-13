package ru.who.livansetting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
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
import ru.who.livansetting.R
import ru.who.livansetting.services.CarService
import ru.who.livansetting.ui.theme.LivanSettingTheme

class FunctionIdActivity : ComponentActivity() {
    
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, FunctionIdActivity::class.java)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            LivanSettingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FunctionIdScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunctionIdScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val carService = remember { CarService(context) }
    
    var functionId by remember { mutableStateOf("") }
    var functionValue by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
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
                text = stringResource(R.string.function_id_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // Поле для ввода Function ID
        OutlinedTextField(
            value = functionId,
            onValueChange = { functionId = it },
            label = { Text(stringResource(R.string.function_id_hint)) },
            placeholder = { Text("Например: 537135104") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        // Поле для ввода значения
        OutlinedTextField(
            value = functionValue,
            onValueChange = { functionValue = it },
            label = { Text(stringResource(R.string.function_value_hint)) },
            placeholder = { Text("Например: 1") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
        
        // Три кнопки горизонтально
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Кнопка isFunctionSupported
            Button(
                onClick = {
                    Log.d("FunctionIdActivity", "isFunctionSupported clicked")
                    if (functionId.isBlank()) {
                        resultText = "Неверный Function ID"
                        return@Button
                    }
                    
                    isLoading = true
                    resultText = "Выполняется isFunctionSupported..."
                    
                    try {
                        val id = functionId.toIntOrNull()
                        if (id == null) {
                            resultText = "Неверный Function ID"
                            return@Button
                        }
                        
                        val isSupported = carService.isFunctionSupported(id)
                        resultText = "isFunctionSupported($id) = $isSupported"
                    } catch (e: Exception) {
                        resultText = "Ошибка: ${e.message}"
                        Log.e("FunctionIdActivity", "Error in isFunctionSupported", e)
                    } finally {
                        isLoading = false
                    }
                },
                enabled = !isLoading && carService.isConnected(),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.is_function_supported),
                    fontSize = 12.sp
                )
            }
            
            // Кнопка getFunctionValue
            Button(
                onClick = {
                    Log.d("FunctionIdActivity", "getFunctionValue clicked")
                    if (functionId.isBlank()) {
                        resultText = "Неверный Function ID"
                        return@Button
                    }
                    
                    isLoading = true
                    resultText = "Выполняется getFunctionValue..."
                    
                    try {
                        val id = functionId.toIntOrNull()
                        if (id == null) {
                            resultText = "Неверный Function ID"
                            return@Button
                        }
                        
                        val value = carService.getFunctionValue(id)
                        resultText = "getFunctionValue($id) = $value"
                    } catch (e: Exception) {
                        resultText = "Ошибка: ${e.message}"
                        Log.e("FunctionIdActivity", "Error in getFunctionValue", e)
                    } finally {
                        isLoading = false
                    }
                },
                enabled = !isLoading && carService.isConnected(),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.get_function_value),
                    fontSize = 12.sp
                )
            }
            
            // Кнопка setFunctionValue
            Button(
                onClick = {
                    Log.d("FunctionIdActivity", "setFunctionValue clicked")
                    if (functionId.isBlank() || functionValue.isBlank()) {
                        resultText = "Заполните оба поля"
                        return@Button
                    }
                    
                    isLoading = true
                    resultText = "Выполняется setFunctionValue..."
                    
                    try {
                        val id = functionId.toIntOrNull()
                        val value = functionValue.toIntOrNull()
                        
                        if (id == null) {
                            resultText = "Неверный Function ID"
                            return@Button
                        }
                        
                        if (value == null) {
                            resultText = "Неверное значение"
                            return@Button
                        }
                        
                        val success = carService.setFunctionValue(id, value)
                        resultText = "setFunctionValue($id, $value) = $success"
                    } catch (e: Exception) {
                        resultText = "Ошибка: ${e.message}"
                        Log.e("FunctionIdActivity", "Error in setFunctionValue", e)
                    } finally {
                        isLoading = false
                    }
                },
                enabled = !isLoading && carService.isConnected(),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.set_function_value),
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
                        text = resultText.ifBlank { "Результат выполнения функций будет отображаться здесь" },
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
