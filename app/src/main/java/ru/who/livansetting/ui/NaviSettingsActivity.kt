package ru.who.livansetting.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
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
import ru.who.livansetting.core.MainService
import ru.who.livansetting.data.SettingsManager
import ru.who.livansetting.ui.theme.LivanSettingTheme

/**
 * Экран настроек вывода навигации на приборку (DIM).
 */
class NaviSettingsActivity : ComponentActivity() {
    companion object {
        fun createIntent(context: Context) = Intent(context, NaviSettingsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LivanSettingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NaviSettingsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun NaviSettingsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val settings = remember { SettingsManager(context) }

    var enabled by remember { mutableStateOf(settings.isDimNaviEnabled()) }
    var finishingEnabled by remember { mutableStateOf(settings.getDimNaviFinishingEst() > 0) }
    var testRunning by remember { mutableStateOf(MainService.getInstance()?.getDimNaviManager()?.isTestRouteRunning() == true) }
    var sourceActive by remember { mutableStateOf(false) }

    // Периодически опрашиваем статус источника (приходят ли broadcast от патчера).
    LaunchedEffect(enabled) {
        while (true) {
            sourceActive = MainService.getInstance()?.getDimNaviManager()?.isSourceActive() == true
            kotlinx.coroutines.delay(2000)
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { (context as ComponentActivity).finish() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
            Text(
                text = stringResource(R.string.dim_navi_title),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (sourceActive)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(
                                if (sourceActive) R.string.dim_navi_source_active
                                else R.string.dim_navi_source_inactive
                            ),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        if (!sourceActive) {
                            Text(
                                text = stringResource(R.string.dim_navi_source_inactive_desc),
                                fontSize = 13.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.dim_navi_enable),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = stringResource(R.string.dim_navi_enable_desc),
                                fontSize = 13.sp
                            )
                        }
                        Switch(
                            checked = enabled,
                            onCheckedChange = {
                                enabled = it
                                settings.setDimNaviEnabled(it)
                                MainService.getInstance()?.refreshDimNavi()
                            }
                        )
                    }
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.dim_navi_finishing),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = stringResource(R.string.dim_navi_finishing_desc),
                                fontSize = 13.sp
                            )
                        }
                        Switch(
                            checked = finishingEnabled,
                            enabled = enabled,
                            onCheckedChange = {
                                finishingEnabled = it
                                // 50 м — типичный порог финишной прямой.
                                settings.setDimNaviFinishingEst(if (it) 50 else -1)
                            }
                        )
                    }
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = stringResource(R.string.dim_navi_test),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = stringResource(R.string.dim_navi_test_desc),
                            fontSize = 13.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = {
                                val mgr = MainService.getInstance()?.getDimNaviManager()
                                testRunning = mgr?.toggleTestRoute() == true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                stringResource(
                                    if (testRunning) R.string.dim_navi_test_stop
                                    else R.string.dim_navi_test_start
                                )
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = stringResource(R.string.dim_navi_hint),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
