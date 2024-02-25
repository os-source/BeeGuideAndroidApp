package com.example.beeguide.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.beeguide.navigation.algorithm.CalculationController
import com.example.beeguide.navigation.algorithm.CircleValidator
import com.example.beeguide.navigation.algorithm.Point
import com.example.beeguide.navigation.algorithm.PrecisePoint
import com.example.beeguide.navigation.inertia.Navigator
import com.example.beeguide.navigation.inertia.VelocityResetter
import com.example.beeguide.ui.viewmodels.sensorviewmodels.AccelerationSensorState
import com.example.beeguide.ui.viewmodels.sensorviewmodels.AccelerationSensorViewModel
import com.example.beeguide.ui.viewmodels.sensorviewmodels.CompassState
import com.example.beeguide.ui.viewmodels.sensorviewmodels.CompassViewModel
import com.example.beeguide.ui.viewmodels.sensorviewmodels.RotationSensorState
import com.example.beeguide.ui.viewmodels.sensorviewmodels.RotationSensorViewModel
import com.example.beeguide.ui.viewmodels.sensorviewmodels.UncalibratedAccelerationSensorState
import com.example.beeguide.ui.viewmodels.sensorviewmodels.UncalibratedAccelerationSensorViewModel
import kotlinx.coroutines.launch
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.RegionViewModel
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

sealed interface MapPositionUiState {
    data class Success(
        val location: Point
    ) : MapPositionUiState

    data class Useless(
        val message: String
    ) : MapPositionUiState

    object Error: MapPositionUiState
    object None: MapPositionUiState
}

class MapPositionViewModel(
    private val regionViewModel: RegionViewModel,
    private val mapViewModel: MapViewModel,
    private val accelerationSensorViewModel: AccelerationSensorViewModel,
    private val uncalibratedAccelerationSensorViewModel: UncalibratedAccelerationSensorViewModel,
    private val rotationSensorViewModel: RotationSensorViewModel,
    private val compassViewModel: CompassViewModel
): ViewModel() {
    private var currentPosition: PrecisePoint = PrecisePoint(0.0, 0.0)
    private var currentRotation: Float = 0f
    private var returnCounter: Int = 0

    private var navigator: Navigator? = null
    private var velocityResetter: VelocityResetter = VelocityResetter()

    var mapPositionUiState: MapPositionUiState by mutableStateOf(MapPositionUiState.None)
        private set

    private fun calculatePosition(){
        viewModelScope.launch {
            mapPositionUiState = calculate()
        }
    }

    private fun calculate(): MapPositionUiState {
        val circleValidator = CircleValidator(regionViewModel, mapViewModel); circleValidator.validate()
        val circles = circleValidator.circles

        val calculationController = CalculationController(circles)
        val location = calculationController.control()
        calculationController.logPoints()
        currentPosition.x = location.x.toDouble()
        currentPosition.y = location.y.toDouble()

        if(currentPosition.x != 0.0 || currentPosition.y != 0.0){
            Log.d("Location", "Location: X: ${location.x}, Y: ${location.y}")
            //return MapPositionUiState.Success(clusterRoot.x, clusterRoot.y)
            return MapPositionUiState.Success(Point(currentPosition.x.toInt(), currentPosition.y.toInt()))
        }
        else{
            return MapPositionUiState.Useless("Useless calculation")
        }
    }

    private val rangedBeaconObserver =  Observer<Collection<Beacon>> {
        //calculatePosition()
    }

    private val mapObserver = Observer<MapUiState> {
        navigator = Navigator(170f)
        //calculatePosition()
    }

    private val accelerationSensorObserver = Observer<AccelerationSensorState> {
        viewModelScope.launch {
            mapPositionUiState = navigate()
        }
    }

    private val uncalibratedAccelerationSensorObserver = Observer<UncalibratedAccelerationSensorState> {
        if(uncalibratedAccelerationSensorViewModel.uncalibratedSensorState.value is UncalibratedAccelerationSensorState.Success){
            val sensorValues: UncalibratedAccelerationSensorState.Success = uncalibratedAccelerationSensorViewModel.uncalibratedSensorState.value as UncalibratedAccelerationSensorState.Success
            Log.d("Acceleration-Features-Uncalibrated", "Updated X: ${sensorValues.accelerationXYZ[0]}, Y: ${sensorValues.accelerationXYZ[1]}, Z: ${sensorValues.accelerationXYZ[2]}")
            if(velocityResetter.checkReset(sensorValues.accelerationXYZ) && navigator != null){
                navigator?.velocity = floatArrayOf(0f, 0f, 0f)
                Log.d("velocityResetter", "Resetted")
            }
        }
    }

    private val rotationSensorObserver = Observer<RotationSensorState> {
        if(rotationSensorViewModel.rotationSensorState.value is RotationSensorState.Success){
            val sensorValues: RotationSensorState.Success = rotationSensorViewModel.rotationSensorState.value as RotationSensorState.Success
            Log.d("Acceleration-Features-Rotation", "Updated X: ${sensorValues.rotationXYZ[0]}, Y: ${sensorValues.rotationXYZ[1]}, Z: ${sensorValues.rotationXYZ[2]}")
        }
    }

    private val compassObserver = Observer<CompassState> {
        if(compassViewModel.compassState.value is CompassState.Success){
            val sensorValues: CompassState.Success = compassViewModel.compassState.value as CompassState.Success
            Log.d("Acceleration-Features-Compass", "Degrees: ${sensorValues.azimuthInDegrees}")
            currentRotation = sensorValues.azimuthInDegrees
        }
    }

    private fun navigate(): MapPositionUiState {
        if(accelerationSensorViewModel.accelerationSensorState.value is AccelerationSensorState.Success && navigator != null) {
            val sensorValues: AccelerationSensorState.Success =
                accelerationSensorViewModel.accelerationSensorState.value as AccelerationSensorState.Success

            val positionChangeXZ: FloatArray = navigator!!.calculateNavigation(sensorValues.accelerationXYZ, currentRotation)
            currentPosition.x += positionChangeXZ[0]
            currentPosition.y += positionChangeXZ[1]

            returnCounter++

            if(returnCounter > 20){
                returnCounter = 0
                val posX = currentPosition.x.roundToInt()
                val posY = currentPosition.y.roundToInt()
                Log.d("CurrentPosition", "X: $posX, Y: $posY")
                return MapPositionUiState.Success(Point(posX, posY))
            }
        }
        return MapPositionUiState.Useless("Useless calculation")
    }

    init {
        regionViewModel.rangedBeacons.observeForever(rangedBeaconObserver)
        mapViewModel.mapUiState.asLiveData().observeForever(mapObserver)
        accelerationSensorViewModel.accelerationSensorState.asLiveData().observeForever(accelerationSensorObserver)
        uncalibratedAccelerationSensorViewModel.uncalibratedSensorState.asLiveData().observeForever(uncalibratedAccelerationSensorObserver)
        rotationSensorViewModel.rotationSensorState.asLiveData().observeForever(rotationSensorObserver)
        compassViewModel.compassState.asLiveData().observeForever(compassObserver)
    }

    override fun onCleared() {
        super.onCleared()
        regionViewModel.rangedBeacons.removeObserver(rangedBeaconObserver)
        mapViewModel.mapUiState.asLiveData().observeForever(mapObserver)
    }
}