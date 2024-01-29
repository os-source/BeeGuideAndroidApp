package com.example.beeguide.data

import com.example.beeguide.model.Login
import com.example.beeguide.model.Map
import com.example.beeguide.network.BeeGuideApiService

interface BeeGuideRespository {
    suspend fun getMap(): Map
    suspend fun login(): Login
}

class NetworkBeeGuideRepository(
    private val beeGuideApiService: BeeGuideApiService
) : BeeGuideRespository {
    override suspend fun getMap(): Map = beeGuideApiService.getMap()

    override suspend fun login(): Login = beeGuideApiService.login()
}