package com.example.beeguide.data

import android.content.SharedPreferences
import android.util.Log

class AuthenticationManager(dataStore: SharedPreferences) {
    private final val prefs = dataStore;

    companion object {
        const val JWT_TOKEN = "JWT_TOKEN"
        const val JWT_REFRESH = "REFRESH_TOKEN"
        const val LOGGER = "AuthenticationManager"
    }

   // save session token
    fun saveJWTToken(token: String) {
        val editor = prefs.edit()
        editor.putString(JWT_TOKEN, token)
        editor.apply()
        Log.d(LOGGER, "Saved JWT: " + token)
    }

    fun saveRefreshToken(token: String?) {
        val editor = prefs.edit()
        editor.putString(JWT_REFRESH, token)
        editor.apply()
        Log.d(LOGGER, "Saved JWT: " + token)
    }

    // fetch auth token
    fun fetchJWTToken(): String? {
        val token = prefs.getString(JWT_TOKEN, null)
        Log.d(LOGGER, "Retrieved Token: " + token)
        return token;

    }

    fun clearAllTokens() {
        prefs.edit().remove(JWT_REFRESH)
        prefs.edit().remove(JWT_TOKEN)
    }
}