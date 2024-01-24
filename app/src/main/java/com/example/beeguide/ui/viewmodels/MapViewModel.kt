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
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface MapUiState {
    data class Success(
        val map: Map
    ) : MapUiState

    object Error : MapUiState
    object Loading : MapUiState
}

class MapViewModel(
    private val mapRepository: MapRepository
) : ViewModel() {
    companion object {
        private const val TAG = "MapViewModel"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BeeGuideApplication)
                val mapRepository = application.container.mapRepository
                MapViewModel(mapRepository = mapRepository)
            }
        }
    }

    var mapUiState: MapUiState by mutableStateOf(MapUiState.Loading)
        private set

    init {
        getMap()
    }

    fun getMap() {
        viewModelScope.launch {
            mapUiState = MapUiState.Loading
            mapUiState = try {
                MapUiState.Success(mapRepository.getMap())
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