package com.example.beeguide.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Icon(
                    Icons.Rounded.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier
                        .size(35.dp)
                        .clickable(onClick = onSettingsButtonClicked)
                )
            }
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.size(50.dp))
                Text(text = "${userUiState.user.firstName}", fontSize = 32.sp)
                Text(text = "@johnDoe1234", fontSize = 16.sp)
                Spacer(modifier = Modifier.size(40.dp))
                Image(
                    painter = painterResource(id = R.drawable.profile_image_test),
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .clip(
                            CircleShape
                        )
                )
                Spacer(modifier = Modifier.size(30.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clip(RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
                ) {
                    Column {
                        Text(
                            text = "Deine Karte!",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 0.dp),
                            fontSize = 20.sp
                        )
                        Text(
                            text = "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }

        else -> Text(text = "Error!")
    }

    //TODO: Add profile page
}