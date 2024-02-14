package com.example.beeguide.data

import android.content.Context
import android.content.SharedPreferences
import com.example.beeguide.BuildConfig
import com.example.beeguide.R
import com.example.beeguide.navigation.preconditions.SensorGetter
import com.example.beeguide.network.BeeGuideApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface AppContainer {
    val beeGuideRepository: BeeGuideRepository
    val authRepository: AuthRepository
    val mapRepository: MapRepository
    val sensorRepository: SensorRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val baseUrl = "https://beeguide.at/api/v1/"
    private val apiKey = BuildConfig.API_KEY

    private val interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder().addHeader("Api-Key", apiKey).build()
        chain.proceed(request)
    }

    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(
            AuthenticationInterceptor(
                this.provideJwtTokenManager(
                    this.provideDataStore(
                        context
                    )
                )
            )
        ).build()
    }

    fun provideDataStore(appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(
            appContext.getString(R.string.app_name), Context.MODE_PRIVATE
        )
    }

    fun provideJwtTokenManager(dataStore: SharedPreferences): AuthenticationManager {
        return AuthenticationManager(dataStore)
    }

    // Using Retrofit builder to build a retrofit object using a kotlinx.serialization converter

    private fun retrofit(context: Context): BeeGuideApiService {
        return Retrofit.Builder()
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(baseUrl).client(okhttpClient(context)).build()
            .create(BeeGuideApiService::class.java)
    }

    private val retrofitService: BeeGuideApiService by lazy {
        retrofit(context)
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    private val sensorGetter: SensorGetter = SensorGetter()

    override val beeGuideRepository: BeeGuideRepository by lazy {
        NetworkBeeGuideRepository(
            retrofitService, provideJwtTokenManager(provideDataStore(context))
        )
    }

    override val authRepository: AuthRepository by lazy {
        NetworkAuthRepository(retrofitService, prefs)
    }

    override val mapRepository: MapRepository by lazy {
        NetworkMapRepository(retrofitService)
    }

    override val sensorRepository: SensorRepository by lazy {
        HardwareSensorRepository(context)
    }
}