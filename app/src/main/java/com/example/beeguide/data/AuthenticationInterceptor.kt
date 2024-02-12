package com.example.beeguide.data

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor (
    private val tokenManager: AuthenticationManager): Interceptor
{

    override fun intercept(chain: Interceptor.Chain): Response
    {

        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        tokenManager.fetchSessionToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer: " + it)
        } //add JWT Token if ready

        val finalRequest = requestBuilder.build();
        val response = chain.proceed(requestBuilder.build())
        return response;

    }



}