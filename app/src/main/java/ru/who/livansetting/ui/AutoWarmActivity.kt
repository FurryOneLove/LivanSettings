package ru.who.livansetting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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

class AutoWarmActivity : ComponentActivity() {
    companion object {
        fun createIntent(context: Context) = Intent(context, AutoWarmActivity::class.java)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LivanSettingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AutoWarmScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AutoWarmScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("livan_settings", Context.MODE_PRIVATE) }
    
    var driverEnabled by remember { mutableStateOf(sharedPrefs.getBoolean("driver_auto_warm_enabled", false)) }
    var passengerEnabled by remember { mutableStateOf(sharedPrefs.getBoolean("passenger_auto_warm_enabled", false)) }

    LaunchedEffect(driverEnabled, passengerEnabled) {
        sharedPrefs.edit()
            .putBoolean("driver_auto_warm_enabled", driverEnabled)
            .putBoolean("passenger_auto_warm_enabled", passengerEnabled)
            .apply()
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        IconButton(onClick = { (context as ComponentActivity).finish() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }
        
        Text(text = stringResource(R.string.auto_warm_settings), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        
        LazyColumn {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { driverEnabled = !driverEnabled },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.enable_driver_seat_auto_warm), Modifier.weight(1f))
                    Switch(checked = driverEnabled, onCheckedChange = { driverEnabled = it })
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().clickable { passengerEnabled = !passengerEnabled },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.enable_passenger_seat_auto_warm), Modifier.weight(1f))
                    Switch(checked = passengerEnabled, onCheckedChange = { passengerEnabled = it })
                }
            }
        }
    }
}
