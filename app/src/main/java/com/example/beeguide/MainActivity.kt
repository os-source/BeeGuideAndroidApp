package com.example.beeguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.beeguide.ui.components.NavigationMap
import com.example.beeguide.ui.theme.BeeGuideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeeGuideTheme {
                NavigationMap()
            }
        }
    }
}