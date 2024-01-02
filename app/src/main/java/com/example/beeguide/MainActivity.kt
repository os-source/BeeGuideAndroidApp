package com.example.beeguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.beeguide.ui.components.Navbar
import com.example.beeguide.ui.components.NavigationMap
import com.example.beeguide.ui.theme.BeeGuideTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeeGuideTheme {
                Scaffold(
                    bottomBar = {
                        Navbar()
                    }
                ) {
                        innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                    ) {
                        NavigationMap()
                    }
                }
            }
        }
    }
}