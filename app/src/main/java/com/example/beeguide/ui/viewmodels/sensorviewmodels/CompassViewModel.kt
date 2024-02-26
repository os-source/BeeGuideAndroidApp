package com.example.beeguide.ui.viewmodels.sensorviewmodels

import android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.beeguide.BeeGuideApplication
import com.example.beeguide.data.SensorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed interface CompassState {
    data class Success(
        val azimuthInDegrees: Float
    ) : CompassState

    object Error : CompassState
    object Loading : CompassState
}

class CompassViewModel(private val sensorRepository: SensorRepository): ViewModel(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var magnetometer: Sensor? = null
    private var accelerometer: Sensor? = null
    private val lastAccelerometer = FloatArray(3)
    private val lastMagnetometer = FloatArray(3)
    private var lastAccelerometerSet = false
    private var lastMagnetometerSet = false
    private val rotationMatrix = FloatArray(9)
    private val orientation = FloatArray(3)

    companion object {
        private const val TAG = "SensorViewModel"

        //val Factory: ViewModelProvider.Factory = SensorViewModelFactory(sensor = sensor)

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BeeGuideApplication)
                val sensorRepository = application.container.sensorRepository
                CompassViewModel(sensorRepository = sensorRepository)
            }
        }

    }

    private val _compassState = MutableStateFlow<CompassState>(
        CompassState.Loading
    )
    val compassState: StateFlow<CompassState> = _compassState.asStateFlow()

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor === magnetometer) {
            System.arraycopy(event!!.values, 0, lastMagnetometer, 0, event.values.count())
            lastMagnetometerSet = true
        } else if (event!!.sensor === accelerometer) {
            System.arraycopy(event!!.values, 0, lastAccelerometer, 0, event.values.count())
            lastAccelerometerSet = true
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            SensorManager.getRotationMatrix(
                rotationMatrix,
                null,
                lastAccelerometer,
                lastMagnetometer
            )
            SensorManager.getOrientation(rotationMatrix, orientation)
            val azimuthInRadians = orientation[0]
            val azimuthInDegrees =
                (Math.toDegrees(azimuthInRadians.toDouble()) + 360).toFloat() % 360

            viewModelScope.launch {
                _compassState.update {
                    CompassState.Success(
                        azimuthInDegrees = azimuthInDegrees
                    )
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("Acceleration-Features", "AccuracyUpdated")
    }

    init{
        magnetometer = sensorRepository.getMagneticFieldSensor()
        accelerometer = sensorRepository.getUncalibratedAccelerationSensor()
        sensorManager = sensorRepository.getSensorManager()
        sensorManager!!.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME)
        sensorManager!!.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }
    override fun onCleared() {
        sensorRepository.getSensorManager().unregisterListener(this)
    }
}