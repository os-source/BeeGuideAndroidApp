package com.example.beeguide.data

import android.content.Context
import android.content.SharedPreferences
import com.example.beeguide.R

class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val SESSION_TOKEN = "SESSION_TOKEN"
    }

    /**
     * Function to save session token
     */
    fun saveSessionToken(token: String) {
        val editor = prefs.edit()
        editor.putString(SESSION_TOKEN, token)
        editor.apply()
    }

    /**
     * Function to fetch auth token
     */
    fun fetchSessionToken(): String? {
        return prefs.getString(SESSION_TOKEN, null)
    }
}