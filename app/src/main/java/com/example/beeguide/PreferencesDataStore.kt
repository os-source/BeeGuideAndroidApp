package com.example.beeguide

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class PreferencesDataStore(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private var pref = context.dataStore

    private val darkThemeMode = booleanPreferencesKey("dark_theme_mode")

    suspend fun setDarkThemeMode(newDarkThemeMode: Boolean) {
        pref.edit {
            it[darkThemeMode] = newDarkThemeMode
        }
    }

    suspend fun getDarkThemeMode(): Boolean {
        return pref.data.first()[darkThemeMode] ?: false
    }
}


