package com.example.beeguide.data

import android.content.SharedPreferences

class AuthenticationManager(dataStore: SharedPreferences) {
    private final val prefs = dataStore;

    companion object {
        const val JWT_TOKEN = "JWT_TOKEN"
        const val JWT_REFRESH = "REFRESH_TOKEN"
        const val LOGGER = "AuthenticationManager"
    }

    fun saveJWTToken(token: String) {
        val editor = prefs.edit()
        editor.putString(JWT_TOKEN, token)
        editor.apply()
    }

    fun saveRefreshToken(token: String?) {
        val editor = prefs.edit()
        editor.putString(JWT_REFRESH, token)
        editor.apply()
    }

    fun fetchJWTToken(): String? {
        val token = prefs.getString(JWT_TOKEN, null)
        return token;
    }

    fun clearAllTokens() {
        prefs.edit().remove(JWT_REFRESH).apply()
        prefs.edit().remove(JWT_TOKEN).apply()
    }
}