package com.example.beeguide.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.beeguide.R
import com.example.beeguide.ui.components.BeeGuideBottomBar
import com.example.beeguide.ui.components.BeeGuideTopBar
import com.example.beeguide.ui.screens.HomeScreen
import com.example.beeguide.ui.screens.MapScreen
import com.example.beeguide.ui.screens.ProfileScreen
import com.example.beeguide.ui.screens.SettingsScreen
import com.example.beeguide.ui.screens.authentication.SignInScreen
import com.example.beeguide.ui.screens.authentication.SignUpScreen
import com.example.beeguide.ui.screens.settings.AppearanceScreen
import com.example.beeguide.ui.screens.settings.EditProfileScreen
import com.example.beeguide.ui.screens.settings.PrivacyScreen
import com.example.beeguide.ui.screens.settings.SecurityScreen
import com.example.beeguide.ui.viewmodels.AppearanceViewModel
import com.example.beeguide.ui.viewmodels.MapFileViewModel
import com.example.beeguide.ui.viewmodels.MapPositionViewModel
import com.example.beeguide.ui.viewmodels.MapViewModel
import com.example.beeguide.ui.viewmodels.UserViewModel

/** enum values that represent the screens in the app */
enum class BeeGuideRoute(@StringRes val title: Int) {
    Home(R.string.home),
    Map(R.string.map),
    Profile(R.string.profile),
    Settings(R.string.settings),
    Authentication(R.string.sign_in),

    SettingsOverview(R.string.settings),
    EditProfile(R.string.edit_profile),
    Privacy(R.string.privacy),
    Security(R.string.security),
    Appearance(R.string.appearance),

    SignIn(R.string.sign_in),
    SignUp(R.string.sign_up),
}

@Composable
fun BeeGuideApp(
    appearanceViewModel: AppearanceViewModel,
    mapViewModel: MapViewModel,
    mapPositionViewModel: MapPositionViewModel,
    navController: NavHostController = rememberNavController()
) {
    val topBarScreens = listOf<BeeGuideRoute>(
        BeeGuideRoute.SettingsOverview,
        BeeGuideRoute.EditProfile,
        BeeGuideRoute.Privacy,
        BeeGuideRoute.Security,
        BeeGuideRoute.Appearance,
    )
    val bottomBarScreens =
        listOf<BeeGuideRoute>(BeeGuideRoute.Home, BeeGuideRoute.Map, BeeGuideRoute.Profile)

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = BeeGuideRoute.valueOf(
        backStackEntry?.destination?.route ?: BeeGuideRoute.Map.name
    )

    val userViewModel: UserViewModel = viewModel(factory = UserViewModel.Factory)

    Scaffold(
        topBar = {
            if (currentScreen in topBarScreens) {
                BeeGuideTopBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
        },
        bottomBar = {
            if (currentScreen in bottomBarScreens) {
                BeeGuideBottomBar(
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
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BeeGuideRoute.Map.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = BeeGuideRoute.Map.name) {
                val mapFileViewModel: MapFileViewModel =
                    viewModel(factory = MapFileViewModel.Factory)

                MapScreen(
                    mapPositionUiState = mapPositionViewModel.mapPositionUiState,
                    mapFileUiState = mapFileViewModel.mapFileUiState,
                    mapViewModel = mapViewModel
                )
            }
            composable(route = BeeGuideRoute.Home.name) {
                HomeScreen(
                    userUiState = userViewModel.userUiState,
                    onSignInButtonClicked = { navController.navigate(BeeGuideRoute.Authentication.name) }
                )
            }
            composable(route = BeeGuideRoute.Profile.name) {
                ProfileScreen(
                    userUiState = userViewModel.userUiState,
                    onSettingsButtonClicked = { navController.navigate(BeeGuideRoute.Settings.name) }
                )
            }
            navigation(
                route = BeeGuideRoute.Settings.name,
                startDestination = BeeGuideRoute.SettingsOverview.name,
            ) {
                composable(route = BeeGuideRoute.SettingsOverview.name) {
                    SettingsScreen(
                        onEditProfileSettingsClicked = { navController.navigate(BeeGuideRoute.EditProfile.name) },
                        onPrivacySettingsClicked = { navController.navigate(BeeGuideRoute.Privacy.name) },
                        onSecuritySettingsClicked = { navController.navigate(BeeGuideRoute.Security.name) },
                        onAppearanceSettingsClicked = { navController.navigate(BeeGuideRoute.Appearance.name) },
                    )
                }
                composable(route = BeeGuideRoute.EditProfile.name) {
                    EditProfileScreen(
                        userViewModel = userViewModel,
                        navigateToProfileScreen = { navController.navigate(BeeGuideRoute.Profile.name) },
                    )
                }
                composable(route = BeeGuideRoute.Privacy.name) {
                    PrivacyScreen()
                }
                composable(route = BeeGuideRoute.Security.name) {
                    SecurityScreen(
                        userViewModel = userViewModel
                    )
                }
                composable(route = BeeGuideRoute.Appearance.name) {
                    AppearanceScreen(appearanceViewModel)
                }
            }
            navigation(
                route = BeeGuideRoute.Authentication.name,
                startDestination = BeeGuideRoute.SignIn.name,
            ) {
                composable(route = BeeGuideRoute.SignIn.name) {
                    SignInScreen(
                        userViewModel = userViewModel,
                        onSignUpButtonClicked = { navController.navigate(BeeGuideRoute.SignUp.name) },
                        navigateToHomeScreen = { navController.navigate(BeeGuideRoute.Home.name) },
                    )
                }
                composable(route = BeeGuideRoute.SignUp.name) {
                    SignUpScreen(
                        userViewModel = userViewModel,
                        onSignInButtonClicked = { navController.navigate(BeeGuideRoute.SignIn.name) },
                        navigateToHomeScreen = { navController.navigate(BeeGuideRoute.Home.name) },
                    )
                }
            }
        }

        // bottom navigation bar top icon
        if (currentScreen in bottomBarScreens) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Icon(
                    painter = painterResource(R.drawable.navbar_wave),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(1.3f)
                )
            }
        }
    }
}