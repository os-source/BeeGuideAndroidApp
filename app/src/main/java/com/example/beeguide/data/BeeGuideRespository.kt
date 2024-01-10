package com.example.beeguide.data

import com.example.beeguide.model.Test
import com.example.beeguide.network.BeeGuideApiService

interface BeeGuideRespository {
    suspend fun getUser(): Test
}

class NetworkBeeGuideRepository(
    private val beeGuideApiService: BeeGuideApiService
) : BeeGuideRespository {
    override suspend fun getUser(): Test = beeGuideApiService.getUser()
}