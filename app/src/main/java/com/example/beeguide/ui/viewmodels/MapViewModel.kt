package com.example.beeguide.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.beeguide.BeeGuideApplication
import com.example.beeguide.data.MapRepository
import com.example.beeguide.model.Map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface MapUiState {
    data class Success(
        val map: Map,
    ) : MapUiState

    object Error : MapUiState
    object Loading : MapUiState
}

sealed interface MapFileUiState {
    data class Success(
        val mapFile: String
    ) : MapFileUiState

    object Error : MapFileUiState
    object Loading : MapFileUiState
}

class MapViewModel(
    private val mapRepository: MapRepository,
    uuId: String,
    major: Int,
) : ViewModel() {
    companion object {
        private const val TAG = "MapViewModel"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BeeGuideApplication)
                val mapRepository = application.container.mapRepository
                MapViewModel(mapRepository = mapRepository, uuId = "426C7565-4368-6172-6D43-6561636F6E73", major = 3000)
            }
        }
    }

    private val _mapUiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val mapUiState: StateFlow<MapUiState> = _mapUiState.asStateFlow()

    init {
        getMap(uuId, major)
    }

    fun getMap(uuId: String, major: Int) {
        viewModelScope.launch {
            _mapUiState.update {
                MapUiState.Loading
            }
            _mapUiState.update {
                try {
                    MapUiState.Success(mapRepository.getMap(uuId, major))
                } catch (e: IOException) {
                    Log.d(TAG, "getMap: $e")
                    MapUiState.Error
                } catch (e: HttpException) {
                    Log.d(TAG, "getMap: $e")
                    MapUiState.Error
                }
            }
        }
    }
}

class MapFileViewModel(private val mapRepository: MapRepository, private val mapId: Int) : ViewModel() {
    var mapFileUiState: MapFileUiState by mutableStateOf(MapFileUiState.Loading)
        private set

    init {
        getMapFile()
    }

    fun getMapFile() {
        viewModelScope.launch {
            mapFileUiState = MapFileUiState.Loading
            mapFileUiState = try {
                MapFileUiState.Success(mapRepository.getMapFile(mapId))
            } catch (e: IOException) {
                Log.d("MapFileUiState", e.toString())
                MapFileUiState.Error
            } catch (e: HttpException) {
                Log.d("MapFileUiState", e.toString())
                MapFileUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BeeGuideApplication)
                val mapRepository = application.container.mapRepository
                MapFileViewModel(mapRepository = mapRepository, mapId = 1)
            }
        }
    }
}