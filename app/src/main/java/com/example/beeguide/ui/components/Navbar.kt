package com.example.beeguide.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.beeguide.R

@Composable
fun Navbar() {
    Column {
        Icon(painter = painterResource(R.drawable.navbar_wave),
            contentDescription = null,
            tint = Color.Blue,
            modifier = Modifier
                .fillMaxWidth()
                .scale(1.3f)
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .zIndex(1f),
            color = Color.Blue
        ) {
            Row(
                modifier = Modifier
                    .padding(50.dp, 0.dp, 50.dp, 12.5.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Icon(Icons.Rounded.Home,
                    contentDescription = "Home",
                    Modifier.size(35.dp)
                )

                Icon(
                    painterResource(id = R.drawable.round_explore_24),
                    contentDescription = "Map",
                    Modifier.size(50.dp)
                )

                Icon(Icons.Rounded.Person,
                    contentDescription = "Profile",
                    Modifier.size(35.dp)
                )
            }
        }
    }
}