package com.example.beeguide.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.beeguide.ui.viewmodels.sensorviewmodels.CompassState
import com.example.beeguide.ui.viewmodels.sensorviewmodels.UncalibratedAccelerationSensorState
import com.example.beeguide.ui.viewmodels.sensorviewmodels.UncalibratedAccelerationSensorViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

sealed interface MovingState {
    data class Success(
        val isMoving: Boolean
    ) : MovingState

    data class Useless(
        val message: String
    ) : MovingState

    object Error: MovingState
    object None: MovingState
}

class MovingViewModel(
    private val uncalibratedAccelerationSensorViewModel: UncalibratedAccelerationSensorViewModel
): ViewModel() {
    private var hitCount: Int = 0
    private var hitSum: Double = 0.0
    private var hitResult: Double = 0.0

    private val SAMPLE_SIZE = 50 // change this sample size as you want, higher is more precise but slow measure.
    private val THRESHOLD = 0.2 // change this threshold as you want, higher is more spike movement

    private var mAccelCurrent: Double = 0.0
    private var mAccelLast: Double = 0.0
    private var mAccel: Double = 0.0

    private val _movingState = MutableStateFlow<MovingState>(
        MovingState.None
    )
    val movingState: StateFlow<MovingState> = _movingState.asStateFlow()

    private fun executeCheck(sensorValues: UncalibratedAccelerationSensorState.Success): MovingState {
        val x: Double = sensorValues.accelerationXYZ[0].toDouble()
        val y: Double = sensorValues.accelerationXYZ[1].toDouble()
        val z: Double = sensorValues.accelerationXYZ[2].toDouble()
        mAccelLast = mAccelCurrent
        mAccelCurrent = sqrt(x.pow(2) + y.pow(2) + z.pow(2))
        val delta = (mAccelCurrent - mAccelLast)
        mAccel = mAccel * 0.9f + delta

        if (hitCount <= SAMPLE_SIZE) {
            hitCount++
            hitSum += abs(mAccel)
        } else {
            hitResult = hitSum / SAMPLE_SIZE
            Log.d("hitresult", java.lang.String.valueOf(hitResult))
            return if (hitResult > THRESHOLD) {
                Log.d("Moving", "Moving")
                hitCount = 0
                hitSum = 0.0
                hitResult = 0.0
                MovingState.Success(true)
            } else {
                Log.d("Moving", "Stop Moving")
                hitCount = 0
                hitSum = 0.0
                hitResult = 0.0
                MovingState.Success(false)
            }
        }
        return MovingState.Useless("No values")
    }

    private val uncalibratedAccelerationSensorObserver = Observer<UncalibratedAccelerationSensorState> {
        if(uncalibratedAccelerationSensorViewModel.uncalibratedSensorState.value is UncalibratedAccelerationSensorState.Success){
            val sensorValues: UncalibratedAccelerationSensorState.Success = uncalibratedAccelerationSensorViewModel.uncalibratedSensorState.value as UncalibratedAccelerationSensorState.Success

            viewModelScope.launch {
                _movingState.update {
                    executeCheck(sensorValues)
                }
            }
        }
    }

    init {
        uncalibratedAccelerationSensorViewModel.uncalibratedSensorState.asLiveData().observeForever(uncalibratedAccelerationSensorObserver)
    }

    override fun onCleared() {
        super.onCleared()
        uncalibratedAccelerationSensorViewModel.uncalibratedSensorState.asLiveData().removeObserver(uncalibratedAccelerationSensorObserver)
    }
}