package com.example.beeguide.data

import com.example.beeguide.model.Login
import com.example.beeguide.model.LoginRequest
import com.example.beeguide.model.Map
import com.example.beeguide.model.User
import com.example.beeguide.network.BeeGuideApiService

interface BeeGuideRepository {
    suspend fun getMap(): Map
    suspend fun Login(email: String, password: String, remember: Boolean)
    suspend fun getUser(): User
}

class NetworkBeeGuideRepository(
    private val beeGuideApiService: BeeGuideApiService, private val authenticator : AuthenticationManager
) : BeeGuideRepository {
    override suspend fun getMap(): Map = beeGuideApiService.getMap()

    override suspend fun getUser() : User = beeGuideApiService.getUser()

    override suspend fun Login(email:String, password:String, remember: Boolean)
    {
        val token:Login = beeGuideApiService.login(LoginRequest(email, password), false);
        authenticator.saveJWTToken(token.JWT);
        if(remember)
        {
            authenticator.saveRefreshToken(token.refresh);
        }


    }


}