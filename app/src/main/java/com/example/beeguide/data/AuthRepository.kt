package com.example.beeguide.data

import android.util.Log
import com.example.beeguide.model.SignInRequest
import com.example.beeguide.model.SignUpRequest
import com.example.beeguide.model.TokenResponse
import com.example.beeguide.network.AuthResult
import com.example.beeguide.network.BeeGuideApiService
import retrofit2.HttpException

interface AuthRepository {
    suspend fun signUp(email: String, password: String, name: String): AuthResult<Unit>
    suspend fun signIn(email: String, password: String, remember: Boolean): AuthResult<Unit>
}

class NetworkAuthRepository(
    private val beeGuideApiService: BeeGuideApiService,
    private val authenticationManager: AuthenticationManager,
) : AuthRepository {
    override suspend fun signUp(email: String, password: String, name: String): AuthResult<Unit> {
        return try {
            beeGuideApiService.signUp(SignUpRequest(email, password, name))
            signIn(email, password, false)
        } catch (e: HttpException) {
            Log.d("NetworkAuthRepository", e.toString())
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
        try {
            val token: TokenResponse = beeGuideApiService.signIn(SignInRequest(email, password), false)
            authenticationManager.saveJWTToken(token.JWT)
            if (remember) {
                authenticationManager.saveRefreshToken(token.refresh)
            }
            return AuthResult.Authorized()
        } catch (e: HttpException) {
            Log.d("NetworkAuthRepository", e.toString())
            if (e.code() == 401) {
                return AuthResult.Unauthorized()
            } else {
                return AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            Log.d("NetworkAuthRepository", e.toString())
            return AuthResult.UnknownError()
        }
    }
}