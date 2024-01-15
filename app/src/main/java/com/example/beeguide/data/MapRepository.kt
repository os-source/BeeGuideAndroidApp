package com.example.beeguide.data

import com.example.beeguide.model.Map
import com.example.beeguide.network.BeeGuideApiService

interface MapRepository {
    suspend fun getMap(): Map
}

class NetworkMapRepository(
    private val beeGuideApiService: BeeGuideApiService
) : MapRepository {
    override suspend fun getMap(): Map = beeGuideApiService.getMap()
}