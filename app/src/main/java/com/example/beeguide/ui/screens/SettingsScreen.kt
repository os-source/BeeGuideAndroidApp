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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beeguide.R
import com.example.beeguide.helpers.getVersionName
import com.example.beeguide.ui.components.SettingsClickableComp
import com.example.beeguide.ui.components.SettingsGroup

enum class SettingsRoute() {
    edit_profile,
    notifications,
    privacy,
    security,
    appearance,
    help
}

@Composable
fun Settings(
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val settingsUiState = settingsViewModel.settingsUiState
    when (settingsUiState) {
        is SettingsUiState.Loading ->
            Text(text = "Loading...")

        is SettingsUiState.Success ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = stringResource(id = R.string.settings), fontSize = 30.sp)

                SettingsGroup(name = R.string.profile) {
                    SettingsClickableComp(
                        name = R.string.edit_profile,
                        icon = Icons.Rounded.Edit,
                        iconDesc = R.string.edit_profile,
                    ) {
                    }
                }

                SettingsGroup(name = R.string.settings) {
                    SettingsClickableComp(
                        name = R.string.notifications,
                        icon = Icons.Rounded.Notifications,
                        iconDesc = R.string.notifications,
                    ) {
                    }
                    Divider()
                    SettingsClickableComp(
                        name = R.string.privacy,
                        icon = Icons.Rounded.AccountBox,
                        iconDesc = R.string.privacy,
                    ) {
                    }
                    Divider()
                    SettingsClickableComp(
                        name = R.string.security,
                        icon = Icons.Rounded.Lock,
                        iconDesc = R.string.security,
                    ) {
                    }
                }

                SettingsGroup(name = R.string.other) {
                    SettingsClickableComp(
                        name = R.string.appearance,
                        icon = Icons.Rounded.Favorite,
                        iconDesc = R.string.appearance,
                    ) {
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

        else -> Text(text = "Error!")
    }

    //TODO: Add settings page
}

/*
                SettingsSwitchComp(
                    name = R.string.dark_mode,
                    icon = R.drawable.round_explore_24,
                    iconDesc = R.string.dark_mode,
                    state = settingsUiState.darkMode
                ) {
                    settingsViewModel.updateDarkMode()
                }*/