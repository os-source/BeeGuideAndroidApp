package com.example.beeguide

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.beeguide.navigation.MonitoringActivity
import com.example.beeguide.ui.BeeGuideApp
import com.example.beeguide.ui.screens.AppearanceViewModel
import com.example.beeguide.ui.theme.BeeGuideTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    //private val preferencesDataStore by lazy { PreferencesDataStore(this) }

    private val appearanceViewModel by viewModels<AppearanceViewModel>(
        factoryProducer = {
            AppearanceViewModel.Companion.Factory(false)
        }
    )

    val monitor: MonitoringActivity = MonitoringActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var preferencesDataStore = PreferencesDataStore(this)

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("AppearanceViewModel", "create: ${preferencesDataStore.getDarkThemeMode()}")
        }

        setContent {
            BeeGuideTheme(appearanceViewModel) {
                BeeGuideApp(appearanceViewModel)
            }
        }

    }
}

fun getDarkThemeMode(): Boolean {
    CoroutineScope(Dispatchers.IO).launch {
        val preferencesDataStore = PreferencesDataStore(BeeGuideApplication())
        Log.d("AppearanceViewModel", "create: ${preferencesDataStore.getDarkThemeMode()}")
    }
    return true
}