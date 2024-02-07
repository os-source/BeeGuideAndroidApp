package com.example.beeguide.ui.viewmodels

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.beeguide.BeeGuideApplication
import com.example.beeguide.data.HardwareSensorRepository
import com.example.beeguide.data.SensorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.sql.Timestamp


sealed interface SensorState {
    data class Success(
        val accelerationX: Float,
        val accelerationZ: Float,
        val timestamp: Long
    ) : SensorState

    object Error : SensorState
    object Loading : SensorState
}

class SensorViewModel(private val sensorRepository: SensorRepository): ViewModel(), SensorEventListener {
    companion object {
        private const val TAG = "SensorViewModel"

        //val Factory: ViewModelProvider.Factory = SensorViewModelFactory(sensor = sensor)

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BeeGuideApplication)
                val sensorRepository = application.container.sensorRepository
                SensorViewModel(sensorRepository = sensorRepository)
            }
        }

    }

    private val _sensorState = MutableStateFlow<SensorState>(SensorState.Loading)
    val sensorState: StateFlow<SensorState> = _sensorState.asStateFlow()

    override fun onSensorChanged(event: SensorEvent?) {
        Log.d("Acceleration-Features", "Updated ${event?.values?.joinToString(", ")}")
        viewModelScope.launch {
            _sensorState.update {
                SensorState.Success(accelerationX = event!!.values[0], accelerationZ = event.values[2], timestamp = event.timestamp)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("Acceleration-Features", "AccuracyUpdated")
    }

    init{
        sensorRepository.getSensorManager().registerListener(this, sensorRepository.getSensor(), SensorManager.SENSOR_DELAY_GAME)
    }
    override fun onCleared() {
        sensorRepository.getSensorManager().unregisterListener(this)
    }
}

/*
class SensorViewModelFactory(private val sensor: Sensor) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SensorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SensorViewModel(sensor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

 */