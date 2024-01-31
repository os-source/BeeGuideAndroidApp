package com.example.beeguide.data

import com.example.beeguide.model.Map
import com.example.beeguide.network.BeeGuideApiService
import okhttp3.ResponseBody
import retrofit2.Call

interface BeeGuideRespository {
    suspend fun getMap(): Map
    suspend fun login(): String
}

class NetworkBeeGuideRepository(
    private val beeGuideApiService: BeeGuideApiService
) : BeeGuideRespository {
    override suspend fun getMap(): Map = beeGuideApiService.getMap()

    override suspend fun login(): String = beeGuideApiService.login()

}