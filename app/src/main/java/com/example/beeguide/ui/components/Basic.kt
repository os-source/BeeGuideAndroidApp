package com.example.beeguide.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.undraw_sign_in_re_o58h),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(100.dp, 50.dp)
                    .alpha(0.5f),
            )
            Text(text = stringResource(id = R.string.unexpected_error))
        }
    }
}

@Composable
fun BeeGuideCard(
    text: String,
    heading: String? = null,
    label: String? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    label?.let {
        Text(text = label)
        Spacer(modifier = Modifier.height(8.dp))
    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            heading?.let {
                Text(
                    text = heading,
                    fontSize = 20.sp,
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            Text(text = text)
            content()
        }
    }
}