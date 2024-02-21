package com.example.beeguide.data

import android.util.Log
import com.example.beeguide.model.AuthRequest
import com.example.beeguide.model.TokenResponse
import com.example.beeguide.network.AuthResult
import com.example.beeguide.network.BeeGuideApiService
import retrofit2.HttpException

interface AuthRepository {
    suspend fun signUp(email: String, password: String): AuthResult<Unit>
    suspend fun signIn(email: String, password: String, remember: Boolean): AuthResult<Unit>
}

class NetworkAuthRepository(
    private val beeGuideApiService: BeeGuideApiService,
    private val authenticationManager: AuthenticationManager,
) : AuthRepository {
    override suspend fun signUp(email: String, password: String): AuthResult<Unit> {
        return try {
            beeGuideApiService.signUp(AuthRequest(email, password))
            signIn(email, password, false)
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

    override suspend fun signIn(email: String, password: String, remember: Boolean): AuthResult<Unit> {
        Log.d("test", "blala")
        try {
            Log.d("test", "blala2")
            val token: TokenResponse = beeGuideApiService.signIn(AuthRequest(email, password), false)
            Log.d("test", "blala3")
            authenticationManager.saveJWTToken(token.JWT)
            Log.d("test", "blala4")
            if (remember) {
                authenticationManager.saveRefreshToken(token.refresh)
            }
            Log.d("test", "blala5")
            return AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                return AuthResult.Unauthorized()
            } else {
                return AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            return AuthResult.UnknownError()
        }
    }
}