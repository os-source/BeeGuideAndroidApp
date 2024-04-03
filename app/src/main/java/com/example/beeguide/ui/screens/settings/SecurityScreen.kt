package com.example.beeguide.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beeguide.R
import com.example.beeguide.ui.components.SettingsHeaderDescriptionText
import com.example.beeguide.ui.viewmodels.SignOutViewModel
import com.example.beeguide.ui.viewmodels.UserViewModel

@Composable
fun SecurityScreen(
    userViewModel: UserViewModel,
    signOutViewModel: SignOutViewModel = viewModel(factory = SignOutViewModel.Factory)
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 10.dp)
    ) {
        SettingsHeaderDescriptionText(text = "Sicherheit wird bei BeeGuide groß geschrieben.")

        Button(onClick = {
            userViewModel.clearUser()
            signOutViewModel.signOut()
            Toast.makeText(context, "Du wurdes abgemeldet!", Toast.LENGTH_SHORT).show()
        }) {
            Text(text = stringResource(id = R.string.sign_out))
        }
    }
}