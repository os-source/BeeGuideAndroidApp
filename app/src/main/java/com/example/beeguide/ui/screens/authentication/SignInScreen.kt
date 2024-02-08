package com.example.beeguide.ui.screens.authentication

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.beeguide.R

@Composable
fun SignInScreen() {
    Text(text = stringResource(id = R.string.sign_in))
}