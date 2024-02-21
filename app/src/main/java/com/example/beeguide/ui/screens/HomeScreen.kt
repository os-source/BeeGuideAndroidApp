package com.example.beeguide.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.beeguide.ui.viewmodels.TestUiState
import com.example.beeguide.ui.viewmodels.UserUiState

@Composable
fun HomeScreen(
    userUiState: UserUiState,
    testUiState: TestUiState,
    onSignInButtonClicked: () -> Unit,
) {
    /*when (testUiState) {
        is TestUiState.Loading -> {
            Text(text = "Loading...")
            Log.d("TestUiState", "HomeScreen: ${testUiState}")
        }

        is TestUiState.Success -> {*/
            Log.d("TestUiState", "HomeScreen: ")
            Column(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.hello) + " " + "John" + "!",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                )
                Text(
                    text = "Hier erhällst du wichtige Informationen zu neuen Funktionen von BeeGuide.",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp)
                )
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Column {
                        Text(
                            text = "Individualisierung!",
                            modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 0.dp),
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Passe deine Einstellungen an, um BeeGuide nach deinen Bedürfnissen zu gestalten. Du möchtest weniger Benachrichtigungen erhalten? Ein helleres Design? Kein Problem!",
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
                Button(onClick = onSignInButtonClicked) {
                    Text(text = stringResource(id = R.string.sign_in))
                }
            }
    /*}

    else -> {
        Text(text = "Error!")
        Log.d("HomeScreen TestUiState response", testUiState.toString())
    }
}*/
    //TODO: Add home screen
}