package com.example.beeguide.navigation.algorithm

import android.util.Log
import com.example.beeguide.model.BeeGuideBeacon
import com.example.beeguide.ui.viewmodels.MapUiState
import com.example.beeguide.ui.viewmodels.MapPositionUiState
import com.example.beeguide.ui.viewmodels.MapViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.subscribe
import org.altbeacon.beacon.RegionViewModel
import com.example.beeguide.model.Map
import org.altbeacon.beacon.Beacon

class CircleValidator(private val beaconViewModel: RegionViewModel, private val mapViewModel: MapViewModel) {

    public var circles: List<Circle> = listOf()

    fun validate(){
        when (val mapState: MapUiState = mapViewModel.mapUiState.value) {
            is MapUiState.Loading ->
                Log.d("MapScreen", "MapScreen: None")

            is MapUiState.Success ->
                if(!beaconViewModel.rangedBeacons.value.isNullOrEmpty()) circleAllocator(mapState.map)

            else -> Log.d("MapScreen", "MapScreen: Error")
        }
    }

    private fun circleAllocator(map: Map){
        map.beacons.forEach{beeGuideBeacon : BeeGuideBeacon ->
            val beacon: Beacon? = beaconViewModel.rangedBeacons.value?.find {beacon: Beacon -> beacon.id3.toInt() == beeGuideBeacon.minor }
            val distance: Int = (beacon?.distance?.times(100))?.toInt() ?: 0
            val circle: Circle = Circle(beeGuideBeacon.x, beeGuideBeacon.y, distance)
            circles += circle
        }
    }
}