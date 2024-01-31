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
import kotlinx.coroutines.launch
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.RegionViewModel

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
    private val mapViewModel: MapViewModel
): ViewModel() {

    var mapPositionUiState: MapPositionUiState by mutableStateOf(MapPositionUiState.None)
        private set


    fun calculatePosition(){
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

        if(location.x != 0 || location.y != 0){
            Log.d("Location", "Location: X: ${location.x}, Y: ${location.y}")
            //return MapPositionUiState.Success(clusterRoot.x, clusterRoot.y)
            return MapPositionUiState.Success(location)
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


    init {
        regionViewModel.rangedBeacons.observeForever(rangedBeaconObserver)
        mapViewModel.mapUiState.asLiveData().observeForever(mapObserver)
    }

    override fun onCleared() {
        super.onCleared()
        regionViewModel.rangedBeacons.removeObserver(rangedBeaconObserver)
        mapViewModel.mapUiState.asLiveData().observeForever(mapObserver)
    }



}