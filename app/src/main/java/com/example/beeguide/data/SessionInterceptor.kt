package com.example.beeguide.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody


/**
 * Interceptor to add session token to requests
 */
class SessionInterceptor(context: Context) : Interceptor {

    private val sessionManager = SessionManager(context)
    private val context = context


    override fun intercept(chain: Interceptor.Chain): Response {


        val requestBuilder = chain.request().newBuilder()
        sessionManager.fetchSessionToken()?.let {
            requestBuilder.addHeader("Cookie", it)
        }

        // If token has been saved, add it to the request

        val finalRequest = requestBuilder.build();
        val response = chain.proceed(finalRequest)

        if(response.isRedirect) {
            Log.d("TestInterceptor", "Session has been interrupted with" + response.header("location"))
            val contentType = response.body!!.contentType()
            val body: ResponseBody? =
                response.header("location").toString()?.let { it.toResponseBody(contentType) }

            val newresponse = response.newBuilder().body(body).build()
            Log.d("TestInterceptor", newresponse.body.toString())

            //openUrlInBrowser(context, response.header("location").toString())
        }


        Log.d("SessionInterceptor", "response header has been received" + response.header("set-cookie"))
        response.header("set-cookie")?.let { sessionManager.saveSessionToken(it) }
        Log.d("SessionInterceptor", "Addes Header"+ finalRequest.header("Cookie"))
        Log.d("SessionInterceptor", "received:" + response.headers)
        return response;
    }
}

fun openUrlInBrowser(context: Context, url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    context.startActivity(browserIntent)
}