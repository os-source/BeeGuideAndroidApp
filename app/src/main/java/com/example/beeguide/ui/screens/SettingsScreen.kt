package com.example.beeguide.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.beeguide.R
import com.example.beeguide.helpers.getVersionName
import com.example.beeguide.ui.components.SettingsClickableComp
import com.example.beeguide.ui.components.SettingsGroup
import com.example.beeguide.ui.screens.settings.AboutScreen
import com.example.beeguide.ui.screens.settings.AppearanceScreen
import com.example.beeguide.ui.screens.settings.EditProfileScreen
import com.example.beeguide.ui.screens.settings.NotificationsScreen
import com.example.beeguide.ui.screens.settings.PrivacyScreen
import com.example.beeguide.ui.screens.settings.SecurityScreen

/** enum values that represent the settings screens in the app */
enum class SettingsRoute() {
    Settings,
    EditProfile,
    Notifications,
    Privacy,
    Security,
    Appearance,
    About
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    appearanceViewModel: AppearanceViewModel,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.settings))
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SettingsRoute.Settings.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = SettingsRoute.Settings.name) {
                SettingsScreen(navController)
            }
            composable(route = SettingsRoute.EditProfile.name) {
                EditProfileScreen()
            }
            composable(route = SettingsRoute.Notifications.name) {
                NotificationsScreen()
            }
            composable(route = SettingsRoute.Privacy.name) {
                PrivacyScreen()
            }
            composable(route = SettingsRoute.Security.name) {
                SecurityScreen()
            }
            composable(route = SettingsRoute.Appearance.name) {
                AppearanceScreen(appearanceViewModel)
            }
            composable(route = SettingsRoute.About.name) {
                AboutScreen()
            }
        }
    }
}

@Composable
fun SettingsScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        SettingsGroup(name = R.string.profile) {
            SettingsClickableComp(
                name = R.string.edit_profile,
                icon = Icons.Rounded.Edit,
                iconDesc = R.string.edit_profile,
            ) {
                navController.navigate(SettingsRoute.EditProfile.name)
            }
        }

        SettingsGroup(name = R.string.settings) {
            SettingsClickableComp(
                name = R.string.notifications,
                icon = Icons.Rounded.Notifications,
                iconDesc = R.string.notifications,
            ) {
                navController.navigate(SettingsRoute.Notifications.name)
            }
            Divider()
            SettingsClickableComp(
                name = R.string.privacy,
                icon = Icons.Rounded.AccountBox,
                iconDesc = R.string.privacy,
            ) {
                navController.navigate(SettingsRoute.Privacy.name)
            }
            Divider()
            SettingsClickableComp(
                name = R.string.security,
                icon = Icons.Rounded.Lock,
                iconDesc = R.string.security,
            ) {
                navController.navigate(SettingsRoute.Security.name)
            }
        }

        SettingsGroup(name = R.string.other) {
            SettingsClickableComp(
                name = R.string.appearance,
                icon = Icons.Rounded.Favorite,
                iconDesc = R.string.appearance,
            ) {
                navController.navigate(SettingsRoute.Appearance.name)
            }
        }

        SettingsGroup(name = R.string.about) {
            SettingsClickableComp(
                name = R.string.share_beeguide,
                icon = Icons.Rounded.Share,
                iconDesc = R.string.share_beeguide,
            ) {
            }
            Divider()
            SettingsClickableComp(
                name = R.string.rate_beeguide,
                icon = Icons.Rounded.Star,
                iconDesc = R.string.rate_beeguide,
            ) {
            }
            Divider()
            SettingsClickableComp(
                name = R.string.help,
                icon = Icons.Rounded.Build,
                iconDesc = R.string.help,
            ) {
            }
            Divider()
            SettingsClickableComp(
                name = R.string.about,
                icon = Icons.Rounded.Info,
                iconDesc = R.string.about,
            ) {
                navController.navigate(SettingsRoute.About.name)
            }
        }

        /*Button(onClick = { /*TODO*/ }) {
            Text(stringResource(id = R.string.sign_out))
        }*/

        Text(
            text = "Version ${getVersionName(context = LocalContext.current)}",
            color = Color(0xFF858585),
            fontSize = 12.sp,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}