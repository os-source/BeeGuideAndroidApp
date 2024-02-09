package com.example.beeguide.ui.screens.authentication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.beeguide.R

@Composable
fun SignInScreen(
    onSignUpButtonClicked: () -> Unit,
) {
    Column {
        Text(text = stringResource(id = R.string.sign_in))

        Row {
            Text(
                text = "${stringResource(id = R.string.no_profile_yet)} ",
            )
            Text(
                text = stringResource(id = R.string.sign_up),
                modifier = Modifier.clickable(onClick = onSignUpButtonClicked),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}