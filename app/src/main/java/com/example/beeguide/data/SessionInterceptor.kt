package com.example.beeguide.data
import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response


/**
 * Interceptor to add session token to requests
 */
class SessionInterceptor(context: Context) : Interceptor {

    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {


        val requestBuilder = chain.request().newBuilder()

        // If token has been saved, add it to the request

        val finalRequest = requestBuilder.build();
        val response = chain.proceed(requestBuilder.build())
        Log.d("SessionInterceptor", "response header has been received" + response.header("set-cookie"))
        Log.d("SessionInterceptor", "saved"+ finalRequest.headers)
        Log.d("SessionInterceptor", "received:" + response.headers)
        return response;
    }
}