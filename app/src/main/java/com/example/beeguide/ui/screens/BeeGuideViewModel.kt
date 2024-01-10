package com.example.beeguide.ui.screens

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
import com.example.beeguide.PreferencesDataStore
import com.example.beeguide.data.BeeGuideRespository
import com.example.beeguide.model.MarsPhoto
import com.example.beeguide.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

// UI State
sealed interface MarsUiState {
    data class Success(val photos: List<MarsPhoto>) : MarsUiState
    object Error : MarsUiState
    object Loading : MarsUiState
}

sealed interface UserUiState {
    data class Success(
        val user: User
    ) : UserUiState

    object Error : UserUiState
    object Loading : UserUiState
}

sealed interface TestUiState {
    data class Success(
        val test: String
    ) : TestUiState

    object Error : TestUiState
    object Loading : TestUiState
}

/*class MarsViewModel(private val marsPhotosRepository: MarsPhotosRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Loading)
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getMarsPhotos()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     */
    fun getMarsPhotos() {
        viewModelScope.launch {
            marsUiState = MarsUiState.Loading
            marsUiState = try {
                MarsUiState.Success(marsPhotosRepository.getMarsPhotos())
            } catch (e: IOException) {
                MarsUiState.Error
            } catch (e: HttpException) {
                MarsUiState.Error
            }
        }
    }

    /**
     * Factory for [MarsViewModel] that takes [MarsPhotosRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BeeGuideApplication)
                val marsPhotosRepository = application.container.marsPhotosRepository
                MarsViewModel(marsPhotosRepository = marsPhotosRepository)
            }
        }
    }
}*/

class UserViewModel(private val beeGuideRespository: BeeGuideRespository) : ViewModel() {
    var userUiState: UserUiState by mutableStateOf(UserUiState.Loading)
        private set

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            userUiState = UserUiState.Loading
            userUiState = try {
                UserUiState.Success(User("John", "Doe"))
            } catch (e: IOException) {
                UserUiState.Error
            } catch (e: HttpException) {
                UserUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BeeGuideApplication)
                val beeGuideRespository = application.container.beeGuideRespository
                UserViewModel(beeGuideRespository = beeGuideRespository)
            }
        }
    }
}


class TestViewModel(private val beeGuideRespository: BeeGuideRespository) : ViewModel() {
    var testUiState: TestUiState by mutableStateOf(TestUiState.Loading)
        private set

    init {
        getTest()
    }

    fun getTest() {
        viewModelScope.launch {
            testUiState = TestUiState.Loading
            testUiState = try {
                TestUiState.Success(beeGuideRespository.getUser().title)
            } catch (e: IOException) {
                TestUiState.Error
            } catch (e: HttpException) {
                TestUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BeeGuideApplication)
                val beeGuideRespository = application.container.beeGuideRespository
                TestViewModel(beeGuideRespository = beeGuideRespository)
            }
        }
    }
}



class AppearanceViewModel(
    private val bol: Boolean
) : ViewModel() {
    var darkMode by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {
            darkMode = bol
        }
    }

    fun update() {
        darkMode = !darkMode
        /*viewModelScope.launch {
            preferencesDataStore.setDarkThemeMode(darkMode)
        }*/
    }

    companion object {
        class Factory(private val bool: Boolean) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AppearanceViewModel(bool) as T
            }
        }
    }
}

fun getDarkThemeMode(): Boolean {
    CoroutineScope(Dispatchers.IO).launch {
        val preferencesDataStore = PreferencesDataStore(BeeGuideApplication())
        Log.d("AppearanceViewModel", "create: ${preferencesDataStore.getDarkThemeMode()}")
    }
    return true
}



//darkTheme: Boolean = isSystemInDarkTheme(),