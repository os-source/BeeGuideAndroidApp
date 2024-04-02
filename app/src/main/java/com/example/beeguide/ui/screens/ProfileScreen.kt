package com.example.beeguide.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beeguide.R
import com.example.beeguide.helpers.imageBitmapFromBase64String
import com.example.beeguide.ui.components.BeeGuideCard
import com.example.beeguide.ui.components.BeeGuideCircularProgressIndicator
import com.example.beeguide.ui.components.BeeGuideUnexpectedError
import com.example.beeguide.ui.viewmodels.UserUiState

@Composable
fun ProfileScreen(
    userUiState: UserUiState,
    onSettingsButtonClicked: () -> Unit
) {
    when (userUiState) {
        is UserUiState.Loading -> BeeGuideCircularProgressIndicator()

        is UserUiState.Success -> {
            Log.d("ProfileScreen", "User: ${userUiState.user}")
            Icon(
                painter = painterResource(R.drawable.round_shape),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(1.3f)
            )
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                IconButton(onClick = onSettingsButtonClicked) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = stringResource(R.string.settings),
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(50.dp))

                Text(text = userUiState.user.name, fontSize = 32.sp)
                Text(text = userUiState.user.email, fontSize = 16.sp)

                Spacer(modifier = Modifier.size(40.dp))

                if (userUiState.user.userDetails?.profilePicture != null) {
                    Image(
                        bitmap = imageBitmapFromBase64String(userUiState.user.userDetails.profilePicture),
                        contentDescription = null,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(
                                CircleShape
                            )
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile_image_test),
                        contentDescription = null,
                        modifier = Modifier
                            .size(128.dp)
                            .clip(
                                CircleShape
                            )
                    )
                }

                Spacer(modifier = Modifier.size(30.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    BeeGuideCard(
                        text = userUiState.user.userDetails?.bio
                            ?: "${stringResource(id = R.string.hello_i_am)} ${userUiState.user.name}.",
                        heading = stringResource(id = R.string.about_me),
                    ) {}
                }
            }
        }

        else -> BeeGuideUnexpectedError()
    }
}