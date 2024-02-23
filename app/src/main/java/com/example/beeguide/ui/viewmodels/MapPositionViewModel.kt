package com.example.beeguide.ui.viewmodels

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
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
    private val sensorViewModel: SensorViewModel,
    private val uncalibratedSensorViewModel: UncalibratedSensorViewModel
): ViewModel() {

    //private var oldSensorValues: SensorState.Success = SensorState.Success(accelerationX = 0f, accelerationZ = 0f, timestamp = 0)
    private var currentVelocity: Float = 0f
    private var currentPosition: PrecisePoint = PrecisePoint(0.0, 0.0)
    private var calculationCounter: Int = 0;



    private var linearAcceleration: FloatArray = FloatArray(2)
    private var velocity: FloatArray = FloatArray(2)
    private var distanceX: Float = 0f
    private var distanceZ: Float = 0f
    private var lastTimestamp: Long = 0
    private var stuckCounter: Int = 0
    private var stuckAverageX: Float = 0f
    private var stuckAverageZ: Float = 0f
    private var stuckAverageXlow: Float = 0f
    private var stuckAverageZlow: Float = 0f



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
        calculatePosition()
    }

    private val mapObserver = Observer<MapUiState> {
        calculatePosition()
    }

    private val sensorObserver = Observer<SensorState> {
        viewModelScope.launch {
            mapPositionUiState = navigate()
        }
    }

    private val uncalibratedSensorObserver = Observer<SensorState> {
        if(uncalibratedSensorViewModel.sensorState.value is UncalibratedSensorState.Success){
            val sensorValues: UncalibratedSensorState.Success = uncalibratedSensorViewModel.sensorState.value as UncalibratedSensorState.Success
            Log.d("Acceleration-Features-Uncalibrated", "Updated X: ${sensorValues.accelerationXYZ[0]}, Y: ${sensorValues.accelerationXYZ[1]}, Z: ${sensorValues.accelerationXYZ[2]}")
        }
    }

    private fun navigate(): MapPositionUiState {
        if(sensorViewModel.sensorState.value is SensorState.Success) {
            val sensorValues: SensorState.Success =
                sensorViewModel.sensorState.value as SensorState.Success

            val currentTime = System.currentTimeMillis()
            val dt = if (lastTimestamp != 0L) (currentTime - lastTimestamp) / 1000.0f else 0f
            lastTimestamp = currentTime

            linearAcceleration = sensorValues.accelerationXZ

            if(sensorValues.accelerationXZ[0] > 20 || sensorValues.accelerationXZ[1] > 20 || sensorValues.accelerationXZ[0] < 20 || sensorValues.accelerationXZ[1] < 20){
                Log.d("filter", "filtered")
                MapPositionUiState.Useless("Useless calculation")
            }

            // Integrate acceleration to get velocity
            for (i in 0 until 2) {
                velocity[i] += linearAcceleration[i] * dt
            }

            val vMagnitude = sqrt(velocity[0].pow(2) + velocity[1].pow(2))

            // Integrate velocity to get distance
            val distanceChangeX: Float = velocity[0] * dt
            val distanceChangeY: Float = velocity[1] * dt

            val bufferValueMagnitude = 0.0001
            val bufferValueVelocityReset = 0.0005

            if(distanceChangeX > bufferValueMagnitude || distanceChangeX < -bufferValueMagnitude) distanceX += distanceChangeX
            if(distanceChangeY > bufferValueMagnitude || distanceChangeY < -bufferValueMagnitude) distanceZ += distanceChangeY

            // Reset velocity to zero when device is at rest
            //if (distanceChangeX < bufferValueVelocityReset && distanceChangeX > -bufferValueVelocityReset) velocity[0] = 0f
            //if (distanceChangeY < bufferValueVelocityReset && distanceChangeY > -bufferValueVelocityReset) velocity[1] = 0f

            // Print the current distance
            Log.d("since-distance-X", distanceX.toString())
            Log.d("since-distance-Z", distanceZ.toString())
            Log.d("current-velocity-X", velocity[0].toString())


            val bufferValueStuck = 0.05

            if(stuckCounter < 4){
                stuckAverageX += velocity[0]
                stuckAverageZ += velocity[1]
                if(stuckCounter > 1) {
                    stuckAverageXlow += velocity[0]
                    stuckAverageZlow += velocity[1]
                }
                stuckCounter++
            }
            else{
                stuckAverageX /= stuckCounter
                stuckAverageZ /= stuckCounter

                stuckAverageXlow /= stuckCounter/2
                stuckAverageZlow /= stuckCounter/2

                if(stuckAverageXlow < stuckAverageX * 1 + bufferValueStuck && stuckAverageXlow > stuckAverageX * 1 - bufferValueStuck) velocity[0] = 0f
                if(stuckAverageZlow < stuckAverageZ * 1 + bufferValueStuck && stuckAverageZlow > stuckAverageZ * 1 - bufferValueStuck) velocity[1] = 0f

                stuckCounter = 0
            }



            /*
            val curOldSensorValues = oldSensorValues
            oldSensorValues = sensorValues

            if (oldSensorValues.timestamp != 0.toLong()) {
                currentVelocity = 0f
                val timeDifference: Float =
                    (sensorValues.timestamp - curOldSensorValues.timestamp).toFloat() / 1000000000.0f //to seconds

                Log.d("Timestamp-old", sensorValues.timestamp.toString())
                Log.d("Timestamp-new", curOldSensorValues.timestamp.toString())

                val acceleration = sensorValues.accelerationX
                Log.d("AccelerationX", sensorValues.accelerationX.toString())
                Log.d("TimeDifference", timeDifference.toString())
                val distance =
                    (currentVelocity * timeDifference + 0.5 * acceleration * timeDifference.pow(
                        2
                    )) * 100
                currentVelocity += timeDifference * acceleration

                Log.d("distance", distance.toString())
                Log.d("velocity", currentVelocity.toString())

                currentPosition.x += distance
                calculationCounter++


                if(calculationCounter > 100)
                {
                    calculationCounter = 0
                    Log.d("position", "X: ${currentPosition.x.toString()}, Y: ${currentPosition.y.roundToInt()}")
                    return MapPositionUiState.Success(Point(currentPosition.x.roundToInt(), currentPosition.y.roundToInt()))
                }

            }
             */
        }
        return MapPositionUiState.Useless("Useless calculation")
    }

    init {
        regionViewModel.rangedBeacons.observeForever(rangedBeaconObserver)
        mapViewModel.mapUiState.asLiveData().observeForever(mapObserver)
        sensorViewModel.sensorState.asLiveData().observeForever(sensorObserver)
        uncalibratedSensorViewModel.sensorState.asLiveData().observeForever(uncalibratedSensorObserver)
    }

    override fun onCleared() {
        super.onCleared()
        regionViewModel.rangedBeacons.removeObserver(rangedBeaconObserver)
        mapViewModel.mapUiState.asLiveData().observeForever(mapObserver)
    }



}