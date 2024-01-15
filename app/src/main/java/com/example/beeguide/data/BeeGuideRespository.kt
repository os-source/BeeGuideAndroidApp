package com.example.beeguide.data

import com.example.beeguide.model.Map
import com.example.beeguide.network.BeeGuideApiService

interface BeeGuideRespository {
    suspend fun getMap(): Map
}

class NetworkBeeGuideRepository(
    private val beeGuideApiService: BeeGuideApiService
) : BeeGuideRespository {
    override suspend fun getMap(): Map = beeGuideApiService.getMap()
}