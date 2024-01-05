package com.example.beeguide.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ProfileScreen(
    userUiState: UserUiState,
    onSettingsButtonClicked: () -> Unit
) {
    when (userUiState) {
        is UserUiState.Loading ->
            Text(text = "Loading...")

        is UserUiState.Success ->
            Column {
                Text(text = "${userUiState.user.firstName} ${userUiState.user.lastName}")
                Button(onClick = onSettingsButtonClicked) {
                    Text(text = "Settings")
                }
            }


        else -> Text(text = "Error!")
    }

    //TODO: Add profile page
}