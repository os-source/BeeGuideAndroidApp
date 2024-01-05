package com.example.beeguide.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beeguide.R
import com.example.beeguide.ui.components.SettingsGroup
import com.example.beeguide.ui.components.SettingsSwitchComp
import com.example.beeguide.ui.screens.SettingsUiState
import com.example.beeguide.ui.screens.SettingsViewModel

@Composable
fun AppearanceScreen(
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
                SettingsGroup(name = R.string.settings) {
                    SettingsSwitchComp(
                        name = R.string.dark_mode,
                        icon = R.drawable.round_explore_24,
                        iconDesc = R.string.dark_mode,
                        state = settingsUiState.darkMode
                    ) {
                        settingsViewModel.updateDarkMode()
                    }
                }
            }

        else -> Text(text = "Error!")
    }
}