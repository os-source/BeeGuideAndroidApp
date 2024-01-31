package com.example.beeguide.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import com.example.beeguide.R
import com.example.beeguide.ui.viewmodels.UserUiState

@Composable
fun ProfileScreen(
    userUiState: UserUiState,
    onSettingsButtonClicked: () -> Unit

) {
    when (userUiState) {
        is UserUiState.Loading ->
            Text(text = "Loading...")

        is UserUiState.Success -> {

            Icon(
                painter = painterResource(R.drawable.round_shape),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(1.3f)
            )
            Column {
                Text(text = "${userUiState.user.firstName} ${userUiState.user.lastName}")
                Button(onClick = onSettingsButtonClicked) {
                    Text(text = "Settings")
                }
            }
        }

        else -> Text(text = "Error!")
    }

    //TODO: Add profile page
}