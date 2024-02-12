package com.example.beeguide.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.beeguide.R
import com.example.beeguide.ui.BeeGuideRoute

@Composable
fun BeeGuideBottomBar(
    onHomeIconClicked: () -> Unit,
    onMapIconClicked: () -> Unit,
    onProfileIconClicked: () -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .zIndex(1f),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(50.dp, 0.dp, 50.dp, 12.5.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Icon(
                Icons.Rounded.Home,
                contentDescription = "Home",
                modifier = Modifier
                    .size(35.dp)
                    .clickable(onClick = onHomeIconClicked)
            )
            Icon(
                painterResource(id = R.drawable.round_explore_24),
                contentDescription = "Map",
                modifier = Modifier
                    .size(50.dp)
                    .clickable(onClick = onMapIconClicked)
            )
            Icon(
                Icons.Rounded.Person,
                contentDescription = "Profile",
                modifier = Modifier
                    .size(35.dp)
                    .clickable(onClick = onProfileIconClicked)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeeGuideTopBar(
    modifier: Modifier = Modifier,
    currentScreen: BeeGuideRoute,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(stringResource(id = currentScreen.title))
        },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.back_button),
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun BeeGuideFloatingActionButtton(
    fabIcon: ImageVector,
    @StringRes fabDescription: Int,
    onFabClicked: () -> Unit,
) {
    Surface(
        onClick = onFabClicked,
        modifier = Modifier
            .padding(10.dp)
            .clip(CircleShape)
    ) {
        Icon(
            fabIcon,
            contentDescription = stringResource(id = fabDescription),
            modifier = Modifier
                .padding(15.dp)
                .size(35.dp)
        )
    }
}