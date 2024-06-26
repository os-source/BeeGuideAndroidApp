package com.example.beeguide.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.example.beeguide.ui.viewmodels.AppearanceViewModel

val DarkColorScheme = darkColorScheme(
    primary = primary,
    secondary = secondary,
    tertiary = primary,
    background = background_dark,
    surface = surface_dark,
    onPrimary = light,
    onSecondary = light,
    onTertiary = light,
    onBackground = light,
    onSurface = light,
)

val LightColorScheme = lightColorScheme(
    primary = primary,
    secondary = secondary,
    tertiary = primary,
    background = background_light,
    surface = surface_light,
    onPrimary = light,
    onSecondary = light,
    onTertiary = light,
    onBackground = dark,
    onSurface = dark,
)

@Composable
fun BeeGuideTheme(
    appearanceViewModel: AppearanceViewModel,
    dynamicColor: Boolean = false, // Disabled
    content: @Composable () -> Unit
) {
    val darkTheme: Boolean = appearanceViewModel.darkMode

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}