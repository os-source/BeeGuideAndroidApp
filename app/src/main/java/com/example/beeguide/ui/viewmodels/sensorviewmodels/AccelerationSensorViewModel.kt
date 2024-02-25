package com.example.beeguide.ui.viewmodels.sensorviewmodels

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


sealed interface AccelerationSensorState {
    data class Success(
        val accelerationXYZ: FloatArray,
        val timestamp: Long
    ) : AccelerationSensorState

    object Error : AccelerationSensorState
    object Loading : AccelerationSensorState
}

class AccelerationSensorViewModel(private val sensorRepository: SensorRepository): ViewModel(), SensorEventListener {
    companion object {
        private const val TAG = "SensorViewModel"

        //val Factory: ViewModelProvider.Factory = SensorViewModelFactory(sensor = sensor)

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BeeGuideApplication)
                val sensorRepository = application.container.sensorRepository
                AccelerationSensorViewModel(sensorRepository = sensorRepository)
            }
        }

    }

    private val _accelerationSensorState = MutableStateFlow<AccelerationSensorState>(
        AccelerationSensorState.Loading
    )
    val accelerationSensorState: StateFlow<AccelerationSensorState> = _accelerationSensorState.asStateFlow()

    override fun onSensorChanged(event: SensorEvent?) {
        Log.d("Acceleration-Features", "Updated ${event?.values?.joinToString(", ")}")
        viewModelScope.launch {
            _accelerationSensorState.update {
                AccelerationSensorState.Success(
                    accelerationXYZ = floatArrayOf(
                        event!!.values[0],
                        event.values[1],
                        event.values[2]
                    ), timestamp = event.timestamp
                )
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("Acceleration-Features", "AccuracyUpdated")
    }

    init{
        sensorRepository.getSensorManager().registerListener(this, sensorRepository.getAccelerationSensor(), SensorManager.SENSOR_DELAY_GAME)
    }
    override fun onCleared() {
        sensorRepository.getSensorManager().unregisterListener(this)
    }
}