package com.example.beeguide.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beeguide.R
import com.example.beeguide.ui.viewmodels.TestUiState
import com.example.beeguide.ui.viewmodels.UserUiState

@Composable
fun HomeScreen(
    userUiState: UserUiState,
    testUiState: TestUiState
) {
    when (testUiState) {
        is TestUiState.Loading ->{
            Text(text = "Loading...")
        Log.d("TestUiState", "HomeScreen: ${testUiState}")}

        is TestUiState.Success -> {
            Log.d("TestUiState", "HomeScreen: ${testUiState}")
            Column {
                Text(
                    text = stringResource(id = R.string.hello) + " " + testUiState.test + "!",
                    fontSize = 30.sp,
                    modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 0.dp)
                )
                Text(
                    text = "Hier erhällst du wichtige Informationen zu neuen Funktionen von BeeGuide.",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(10.dp, 10.dp, 10.dp, 10.dp)
                )
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

        else -> {
            Text(text = "Error!")
            Log.d("HomeScreen TestUiState response", testUiState.toString())
        }
    }
    //TODO: Add home screen
}