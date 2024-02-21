package com.example.beeguide.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.beeguide.R
import com.example.beeguide.ui.components.BeeGuideSingleLineTextArea
import com.example.beeguide.ui.components.BeeGuideTextArea
import com.example.beeguide.ui.components.SettingsHeaderDescriptionText
import com.example.beeguide.ui.components.SettingsSingleGroup
import com.example.beeguide.ui.components.SettingsSwitchComp

@Composable
fun EditProfileScreen() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp)
    ) {
        SettingsHeaderDescriptionText(text = "Gestalte dein Profil nach deinen Vorstellungen.")

        BeeGuideSingleLineTextArea(
            value = "johnDoe1234",
            onValueChange = {},
            label = "Benutzername",
        )

        BeeGuideSingleLineTextArea(
            value = "John Doe",
            onValueChange = {},
            label = "Name",
        )

        BeeGuideTextArea(
            value = "Meine Beschreibung",
            onValueChange = {},
            label = "Beschreibung",
            placeholder = "Beschreibe dich in ein paar Worten.",
        )

        SettingsSingleGroup {
            SettingsSwitchComp(
                name = R.string.available_maps,
                icon = Icons.Rounded.LocationOn,
                iconDesc = R.string.available_maps,
                state = true,
            ) {
            }
        }
        SettingsSingleGroup {
            SettingsSwitchComp(
                name = R.string.tipps_and_recomandations,
                icon = Icons.Rounded.Info,
                iconDesc = R.string.tipps_and_recomandations,
                state = true,
            ) {
            }
        }
        SettingsSingleGroup {
            SettingsSwitchComp(
                name = R.string.updates_and_newFeatures,
                icon = Icons.Rounded.ThumbUp,
                iconDesc = R.string.updates_and_newFeatures,
                state = true,
            ) {
            }
        }
    }
}