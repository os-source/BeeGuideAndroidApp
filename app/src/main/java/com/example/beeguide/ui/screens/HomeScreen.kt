package com.example.beeguide.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beeguide.R
import com.example.beeguide.ui.components.BeeGuideCard
import com.example.beeguide.ui.viewmodels.UserUiState

@Composable
fun HomeScreen(
    userUiState: UserUiState,
    onSignInButtonClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 10.dp)
    ) {
        val welcomeText = if (userUiState is UserUiState.Success) {
            "${stringResource(id = R.string.hello)} ${userUiState.user.name}!"
        } else {
            "${stringResource(id = R.string.hello)}!"
        }

        Text(
            text = welcomeText,
            fontSize = 30.sp,
            lineHeight = 38.sp,
            modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp),
        )

        Text(
            text = "Hier erhällst du wichtige Informationen zu neuen Funktionen von BeeGuide.",
            fontSize = 18.sp,
            modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        BeeGuideCard(
            text = "Passe deine Einstellungen an, um BeeGuide nach deinen Bedürfnissen zu gestalten.",
            heading = "Individualisierung!",
            label = "Tipp"
        ) {}

        Spacer(modifier = Modifier.height(20.dp))

        BeeGuideCard(
            text = "Und erstelle dein individuelles Profil.",
            heading = "Melde dich an!",
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Button(onClick = onSignInButtonClicked) {
                Text(text = stringResource(id = R.string.sign_in))
            }
        }
    }
}