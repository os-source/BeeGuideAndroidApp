package com.example.beeguide.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beeguide.R
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

        Text(text = "Tipp")
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column {
                Text(
                    text = "Individualisierung!",
                    modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 0.dp),
                    fontSize = 20.sp
                )
                Text(
                    text = "Passe deine Einstellungen an, um BeeGuide nach deinen Bedürfnissen zu gestalten.",
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 20.dp, 0.dp, 0.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Column {
                Text(
                    text = "Melde dich an!",
                    modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 0.dp),
                    fontSize = 20.sp
                )
                Text(
                    text = "Und erstelle dein individuelles Profil.",
                    modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 0.dp),
                )
                Button(onClick = onSignInButtonClicked, modifier = Modifier.padding(10.dp)) {
                    Text(text = stringResource(id = R.string.sign_in))
                }
            }
        }
    }
}