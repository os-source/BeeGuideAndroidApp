package com.example.beeguide.network

import com.example.beeguide.model.MarsPhoto
import com.example.beeguide.model.Test
import retrofit2.http.GET

interface MarsApiService {
    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto>
}

interface BeeGuideApiService {
    @GET("1")
    suspend fun getUser(): Test
}