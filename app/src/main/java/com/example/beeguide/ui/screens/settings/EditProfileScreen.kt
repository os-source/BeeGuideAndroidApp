package com.example.beeguide.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.beeguide.R
import com.example.beeguide.ui.components.BeeGuideCircularProgressIndicator
import com.example.beeguide.ui.components.BeeGuideSingleLineTextArea
import com.example.beeguide.ui.components.BeeGuideTextArea
import com.example.beeguide.ui.components.BeeGuideUnexpectedError
import com.example.beeguide.ui.components.SettingsHeaderDescriptionText
import com.example.beeguide.ui.viewmodels.UserUiState
import com.example.beeguide.ui.viewmodels.UserViewModel

@Composable
fun EditProfileScreen(
    userViewModel: UserViewModel,
    navigateToProfileScreen: () -> Unit,
) {
    when (val userUiState = userViewModel.userUiState) {
        is UserUiState.Loading -> BeeGuideCircularProgressIndicator()

        is UserUiState.Success -> {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 10.dp)
            ) {
                SettingsHeaderDescriptionText(text = stringResource(id = R.string.customize_profile_description))

                BeeGuideSingleLineTextArea(
                    value = userUiState.user.name,
                    onValueChange = { userViewModel.nameChanged(it) },
                    label = "Name",
                )

                BeeGuideTextArea(
                    value = userUiState.user.userDetails?.bio?:"",
                    onValueChange = { userViewModel.bioChanged(it) },
                    label = "Beschreibung",
                    placeholder = "Beschreibe dich in ein paar Worten.",
                )
                
                Button(onClick = { userViewModel.saveUpdatedUser(navigateToProfileScreen) }) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        }

        else -> BeeGuideUnexpectedError()
    }
}