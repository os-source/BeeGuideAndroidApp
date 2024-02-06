package com.example.beeguide.ui

import android.content.Context
import android.app.Service
import android.hardware.SensorManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.beeguide.navigation.beacons.Monitor
import com.example.beeguide.ui.components.Navbar
import com.example.beeguide.ui.screens.HomeScreen
import com.example.beeguide.ui.screens.MapScreen
import com.example.beeguide.ui.screens.ProfileScreen
import com.example.beeguide.ui.screens.Settings
import com.example.beeguide.ui.screens.SettingsRoute
import com.example.beeguide.ui.viewmodels.AppearanceViewModel
import com.example.beeguide.ui.viewmodels.MapPositionViewModel
import com.example.beeguide.ui.viewmodels.MapViewModel
import com.example.beeguide.ui.viewmodels.SensorViewModel
import com.example.beeguide.ui.viewmodels.TestViewModel
import com.example.beeguide.ui.viewmodels.UserViewModel


/** enum values that represent the screens in the app */
enum class BeeGuideRoute() {
    Home,
    Map,
    Profile
}

@Composable
fun BeeGuideApp(
    appearanceViewModel: AppearanceViewModel,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            Navbar(
                onHomeIconClicked = {
                    navController.navigate(BeeGuideRoute.Home.name)
                },
                onMapIconClicked = {
                    navController.navigate(BeeGuideRoute.Map.name)
                },
                onProfileIconClicked = {
                    navController.navigate(BeeGuideRoute.Profile.name)
                }
            )
        },
        floatingActionButton = {

        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BeeGuideRoute.Map.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = BeeGuideRoute.Map.name) {
                val monitor = Monitor(LocalContext.current)
                val regionViewModel = monitor.getRegionViewModel()

                val mapViewModel: MapViewModel = viewModel(factory = MapViewModel.Factory)
                val sensorViewModel: SensorViewModel = viewModel(factory = SensorViewModel.Factory)

                val mapPositionViewModel = MapPositionViewModel(regionViewModel = regionViewModel, mapViewModel = mapViewModel, sensorViewModel = sensorViewModel)

                MapScreen(mapPositionUiState = mapPositionViewModel.mapPositionUiState)
            }
            composable(route = BeeGuideRoute.Home.name) {
                val userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
                val testViewModel: TestViewModel = viewModel(factory = TestViewModel.Factory)
                HomeScreen(userUiState = userViewModel.userUiState, testUiState = testViewModel.testUiState)
            }
            composable(route = BeeGuideRoute.Profile.name) {
                val userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)
                ProfileScreen(
                    userUiState = userViewModel.userUiState,
                    onSettingsButtonClicked = { navController.navigate(SettingsRoute.Settings.name) })
            }
            composable(route = SettingsRoute.Settings.name) {
                Settings(appearanceViewModel)
            }
        }
    }
}