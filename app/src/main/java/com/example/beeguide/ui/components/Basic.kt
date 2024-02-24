package com.example.beeguide.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.beeguide.R

@Composable
fun BeeGuideCircularProgressIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun BeeGuideUnexpectedError() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.undraw_sign_in_re_o58h),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(100.dp, 50.dp)
                    .alpha(0.5f),
            )
            Text(text = "An unexpected error occurred")
        }
    }
}