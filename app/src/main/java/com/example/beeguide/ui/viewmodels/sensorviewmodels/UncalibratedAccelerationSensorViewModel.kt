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


sealed interface UncalibratedAccelerationSensorState {
    data class Success(
        val accelerationXYZ: FloatArray
    ) : UncalibratedAccelerationSensorState

    object Error : UncalibratedAccelerationSensorState
    object Loading : UncalibratedAccelerationSensorState
}

class UncalibratedAccelerationSensorViewModel(private val sensorRepository: SensorRepository): ViewModel(), SensorEventListener {
    companion object {
        private const val TAG = "SensorViewModel"

        //val Factory: ViewModelProvider.Factory = SensorViewModelFactory(sensor = sensor)

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BeeGuideApplication)
                val sensorRepository = application.container.sensorRepository
                UncalibratedAccelerationSensorViewModel(sensorRepository = sensorRepository)
            }
        }

    }

    private val _uncalibratedSensorState = MutableStateFlow<UncalibratedAccelerationSensorState>(
        UncalibratedAccelerationSensorState.Loading
    )
    val uncalibratedSensorState: StateFlow<UncalibratedAccelerationSensorState> = _uncalibratedSensorState.asStateFlow()

    override fun onSensorChanged(event: SensorEvent?) {
        //Log.d("Acceleration-Features", "Updated ${event?.values?.joinToString(", ")}")
        viewModelScope.launch {
            _uncalibratedSensorState.update {
                UncalibratedAccelerationSensorState.Success(
                    accelerationXYZ = floatArrayOf(
                        event!!.values[0],
                        event.values[1],
                        event.values[2]
                    )
                )
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("Acceleration-Features", "AccuracyUpdated")
    }

    init{
        sensorRepository.getSensorManager().registerListener(this, sensorRepository.getUncalibratedAccelerationSensor(), 5000)
    }
    override fun onCleared() {
        sensorRepository.getSensorManager().unregisterListener(this)
    }
}