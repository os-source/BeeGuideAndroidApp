package com.example.beeguide.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.beeguide.R
import com.example.beeguide.ui.components.SettingsGroup
import com.example.beeguide.ui.components.SettingsSwitchComp
import com.example.beeguide.ui.viewmodels.AppearanceViewModel


@Composable
fun AppearanceScreen(
    appearanceViewModel: AppearanceViewModel
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        SettingsGroup(name = R.string.settings) {
            SettingsSwitchComp(
                name = R.string.dark_mode,
                icon = R.drawable.round_explore_24,
                iconDesc = R.string.dark_mode,
                state = appearanceViewModel.darkMode,
            ) {
                appearanceViewModel.update()
            }
        }
    }
}