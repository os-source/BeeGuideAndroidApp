package com.example.beeguide.network

import com.example.beeguide.model.Login
import com.example.beeguide.model.LoginRequest
import com.example.beeguide.model.Map
import com.example.beeguide.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BeeGuideApiService {
    @GET("regions/426C7565-4368-6172-6D43-6561636F6E73/maps/3000")
    suspend fun getMap(): Map

    @GET("/user")
    suspend fun getUser(): User
    //getMapXML as String


    @POST("login?rememberme={remember}")
    suspend fun login(
            @Body body: LoginRequest,
            @Query("rememberme") remember : Boolean

        ): Login

    @GET("/maps/{id}/file")
    suspend fun getMapFile(@Path("id") map_id: Int): String
}