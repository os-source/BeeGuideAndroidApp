package com.example.beeguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.beeguide.navigation.MonitoringActivity
import com.example.beeguide.ui.BeeGuideApp
import com.example.beeguide.ui.theme.BeeGuideTheme

class MainActivity : ComponentActivity() {
    val monitor: MonitoringActivity = MonitoringActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeeGuideTheme {
                BeeGuideApp()
            }
        }

    }
}