package com.example.beeguide.network

import com.example.beeguide.model.AuthRequest
import com.example.beeguide.model.Map
import com.example.beeguide.model.TokenResponse
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

    @POST("/signup")
    suspend fun signUp(@Body request: AuthRequest)

    @POST("/login")
    suspend fun signIn(
        @Body body: AuthRequest,
        @Query("rememberme") remember: Boolean
    ): TokenResponse

    @GET("/maps/{id}/file")
    suspend fun getMapFile(@Path("id") map_id: Int): String
}