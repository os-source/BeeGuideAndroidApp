package com.example.beeguide.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity

@Composable
fun Float.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun transparentColor(color: Color, alpha: Float): Color {
    val red = color.red
    val green = color.green
    val blue = color.blue

    return Color(red = red, green = green, blue = blue, alpha = alpha)
}