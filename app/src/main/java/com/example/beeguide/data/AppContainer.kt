package com.example.beeguide.data

import com.example.beeguide.BuildConfig
import com.example.beeguide.network.BeeGuideApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface AppContainer {
    val beeGuideRespository: BeeGuideRespository
    val mapRepository: MapRepository
}

class DefaultAppContainer : AppContainer {
    private val baseUrl = "https://beeguide.at/api/v1/"
    private val apiKey = BuildConfig.API_KEY

    private val interceptor = Interceptor { chain ->
        val request = chain.request()
            .newBuilder()
            .addHeader("Api-Key", "$apiKey")
            .build()
        chain.proceed(request)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    // Using Retrofit builder to build a retrofit object using a kotlinx.serialization converter
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()

    private val retrofitService: BeeGuideApiService by lazy {
        retrofit.create(BeeGuideApiService::class.java)
    }

    override val beeGuideRespository: BeeGuideRespository by lazy {
        NetworkBeeGuideRepository(retrofitService)
    }

    override val mapRepository: MapRepository by lazy {
        NetworkMapRepository(retrofitService)
    }
}