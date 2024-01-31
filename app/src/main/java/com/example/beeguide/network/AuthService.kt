package com.example.beeguide.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface AuthService {

    @GET("user")
    suspend fun login() : Call<ResponseBody>

}