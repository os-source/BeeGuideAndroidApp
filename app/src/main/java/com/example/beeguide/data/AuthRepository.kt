package com.example.beeguide.data

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
    private val authenticator: AuthenticationManager,
) : AuthRepository {
    override suspend fun signUp(email: String, password: String): AuthResult<Unit> {
        return try {
            beeGuideApiService.signUp(request = AuthRequest(email, password))
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
        return try {
            val token: TokenResponse = beeGuideApiService.signIn(AuthRequest(email, password), false)
            authenticator.saveJWTToken(token.JWT)
            if (remember) {
                authenticator.saveRefreshToken(token.refresh)
            }
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