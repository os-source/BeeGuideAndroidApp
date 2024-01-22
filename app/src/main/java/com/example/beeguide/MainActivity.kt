package com.example.beeguide

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.beeguide.navigation.preconditions.PermissionChecker
import com.example.beeguide.ui.BeeGuideApp
import com.example.beeguide.ui.viewmodels.AppearanceViewModel
import com.example.beeguide.ui.theme.BeeGuideTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    //private val preferencesDataStore by lazy { PreferencesDataStore(this) }

    private val appearanceViewModel by viewModels<AppearanceViewModel>(
        factoryProducer = {
            AppearanceViewModel.Companion.Factory(true)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferencesDataStore = PreferencesDataStore(this)
        val permissionChecker = PermissionChecker(this); permissionChecker.check()

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("AppearanceViewModel", "create: ${preferencesDataStore.getDarkThemeMode()}")
        }

        setContent {
            BeeGuideTheme(appearanceViewModel) {
                BeeGuideApp(appearanceViewModel)
            }
        }

        //val monitor = Monitor(this); monitor.start()
    }
}

fun getDarkThemeMode(): Boolean {
    CoroutineScope(Dispatchers.IO).launch {
        val preferencesDataStore = PreferencesDataStore(BeeGuideApplication())
        Log.d("AppearanceViewModel", "create: ${preferencesDataStore.getDarkThemeMode()}")
    }
    return true
}