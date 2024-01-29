package com.example.beeguide.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.beeguide.R
import com.example.beeguide.ui.components.SettingsHeaderDescriptionText
import com.example.beeguide.ui.components.SettingsSingleGroup
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
        SettingsHeaderDescriptionText(text = "Passe das Design von BeeGuide an deine Vorlieben an.")
        SettingsSingleGroup {
            SettingsSwitchComp(
                name = R.string.dark_mode,
                icon = Icons.Rounded.Favorite,
                iconDesc = R.string.dark_mode,
                state = appearanceViewModel.darkMode,
            ) {
                appearanceViewModel.update()
            }
        }
    }
}