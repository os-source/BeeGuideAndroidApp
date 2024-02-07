package com.example.beeguide.data
import android.content.Context
import com.example.beeguide.BuildConfig
import com.example.beeguide.navigation.preconditions.SensorGetter
import com.example.beeguide.network.BeeGuideApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

interface AppContainer {
    val beeGuideRespository: BeeGuideRespository
    val mapRepository: MapRepository
    val sensorRepository: SensorRepository
}

class DefaultAppContainer(context: Context): AppContainer {
    private val baseUrl = "https://beeguide.at/api/v1/"
    private val apiKey = BuildConfig.API_KEY

    private val interceptor = Interceptor { chain ->
        val request = chain.request()
            .newBuilder()
            .addHeader("Api-Key", "$apiKey")
            .build()
        chain.proceed(request)
    }

    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(SessionInterceptor(context))
            .followRedirects(false)
            .build()
    }





    // Using Retrofit builder to build a retrofit object using a kotlinx.serialization converter



    private fun retrofit(context: Context): BeeGuideApiService {
        return Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(baseUrl)
            .client(okhttpClient(context))
            .build()
            .create(BeeGuideApiService::class.java)

    }

    private fun retroAuth (context: Context): AuthService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okhttpClient(context))
            .build()
            .create(AuthService::class.java)

    }

    private val retrofitService: BeeGuideApiService by lazy {
       retrofit(context)
    }

    private val sensorGetter: SensorGetter = SensorGetter()

    override val beeGuideRespository: BeeGuideRespository by lazy {
        NetworkBeeGuideRepository(retrofitService)
    }

    override val mapRepository: MapRepository by lazy {
        NetworkMapRepository(retrofitService)
    }

    override val sensorRepository: SensorRepository by lazy {
        HardwareSensorRepository(context)
    }

}