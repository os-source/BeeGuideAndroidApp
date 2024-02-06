package com.example.beeguide.network

import com.example.beeguide.model.Map
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface BeeGuideApiService {
    @GET("regions/426C7565-4368-6172-6D43-6561636F6E73/maps/3000")
    suspend fun getMap(): Map
    //chck for redirect, Status Code: 302
    @GET("login/oauth2/authorization/google")
    suspend fun login(): String
    //authorization url
    @GET("/login/oauth2/authorization/google")
    suspend fun logon(): Call<ResponseBody>

    @GET("/maps/{id}/file")
    suspend fun getMapFile(): String



}