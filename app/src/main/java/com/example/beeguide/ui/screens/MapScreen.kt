package com.example.beeguide.ui.screens

import android.util.Log
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.beeguide.R
import com.example.beeguide.navigation.beacons.Monitor
import com.example.beeguide.ui.components.RangedBeaconList
import com.example.beeguide.ui.viewmodels.MapPositionUiState

@Composable
fun MapScreen(
    mapPositionUiState: MapPositionUiState,
) {
    var scale by remember {
        mutableStateOf(1f)
    }
    var rotation by remember {
        mutableStateOf(1f)
    }
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }

    when (mapPositionUiState) {
        is MapPositionUiState.None ->
            Log.d("MapScreen", "MapScreen: None")

        is MapPositionUiState.Success ->
            Log.d("MapScreen", "MapScreen: ${mapPositionUiState.x}")

        else -> Log.d("MapScreen", "MapScreen: Error")
    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)

            rotation += rotationChange

            offset = Offset(
                x = (offset.x + scale * panChange.x),
                y = (offset.y + scale * panChange.y),
            )
        }
        var imageSize by remember { mutableStateOf<Size?>(null) }
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    rotationZ = rotation
                    translationX = offset.x
                    translationY = offset.y
                }
                .transformable(state),
        ){
            Image(
                painter = painterResource(R.drawable.mapnew),
                contentDescription = null,
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        imageSize = Size(coordinates.size.width, coordinates.size.height)
                    }
            )

            imageSize?.let { size ->
                MapMarker(markerPosition = Pair(0.5f, 0.5f), imageSize = size)
                MapMarker(markerPosition = Pair(0.2f, 0.3f), imageSize = size)
                MapMarker(markerPosition = Pair(0.7f, 0.56f), imageSize = size)
                MapMarker(markerPosition = Pair(0.19f, 0.59f), imageSize = size)
                MapMarker(markerPosition = Pair(0.93f, 0.23f), imageSize = size)
            }
        }
    }
    Box (
    ){
        val ctx = LocalContext.current
        val owner = LocalLifecycleOwner.current

        val monitor = Monitor(ctx)
        monitor.startLogging(owner)
        val regionViewModel = monitor.getRegionViewModel()
        RangedBeaconList(regionViewModel = regionViewModel)
    }
}

@Composable
fun MapMarker(markerPosition: Pair<Float, Float>, imageSize: Size) {
    val offsetX = (markerPosition.first * imageSize.width).pxToDp() - 3.dp
    val offsetY = (markerPosition.second * imageSize.height).pxToDp() - 3.dp

    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
    ) {
        Box(
            modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.Red)
        )
    }
}

@Composable
fun Float.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }