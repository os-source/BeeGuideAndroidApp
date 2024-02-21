package com.example.beeguide.data

import com.example.beeguide.model.Map
import com.example.beeguide.model.User
import com.example.beeguide.network.BeeGuideApiService

interface BeeGuideRepository {
    suspend fun getMap(): Map
    suspend fun getUser(): User
}

class NetworkBeeGuideRepository(
    private val beeGuideApiService: BeeGuideApiService,
) : BeeGuideRepository {
    override suspend fun getMap(): Map = beeGuideApiService.getMap()
    override suspend fun getUser(): User = beeGuideApiService.getUser()
}