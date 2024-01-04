package com.example.beeguide.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Home(
    userUiState: UserUiState
) {
    when (userUiState) {
        is UserUiState.Loading ->
            Text(text = "Loading...")

        is UserUiState.Success ->
            Text(text = "Hallo ${userUiState.user.firstName}!")

        else -> Text(text = "Error!")
    }
    //TODO: Add home screen
}