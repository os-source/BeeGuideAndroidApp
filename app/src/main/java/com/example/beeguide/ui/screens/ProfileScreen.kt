package com.example.beeguide.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Profile(userUiState: UserUiState
) {
    when (userUiState) {
        is UserUiState.Loading ->
            Text(text = "Loading...")

        is UserUiState.Success ->
            Text(text = "${userUiState.user.firstName} ${userUiState.user.lastName}")

        else -> Text(text = "Error!")
    }
    //TODO: Add profile page
}