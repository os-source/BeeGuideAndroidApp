package com.example.beeguide.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.beeguide.BeeGuideApplication
import com.example.beeguide.data.BeeGuideRespository
import com.example.beeguide.model.User
import com.example.beeguide.ui.screens.TestUiState
import com.example.beeguide.ui.screens.UserUiState
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

