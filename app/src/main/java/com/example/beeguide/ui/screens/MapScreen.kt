package com.example.beeguide.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beeguide.R
import com.example.beeguide.helpers.svgStringToImageBitmap
import com.example.beeguide.ui.components.BeeGuideCircularProgressIndicator
import com.example.beeguide.ui.viewmodels.MapFileUiState
import com.example.beeguide.ui.viewmodels.MapPositionUiState
import com.example.beeguide.ui.viewmodels.MapUiState
import com.example.beeguide.ui.viewmodels.MapViewModel
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MapScreen(
    mapPositionUiState: MapPositionUiState,
    mapFileUiState: MapFileUiState,
    mapViewModel: MapViewModel,
) {
    val context = LocalContext.current

    val mapUiState by mapViewModel.mapUiState.collectAsState()

    when (val state = mapUiState) {
        is MapUiState.Success -> {
            when (mapFileUiState) {
                is MapFileUiState.Loading -> {
                    BeeGuideCircularProgressIndicator()
                }

                is MapFileUiState.Success -> {
                    fun Offset.rotateBy(angle: Float): Offset {
                        val angleInRadians = angle * PI / 180
                        return Offset(
                            (x * cos(angleInRadians) - y * sin(angleInRadians)).toFloat(),
                            (x * sin(angleInRadians) + y * cos(angleInRadians)).toFloat()
                        )
                    }

                    var mapOffset by remember { mutableStateOf(Offset.Zero) }
                    var mapZoom by remember { mutableFloatStateOf(1f) }
                    var mapAngle by remember { mutableFloatStateOf(0f) }

                    Box(
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTransformGestures(
                                    onGesture = { centroid, pan, gestureZoom, gestureRotate ->
                                        val oldScale = mapZoom
                                        val newScale = mapZoom * gestureZoom

                                        mapOffset =
                                            (mapOffset + centroid / oldScale).rotateBy(gestureRotate) - (centroid / newScale + pan / oldScale)

                                        mapZoom = newScale

                                        mapAngle += gestureRotate
                                    }
                                )
                            }
                            .graphicsLayer {
                                translationX = -mapOffset.x * mapZoom
                                translationY = -mapOffset.y * mapZoom
                                scaleX = mapZoom
                                scaleY = mapZoom
                                rotationZ = mapAngle
                                transformOrigin = TransformOrigin(0f, 0f)
                            }
                            .fillMaxSize()
                    ) {
                        // convert svg string to image bitmap
                        val imageBitmap = svgStringToImageBitmap(mapFileUiState.mapFile)

                        val imageSize =
                            Size(imageBitmap.width.toFloat(), imageBitmap.height.toFloat())

                        // draw image
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawImage(
                                imageBitmap,
                                topLeft = Offset(0f, 0f),
                            )
                        }

                        imageSize.let { size ->
                            // draw points of interest
                            state.map.pois.forEach { poi ->
                                val xPosition =
                                    1 / state.map.xAxis.toFloat() * poi.x
                                val yPosition =
                                    1 / state.map.yAxis.toFloat() * poi.y

                                MapMarker(
                                    markerPosition = Pair(xPosition, yPosition),
                                    imageSize = size,
                                    description = poi.description,
                                )
                            }

                            // draw current user position
                            var mapPosition: MutableState<Pair<Float, Float>?> =
                                remember { mutableStateOf(null) }

                            when (mapPositionUiState) {
                                is MapPositionUiState.None -> {
                                    Toast.makeText(
                                        context,
                                        stringResource(id = R.string.no_position_found),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                is MapPositionUiState.Success -> {
                                    val xPosition =
                                        1 / state.map.xAxis.toFloat() * mapPositionUiState.location.x
                                    val yPosition =
                                        1 / state.map.yAxis.toFloat() * mapPositionUiState.location.y

                                    mapPosition.value = Pair(xPosition, yPosition)
                                }

                                else -> {
                                    if (mapPosition == null) {
                                        Toast.makeText(
                                            context,
                                            stringResource(id = R.string.unexpected_error),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                            if (mapPosition.value != null) {
                                UserMarker(
                                    markerPosition = mapPosition.value!!,
                                    imageSize = size
                                )
                            }
                        }
                    }
                }

                else -> Toast.makeText(
                    context,
                    stringResource(id = R.string.unexpected_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        is MapUiState.Loading -> {
            BeeGuideCircularProgressIndicator()
        }

        is MapUiState.Error -> {
            Toast.makeText(
                context,
                stringResource(id = R.string.unexpected_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
fun MapMarker(markerPosition: Pair<Float, Float>, imageSize: Size, description: String) {
    var showDescription by remember { mutableStateOf(false) }

    val offsetX = imageSize.width.times(markerPosition.first).pxToDp() - 6.dp
    val offsetY = imageSize.height.times(markerPosition.second).pxToDp() - 6.dp


    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .clickable { showDescription = !showDescription }
            .width(200.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(
                        TransparentColor(
                            color = MaterialTheme.colorScheme.primary,
                            alpha = 0.6f
                        )
                    )
            )
            if (showDescription) {
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = description, fontSize = 8.sp, lineHeight = 10.sp)
            }
        }
    }
}

@Composable
fun UserMarker(markerPosition: Pair<Float, Float>, imageSize: Size) {
    val offsetX = imageSize.width.times(markerPosition.first).pxToDp() - 12.dp
    val offsetY = imageSize.height.times(markerPosition.second).pxToDp() - 12.dp

    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun Float.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun TransparentColor(color: Color, alpha: Float): Color {
    val red = color.red
    val green = color.green
    val blue = color.blue

    return Color(red = red, green = green, blue = blue, alpha = alpha)
}