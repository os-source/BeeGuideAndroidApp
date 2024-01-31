package com.example.beeguide.ui.screens

import android.util.Log
import android.util.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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
    var mapScale by remember {
        mutableStateOf(1f)
    }
    var mapRotation by remember {
        mutableStateOf(1f)
    }
    var mapOffset by remember {
        mutableStateOf(Offset.Zero)
    }
    var mapTransformOrigin by remember {
        mutableStateOf(TransformOrigin(0.5f, 0.5f))
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {
        /*val state = rememberTransformableState { zoomChange, panChange, rotationChange ->
            scale = (scale * zoomChange).coerceIn(1f, 5f)

            rotation += rotationChange

            offset = Offset(
                x = (offset.x + scale * panChange.x),
                y = (offset.y + scale * panChange.y),
            )
        }*/
        var imageSize by remember { mutableStateOf<Size?>(null) }
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = mapScale
                    scaleY = mapScale
                    rotationZ = mapRotation
                    translationX = mapOffset.x
                    translationY = mapOffset.y
                    transformOrigin = mapTransformOrigin
                }
                //.transformable(state)
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, rotation ->
                        mapScale *= zoom
                        mapOffset = Offset(
                            x = (mapOffset.x + mapScale * pan.x),
                            y = (mapOffset.y + mapScale * pan.y),
                        )
                        mapTransformOrigin = TransformOrigin((centroid.x / size.width), (centroid.y / size.height))
                        mapRotation += rotation
                    }
                }
        ){
            Icon(
                painter = painterResource(R.drawable.map_k02),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .rotate(180f)
                    .onGloballyPositioned { coordinates ->
                        imageSize = Size(coordinates.size.width, coordinates.size.height)
                    }
            )

            imageSize?.let { size ->
                /*MapMarker(markerPosition = Pair(0.5f, 0.5f), imageSize = size)
                MapMarker(markerPosition = Pair(0.2f, 0.3f), imageSize = size)
                MapMarker(markerPosition = Pair(0.7f, 0.56f), imageSize = size)
                MapMarker(markerPosition = Pair(0.19f, 0.59f), imageSize = size)
                MapMarker(markerPosition = Pair(0.93f, 0.23f), imageSize = size)*/

                when (mapPositionUiState) {
                    is MapPositionUiState.None ->
                        Log.d("MapScreen", "MapScreen: None")

                    is MapPositionUiState.Success -> {
                        Log.d("MapScreen", "MapScreen: ${mapPositionUiState.location}")
                        var xPosition = 1/685f * mapPositionUiState.location.x
                        var yPosition = 1/855f * mapPositionUiState.location.y
                        UserMarker(markerPosition = Pair(xPosition, yPosition), imageSize = size)
                        /*mapPositionUiState.points.forEach { point ->
                            var xPosition = 1/685f * point.x
                            var yPosition = 1/855f * point.y
                            UserMarker(markerPosition = Pair(xPosition, yPosition), imageSize = size)
                        }*/
                    }

                    else -> Log.d("MapScreen", "MapScreen: Error")
                }

                //UserMarker(markerPosition = Pair(xPosition, yPosition), imageSize = size)
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
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(Color.Red)
        )
    }
}

@Composable
fun UserMarker(markerPosition: Pair<Float, Float>, imageSize: Size) {
    val offsetX = (markerPosition.first * imageSize.width).pxToDp() - 3.dp
    val offsetY = (markerPosition.second * imageSize.height).pxToDp() - 3.dp

    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(Color.Blue)
        )
    }
}


@Composable
fun Float.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }