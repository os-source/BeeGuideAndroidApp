package com.example.beeguide.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.RegionViewModel

sealed interface MapPositionUiState {
    data class Success(
        val x: Int,
        val y: Int
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
            mapPositionUiState = triangulate()
        }
    }

    private fun triangulate(): MapPositionUiState {
        // TODO
        return MapPositionUiState.Success(10, 10) // TODO: calculate actual position
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