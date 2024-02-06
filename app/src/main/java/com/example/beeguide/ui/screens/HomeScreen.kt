package com.example.beeguide.ui.screens

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.beeguide.ui.viewmodels.TestUiState
import com.example.beeguide.ui.viewmodels.UserUiState

@Composable
fun HomeScreen(
    userUiState: UserUiState,
    testUiState: TestUiState
) {
    when (userUiState) {
        is UserUiState.Loading -> {
            Text(text = "Loading...")
        }

        is UserUiState.Success -> {
            LaunchWebView(userUiState.user.lastName)
        }

        else -> {
            Text(text = "Error!")
        }
    }

    when (testUiState) {
        is TestUiState.Loading -> {
            Text(text = "Loading...")
            Log.d("TestUiState", "HomeScreen: ${testUiState}")
        }

        is TestUiState.Success -> {
            Log.d("TestUiState", "HomeScreen: ${testUiState}")
            /*Column {
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
            }*/
        }

        else -> {
            Text(text = "Error!")
            Log.d("HomeScreen TestUiState response", testUiState.toString())
        }
    }
    //TODO: Add home screen
}

@Composable
fun LaunchWebView(htmlContent: String) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AndroidView(factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null)
            }
        })
    }
}