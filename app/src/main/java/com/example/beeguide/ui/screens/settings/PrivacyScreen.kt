package com.example.beeguide.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.beeguide.R
import com.example.beeguide.ui.components.SettingsHeaderDescriptionText

@Composable
fun PrivacyScreen() {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp)
    ) {
        SettingsHeaderDescriptionText(text = "Behalte die Kontrolle über deine Daten.")

        Button(onClick = {
            uriHandler.openUri("mailto:beeguide.official@gmail.com")
        }) {
            Text(text = stringResource(id = R.string.request_profile_deletion))
        }
    }

}