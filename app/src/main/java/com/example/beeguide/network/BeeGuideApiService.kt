package com.example.beeguide.network

import com.example.beeguide.model.Login
import com.example.beeguide.model.Map
import retrofit2.http.GET

interface BeeGuideApiService {
    @GET("regions/426C7565-4368-6172-6D43-6561636F6E73/maps/3000")
    suspend fun getMap(): Map

    @GET("user")
    suspend fun login(): Login
}