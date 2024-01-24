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
import com.example.beeguide.PreferencesDataStore
import com.example.beeguide.data.BeeGuideRespository
import com.example.beeguide.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// UI State
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
                TestUiState.Success(beeGuideRespository.getMap().name)
            } catch (e: IOException) {
                Log.d("TestUiState", "getMap: $e")
                TestUiState.Error
            } catch (e: HttpException) {
                Log.d("TestUiState", "getMap: $e")
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