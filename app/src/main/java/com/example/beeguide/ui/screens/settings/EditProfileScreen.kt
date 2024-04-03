package com.example.beeguide.ui.screens.settings

import android.content.ContentResolver
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.beeguide.R
import com.example.beeguide.helpers.uriToFile
import com.example.beeguide.ui.components.BeeGuideCircularProgressIndicator
import com.example.beeguide.ui.components.BeeGuideSingleLineTextArea
import com.example.beeguide.ui.components.BeeGuideTextArea
import com.example.beeguide.ui.components.BeeGuideUnexpectedError
import com.example.beeguide.ui.components.SettingsHeaderDescriptionText
import com.example.beeguide.ui.viewmodels.UserUiState
import com.example.beeguide.ui.viewmodels.UserViewModel
import java.io.File

@Composable
fun EditProfileScreen(
    userViewModel: UserViewModel,
    navigateToProfileScreen: () -> Unit,
) {
    val context = LocalContext.current

    var imageFile by remember { mutableStateOf<File?>(null) }

    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        // Convert URI to File
        val contentResolver: ContentResolver = context.contentResolver
        imageFile = uri?.let { uriToFile(contentResolver, it) }
    }

    when (val userUiState = userViewModel.userUiState) {
        is UserUiState.Loading -> BeeGuideCircularProgressIndicator()

        is UserUiState.Success -> {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 10.dp)
            ) {
                SettingsHeaderDescriptionText(text = stringResource(id = R.string.customize_profile_description))

                Button(
                    onClick = {
                        getContent.launch("image/*")
                    }
                ) {
                    Text("Profilbild ändern")
                }

                BeeGuideSingleLineTextArea(
                    value = userUiState.user.name,
                    onValueChange = { userViewModel.nameChanged(it) },
                    label = "Name",
                )

                BeeGuideTextArea(
                    value = userUiState.user.userDetails?.bio ?: "",
                    onValueChange = { userViewModel.bioChanged(it) },
                    label = "Beschreibung",
                    placeholder = "Beschreibe dich in ein paar Worten.",
                )

                Button(onClick = { userViewModel.saveUpdatedUser(navigateToProfileScreen, imageFile) }) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        }

        else -> BeeGuideUnexpectedError()
    }
}