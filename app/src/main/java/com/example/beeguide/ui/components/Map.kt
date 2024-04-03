package com.example.beeguide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MapMarker(markerPosition: Pair<Float, Float>, imageSize: Size, description: String) {
    var showDescription by remember { mutableStateOf(false) }

    val offsetX = imageSize.width.times(markerPosition.first).pxToDp() - 6.dp
    val offsetY = imageSize.height.times(markerPosition.second).pxToDp() - 6.dp


    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .clickable { showDescription = !showDescription }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(
                        transparentColor(
                            color = MaterialTheme.colorScheme.primary,
                            alpha = 0.6f
                        )
                    )
            )
            if (showDescription) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = description, fontSize = 8.sp, lineHeight = 10.sp)
            }
        }
    }
}

@Composable
fun UserMarker(markerPosition: Pair<Float, Float>, imageSize: Size) {
    val offsetX = imageSize.width.times(markerPosition.first).pxToDp() - 12.dp
    val offsetY = imageSize.height.times(markerPosition.second).pxToDp() - 12.dp

    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}