package com.example.beeguide.data

import android.hardware.Sensor
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.beeguide.model.Map
import com.example.beeguide.navigation.preconditions.SensorGetter
import com.example.beeguide.network.BeeGuideApiService

interface SensorRepository {
    @Composable
    fun getSensor(): Sensor
}

class HardwareSensorRepository(
    private val sensorGetter: SensorGetter
) : SensorRepository {
    @Composable
    override fun getSensor(): Sensor = sensorGetter.getSensor()
}