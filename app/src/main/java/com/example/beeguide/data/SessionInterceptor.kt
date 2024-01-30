package com.example.beeguide.data

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response


/**
 * Interceptor to add auth token to requests
 */
class SessionInterceptor(context: Context) : Interceptor {
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request
        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("Cookie", "JSESSION=")
        }

        return chain.proceed(requestBuilder.build())
    }
}