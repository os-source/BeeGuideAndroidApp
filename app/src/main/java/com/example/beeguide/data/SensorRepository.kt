package com.example.beeguide.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.beeguide.model.Map
import com.example.beeguide.navigation.preconditions.SensorGetter
import com.example.beeguide.network.BeeGuideApiService

interface SensorRepository {
    fun getAccelerationSensor(): Sensor
    fun getUncalibratedAccelerationSensor(): Sensor
    fun getRotationSensor(): Sensor
    fun getMagneticFieldSensor(): Sensor
    fun getSensorManager(): SensorManager
}

class HardwareSensorRepository(
    private val ctx: Context
) : SensorRepository {

    private val sensorManager = ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager





    override fun getAccelerationSensor(): Sensor  {
        return sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)!!
    }

    override fun getUncalibratedAccelerationSensor(): Sensor  {
        return sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
    }

    override fun getRotationSensor(): Sensor  {
        return sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)!!
    }

    override fun getMagneticFieldSensor(): Sensor  {
        return sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!!
    }

    override fun getSensorManager(): SensorManager {
        return sensorManager
    }
}