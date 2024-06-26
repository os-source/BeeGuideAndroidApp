package com.example.beeguide.navigation.inertia

import android.util.Log
import com.example.beeguide.ui.viewmodels.MapPositionUiState
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class Navigator(private val mapRotation: Float) {
    private var lastTimestamp: Long = 0L
    var velocity: FloatArray = FloatArray(2)
    fun calculateNavigation(accelerationXYZ: FloatArray, currentRotation: Float): FloatArray{
        val currentTime = System.currentTimeMillis()
        val dt = if (lastTimestamp != 0L) (currentTime - lastTimestamp) / 1000.0f else 0f
        lastTimestamp = currentTime

        if(accelerationXYZ[0] > 20 || accelerationXYZ[1] > 20 || accelerationXYZ[0] < -20 || accelerationXYZ[1] < -20){
            Log.d("filter", "filtered")
            return floatArrayOf(0f, 0f)
        }

        // Integrate acceleration to get velocity
        for (i in 0 until 2) {
            velocity[i] += accelerationXYZ[i] * dt
        }

        // Integrate velocity to get distance
        var distanceChangeX: Float = (velocity[0] * dt) * 100
        var distanceChangeY: Float = (velocity[1] * dt) * 100

        // Filtering Useless Movement
        val bufferVal = 0.0001
        if(distanceChangeX.absoluteValue < bufferVal) distanceChangeX = 0f
        if(distanceChangeY.absoluteValue < bufferVal) distanceChangeY = 0f

        Log.d("testhomiejo", distanceChangeX.toString())

        val offsetRotation = Math.toRadians((currentRotation - mapRotation).toDouble())

        val zDistance = (cos(offsetRotation) * distanceChangeY + sin(offsetRotation) * distanceChangeX).toFloat()
        val xDistance = (cos(offsetRotation) * distanceChangeX + sin(offsetRotation) * distanceChangeY).toFloat()

        return floatArrayOf(distanceChangeX, distanceChangeY)
    }
}