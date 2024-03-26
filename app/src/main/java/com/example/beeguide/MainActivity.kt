package com.example.beeguide

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beeguide.navigation.beacons.Monitor
import com.example.beeguide.navigation.preconditions.PermissionChecker
import com.example.beeguide.ui.BeeGuideApp
import com.example.beeguide.ui.theme.BeeGuideTheme
import com.example.beeguide.ui.viewmodels.AppearanceViewModel
import com.example.beeguide.ui.viewmodels.MapPositionViewModel
import com.example.beeguide.ui.viewmodels.MapViewModel
import com.example.beeguide.ui.viewmodels.MovingViewModel
import com.example.beeguide.ui.viewmodels.sensorviewmodels.AccelerationSensorViewModel
import com.example.beeguide.ui.viewmodels.sensorviewmodels.CompassViewModel
import com.example.beeguide.ui.viewmodels.sensorviewmodels.RotationSensorViewModel
import com.example.beeguide.ui.viewmodels.sensorviewmodels.UncalibratedAccelerationSensorViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val appearanceViewModel by viewModels<AppearanceViewModel>(
        factoryProducer = {
            AppearanceViewModel.Companion.Factory(true)
        }
    )

    private val mapViewModel by viewModels<MapViewModel>(
        factoryProducer = {
            MapViewModel.Companion.Factory
        }
    )

    private var mapPositionViewModel: MapPositionViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferencesDataStore = PreferencesDataStore(this)
        val permissionChecker = PermissionChecker(this); permissionChecker.check()
        val monitor = Monitor(this.applicationContext)
        val regionViewModel = monitor.getRegionViewModel()

        val accelerationSensorViewModel by viewModels<AccelerationSensorViewModel>(
            factoryProducer = {
                AccelerationSensorViewModel.Factory
            }
        )

        val uncalibratedAccelerationSensorViewModel by viewModels<UncalibratedAccelerationSensorViewModel>(
            factoryProducer = {
                UncalibratedAccelerationSensorViewModel.Factory
            }
        )

        val rotationSensorViewModel by viewModels<RotationSensorViewModel>(
            factoryProducer = {
                RotationSensorViewModel.Factory
            }
        )

        val compassViewModel by viewModels<CompassViewModel>(
            factoryProducer = {
                CompassViewModel.Factory
            }
        )

        val movingViewModel: MovingViewModel = MovingViewModel(uncalibratedAccelerationSensorViewModel)

        mapPositionViewModel = MapPositionViewModel(
            regionViewModel = regionViewModel,
            mapViewModel = mapViewModel,
            accelerationSensorViewModel = accelerationSensorViewModel,
            movingViewModel = movingViewModel,
            rotationSensorViewModel = rotationSensorViewModel,
            compassViewModel = compassViewModel
        )

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("AppearanceViewModel", "create: ${preferencesDataStore.getDarkThemeMode()}")
        }

        setContent {
            BeeGuideTheme(appearanceViewModel) {
                BeeGuideApp(
                    appearanceViewModel = appearanceViewModel,
                    mapViewModel = mapViewModel,
                    mapPositionViewModel = mapPositionViewModel!!
                )
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