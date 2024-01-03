package com.example.beeguide

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.beeguide.ui.components.Navbar
import com.example.beeguide.ui.screens.Home
import com.example.beeguide.ui.screens.NavigationMap
import com.example.beeguide.ui.screens.Profile
import com.example.beeguide.ui.screens.Settings


/** enum values that represent the screens in the app */
enum class BeeGuideRoute() {
    Home,
    Map,
    Profile,
    Settings
}

@Composable
fun BeeGuideApp(
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
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    NavigationMap()
                }
            }
            composable(route = BeeGuideRoute.Home.name) {
                Home()
            }
            composable(route = BeeGuideRoute.Profile.name) {
                Profile()
            }
            composable(route = BeeGuideRoute.Settings.name) {
                Settings()
            }
        }
    }
}