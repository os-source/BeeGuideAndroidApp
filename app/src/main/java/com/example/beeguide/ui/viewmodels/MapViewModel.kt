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
import com.example.beeguide.data.BeeGuideRespository
import com.example.beeguide.data.MapRepository
import com.example.beeguide.ui.screens.MapUiState
import com.example.beeguide.ui.screens.TestUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MapViewModel(
    private val mapRepository: MapRepository
) : ViewModel() {
    companion object {
        private const val TAG = "MapViewModel"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BeeGuideApplication)
                MapViewModel(mapRepository = application.container.mapRepository)
            }
        }
    }
    private val _mapUiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val mapUiState: StateFlow<MapUiState> = _mapUiState.asStateFlow()

    init {
        getMap()
    }

    fun getMap() {
        viewModelScope.launch {
            _mapUiState.update {
                MapUiState.Loading
            }
            _mapUiState.update {
                try {
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

}