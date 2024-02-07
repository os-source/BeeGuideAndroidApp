package com.example.beeguide.data

import com.example.beeguide.model.Map
import com.example.beeguide.model.User
import com.example.beeguide.network.BeeGuideApiService

interface BeeGuideRespository {
    suspend fun getMap(): Map
    suspend fun login(): String
    suspend fun getUser(): User


}

class NetworkBeeGuideRepository(
    private val beeGuideApiService: BeeGuideApiService
) : BeeGuideRespository {
    override suspend fun getMap(): Map = beeGuideApiService.getMap()

    override suspend fun login(): String = beeGuideApiService.login()

    override suspend fun getUser() : User = beeGuideApiService.getUser()


}