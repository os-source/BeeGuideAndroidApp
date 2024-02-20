package com.example.beeguide.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SettingsClickableComp(
    icon: ImageVector,
    @StringRes iconDesc: Int,
    @StringRes name: Int,
    onClick: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp, 0.dp, 8.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = stringResource(id = iconDesc),
                    modifier = Modifier
                        .size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = name),
                    modifier = Modifier
                        .padding(12.dp),
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.weight(1.0f))
            Icon(
                Icons.Rounded.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Composable
fun SettingsSwitchComp(
    icon: ImageVector,
    @StringRes iconDesc: Int,
    @StringRes name: Int,
    state: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 0.dp, 8.dp, 0.dp),
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = stringResource(id = iconDesc),
                    modifier = Modifier
                        .size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(id = name),
                    modifier = Modifier.padding(12.dp),
                    textAlign = TextAlign.Start,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = state,
                onCheckedChange = { onClick() }
            )
        }
    }
}

@Composable
fun SettingsSingleGroup(
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingsGroup(
    @StringRes name: Int,
    // to accept only composables compatible with column
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Text(stringResource(id = name))
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp))
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
fun SettingsHeaderDescriptionText(
    text: String,
) {
    Text(
        text = text, modifier = Modifier
            .padding(vertical = 8.dp)
    )
}