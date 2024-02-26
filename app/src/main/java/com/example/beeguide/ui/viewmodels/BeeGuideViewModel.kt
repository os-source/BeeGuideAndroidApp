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
import com.example.beeguide.data.BeeGuideRepository
import com.example.beeguide.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import java.io.IOException

// UI State
sealed interface UserUiState {
    data class Success(
        val user: User
    ) : UserUiState

    object Error : UserUiState
    object Loading : UserUiState
}

class UserViewModel(private val beeGuideRepository: BeeGuideRepository) : ViewModel() {
    var userUiState: UserUiState by mutableStateOf(UserUiState.Loading)
        private set

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            userUiState = UserUiState.Loading
            userUiState = try {
                UserUiState.Success(beeGuideRepository.getUser())
            } catch (e: IOException) {
                Log.d("UserUiState", e.toString())
                UserUiState.Error
            } catch (e: HttpException) {
                Log.d("UserUiState", e.toString())
                UserUiState.Error
            }
        }
    }

    fun nameChanged(name: String) {
        val oldUserUiState = userUiState
        if (oldUserUiState is UserUiState.Success) {
            userUiState = UserUiState.Success(oldUserUiState.user.copy(name = name))
        }
    }

    fun bioChanged(bio: String) {
        val oldUserUiState = userUiState
        if (oldUserUiState is UserUiState.Success) {
            userUiState = UserUiState.Success(
                oldUserUiState.user.copy(
                    userDetails = oldUserUiState.user.userDetails?.copy(bio = bio)
                )
            )
        }
    }

    fun saveUpdatedUser(navigateToScreen: () -> Unit, file: File?) {
        viewModelScope.launch {
            val oldUserUiState = userUiState
            if (oldUserUiState is UserUiState.Success) {
                viewModelScope.launch {
                    beeGuideRepository.saveUserName(oldUserUiState.user.name)
                    if (oldUserUiState.user.userDetails?.bio != null) {
                        beeGuideRepository.saveUserBio(oldUserUiState.user.userDetails.bio)
                    }
                    if (file != null) {
                        beeGuideRepository.saveUserProfilePicture(file)
                    }
                }
                navigateToScreen()
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BeeGuideApplication)
                val beeGuideRepository = application.container.beeGuideRepository
                UserViewModel(beeGuideRepository = beeGuideRepository)
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