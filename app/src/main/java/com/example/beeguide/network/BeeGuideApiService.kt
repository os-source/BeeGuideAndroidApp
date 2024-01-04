package com.example.beeguide.network

import com.example.beeguide.model.MarsPhoto
import com.example.beeguide.model.User
import retrofit2.http.GET

interface MarsApiService {
    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto>
}

interface BeeGuideApiService {
    @GET("user")
    suspend fun getUser(): User
}