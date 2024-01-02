package com.example.beeguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.beeguide.ui.components.BeeguideRoute
import com.example.beeguide.ui.components.Navbar
import com.example.beeguide.ui.components.NavigationMap
import com.example.beeguide.ui.theme.BeeGuideTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeeGuideTheme {
                BeeGuideApp()
            }
        }
    }
}

@Composable
fun BeeGuideApp(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            Navbar(
                onHomeIconClicked = {
                    navController.navigate(BeeguideRoute.Home.name)
                },
                onMapIconClicked = {
                    navController.navigate(BeeguideRoute.Map.name)
                },
                onProfileIconClicked = {
                    navController.navigate(BeeguideRoute.Profile.name)
                }
            )
        },
        floatingActionButton = {

        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = BeeguideRoute.Map.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = BeeguideRoute.Map.name) {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    NavigationMap()
                }
            }
            composable(route = BeeguideRoute.Home.name) {
                Home()
            }
            composable(route = BeeguideRoute.Profile.name) {
                Profile()
            }
        }
    }
}

@Composable
fun Home() {
    Text(text = "Home")
}

@Composable
fun Profile() {
    Text(text = "Profile")
}