package com.example.beeguide.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.beeguide.BeeGuideApplication
import com.example.beeguide.data.AuthRepository
import com.example.beeguide.network.AuthResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class SignInUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
)

data class SignUpUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
)

class SignInViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    var signInUiState: SignInUiState by mutableStateOf(SignInUiState())

    private val resultChanel = Channel<AuthResult<Unit>>()
    val authResults = resultChanel.receiveAsFlow()

    fun emailChanged(email: String) {
        signInUiState = signInUiState.copy(email = email)
    }

    fun passwordChanged(password: String) {
        signInUiState = signInUiState.copy(password = password)
    }

    fun signIn() {
        viewModelScope.launch {
            signInUiState = signInUiState.copy(isLoading = true)
            val result = authRepository.signIn(signInUiState.email, signInUiState.password)
            resultChanel.send(result)
            signInUiState = signInUiState.copy(isLoading = false)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BeeGuideApplication)
                val authRepository = application.container.authRepository
                SignInViewModel(authRepository = authRepository)
            }
        }
    }
}

class SignUpViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    var signUpUiState: SignUpUiState by mutableStateOf(SignUpUiState())

    private val resultChanel = Channel<AuthResult<Unit>>()
    val authResults = resultChanel.receiveAsFlow()

    fun emailChanged(email: String) {
        signUpUiState = signUpUiState.copy(email = email)
    }

    fun passwordChanged(password: String) {
        signUpUiState = signUpUiState.copy(password = password)
    }

    fun signUp() {
        viewModelScope.launch {
            signUpUiState = signUpUiState.copy(isLoading = true)
            val result = authRepository.signUp(signUpUiState.email, signUpUiState.password)
            resultChanel.send(result)
            signUpUiState = signUpUiState.copy(isLoading = false)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BeeGuideApplication)
                val authRepository = application.container.authRepository
                SignUpViewModel(authRepository = authRepository)
            }
        }
    }
}