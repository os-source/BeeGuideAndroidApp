package com.example.beeguide.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.beeguide.R
import com.example.beeguide.ui.components.SettingsHeaderDescriptionText
import com.example.beeguide.ui.components.SettingsSingleGroup
import com.example.beeguide.ui.components.SettingsSwitchComp

@Composable
fun PrivacyScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        SettingsHeaderDescriptionText(text = "Behalte die Kontrolle über deine Daten.")
        SettingsSingleGroup {
            SettingsSwitchComp(
                name = R.string.available_maps,
                icon = Icons.Rounded.LocationOn,
                iconDesc = R.string.available_maps,
                state = true,
            ) {
            }
        }
    }
}