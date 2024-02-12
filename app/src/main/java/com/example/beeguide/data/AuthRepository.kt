package com.example.beeguide.data

import android.content.SharedPreferences
import com.example.beeguide.model.AuthRequest
import com.example.beeguide.network.AuthResult
import com.example.beeguide.network.BeeGuideApiService
import retrofit2.HttpException

interface AuthRepository {
    suspend fun signUp(email: String, password: String): AuthResult<Unit>
    suspend fun signIn(email: String, password: String): AuthResult<Unit>
}

class NetworkAuthRepository(
    private val beeGuideApiService: BeeGuideApiService,
    private val prefs: SharedPreferences
) : AuthRepository {
    override suspend fun signUp(email: String, password: String): AuthResult<Unit> {
        return try {
            beeGuideApiService.signUp(request = AuthRequest(email, password))
            signIn(email, password)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }
    override suspend fun signIn(email: String, password: String): AuthResult<Unit> {
        return try {
            val response = beeGuideApiService.signIn(request = AuthRequest(email, password))
            prefs.edit().putString("jwt", response.token).apply()
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }
}