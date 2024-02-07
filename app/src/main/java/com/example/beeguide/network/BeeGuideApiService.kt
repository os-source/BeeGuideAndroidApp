package com.example.beeguide.network

import com.example.beeguide.model.Map
import com.example.beeguide.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface BeeGuideApiService {
    @GET("regions/426C7565-4368-6172-6D43-6561636F6E73/maps/3000")
    suspend fun getMap(): Map
    //chck for redirect, Status Code: 302
    @GET("login/oauth2/authorization/google")
    suspend fun login(): String
    //authorization url
    @GET("/user")
    suspend fun getUser(): User
    //getMapXML as String

    @GET("/maps/{id}/file")
    suspend fun getMapFile(@Path("id") map_id: Int): String



}