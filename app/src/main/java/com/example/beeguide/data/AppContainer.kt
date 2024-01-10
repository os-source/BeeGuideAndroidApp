package com.example.beeguide.data

import com.example.beeguide.network.BeeGuideApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val beeGuideRespository: BeeGuideRespository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://jsonplaceholder.typicode.com/todos/"

    /** Using Retrofit builder to build a retrofit object using a kotlinx.serialization converter */
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: BeeGuideApiService by lazy {
        retrofit.create(BeeGuideApiService::class.java)
    }

    override val beeGuideRespository: BeeGuideRespository by lazy {
        NetworkBeeGuideRepository(retrofitService)
    }
}