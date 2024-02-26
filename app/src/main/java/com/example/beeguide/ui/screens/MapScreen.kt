package com.example.beeguide.ui.screens

import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.caverock.androidsvg.SVG
import com.example.beeguide.ui.components.BeeGuideCircularProgressIndicator
import com.example.beeguide.ui.viewmodels.MapFileUiState
import com.example.beeguide.ui.viewmodels.MapPositionUiState
import com.example.beeguide.ui.viewmodels.MapUiState
import com.example.beeguide.ui.viewmodels.MapViewModel
import java.io.ByteArrayInputStream
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
                    Log.d("MapScreen", "MapScreen: Loading")
                    BeeGuideCircularProgressIndicator()
                }

                is MapFileUiState.Success -> {
                    Log.d("MapScreen", "MapScreen: ${mapFileUiState.mapFile}")

                    // get local density from composable
                    val density = LocalDensity.current

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

                                        // For natural zooming and rotating, the centroid of the gesture should
                                        // be the fixed point where zooming and rotating occurs.
                                        // We compute where the centroid was (in the pre-transformed coordinate
                                        // space), and then compute where it will be after this delta.
                                        // We then compute what the new offset should be to keep the centroid
                                        // visually stationary for rotating and zooming, and also apply the pan.
                                        mapOffset =
                                            (mapOffset + centroid / oldScale).rotateBy(gestureRotate) -
                                                    (centroid / newScale + pan / oldScale)
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
                        // Convert SVG string to InputStream
                        val inputStream = ByteArrayInputStream(mapFileUiState.mapFile.toByteArray())

                        // Parse SVG from InputStream
                        val svg = SVG.getFromInputStream(inputStream)

                        // Render SVG to bitmap
                        val bitmap = Bitmap.createBitmap(
                            svg.documentWidth.toInt(),
                            svg.documentHeight.toInt(),
                            Bitmap.Config.ARGB_8888
                        )

                        val canvas = android.graphics.Canvas(bitmap)
                        svg.renderToCanvas(canvas)

                        // Convert bitmap to ImageBitmap
                        val imageBitmap = bitmap.asImageBitmap()

                        val imageSize = Size(imageBitmap.width.toFloat(), imageBitmap.height.toFloat())

                        // draw image
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawImage(
                                imageBitmap,
                                topLeft = Offset(0f, 0f),
                            )
                        }

                        val top = 0.47f
                        val left = 0.033f
                        val right = 0.2842f
                        val bottom = 0.883f

                        imageSize.let { size ->
                            /*MapMarker(markerPosition = Pair(right, top), imageSize = size)
                            MapMarker(markerPosition = Pair(right, bottom), imageSize = size)
                            MapMarker(markerPosition = Pair(left, top), imageSize = size)
                            MapMarker(markerPosition = Pair(left, bottom), imageSize = size)*/

                            when (mapPositionUiState) {
                                is MapPositionUiState.None ->
                                    Toast.makeText(context, "No position found", Toast.LENGTH_SHORT).show()

                                is MapPositionUiState.Success -> {
                                    Log.d("MapScreen", "MapScreen: ${mapPositionUiState.location}")
                                    val xPosition = (1 / state.map.xAxis * mapPositionUiState.location.x) * (right * size.width - left * size.width) / size.width + left
                                    val yPosition = (1 / state.map.yAxis * mapPositionUiState.location.y) * (bottom * size.height - top * size.height) / size.height + top
                                    UserMarker(
                                        markerPosition = Pair(xPosition, yPosition),
                                        imageSize = size
                                    )
                                }

                                else ->
                                    Toast.makeText(context, "Error MapPositionUiState", Toast.LENGTH_SHORT).show()
                            }


                        }
                    }
                }

                else -> Log.d("MapScreen", "MapScreen: Error MapFileUiState")
            }
        }
        MapUiState.Loading -> {
            // Render your loading state UI
        }
        MapUiState.Error -> {
            // Render your error state UI
        }
    }
}

@Composable
fun MapMarker(markerPosition: Pair<Float, Float>, imageSize: Size) {
    val offsetX = imageSize.width.times(markerPosition.first).pxToDp() - 3.dp
    val offsetY = imageSize.height.times(markerPosition.second).pxToDp() - 3.dp

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
    val offsetX = imageSize.width.times(markerPosition.first).pxToDp() - 6.dp
    val offsetY = imageSize.height.times(markerPosition.second).pxToDp() - 6.dp

    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun Float.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }