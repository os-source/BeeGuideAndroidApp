package com.example.beeguide.data

import com.example.beeguide.model.BioRequest
import com.example.beeguide.model.Map
import com.example.beeguide.model.NameRequest
import com.example.beeguide.model.User
import com.example.beeguide.network.BeeGuideApiService

interface BeeGuideRepository {
    suspend fun getMap(): Map
    suspend fun getUser(): User
    suspend fun saveUserName(name: String)
    suspend fun saveUserBio(bio: String)
}

class NetworkBeeGuideRepository(
    private val beeGuideApiService: BeeGuideApiService,
) : BeeGuideRepository {
    override suspend fun getMap(): Map = beeGuideApiService.getMap()
    override suspend fun getUser(): User = beeGuideApiService.getUser()
    override suspend fun saveUserName(name: String) = beeGuideApiService.saveUserName(
        NameRequest(name)
    )

    override suspend fun saveUserBio(bio: String) = beeGuideApiService.saveUserBio(BioRequest(bio))
}