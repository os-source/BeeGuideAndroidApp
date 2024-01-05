package com.example.beeguide.data

import com.example.beeguide.model.User
import com.example.beeguide.network.BeeGuideApiService

interface  BeeGuideRespository {
    suspend fun getUser(): User
}

class NetworkBeeGuideRepository(
    private val beeGuideApiService: BeeGuideApiService
) : BeeGuideRespository {
    override suspend fun getUser(): User = beeGuideApiService.getUser()
}