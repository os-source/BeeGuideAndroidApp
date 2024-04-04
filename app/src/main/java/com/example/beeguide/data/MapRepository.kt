package com.example.beeguide.data

import com.example.beeguide.model.Map
import com.example.beeguide.network.BeeGuideApiService

interface MapRepository {
    suspend fun getMap(uuId: String, major: Int): Map
    suspend fun getMapFile(mapId: Int): String
}

class NetworkMapRepository(
    private val beeGuideApiService: BeeGuideApiService
) : MapRepository {
    override suspend fun getMap(uuId: String, major: Int): Map = beeGuideApiService.getMap(uuId, major)
    override suspend fun getMapFile(mapId: Int): String = beeGuideApiService.getMapFile(mapId)
}