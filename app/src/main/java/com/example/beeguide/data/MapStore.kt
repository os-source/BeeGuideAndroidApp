package com.example.beeguide.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.beeguide.data.MapKeyValueStore.MapStoreKeys.MapID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREF_NAME = "MapStore"

class MapStoreOld(context: Context) {

    private val StoreReader: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val StoreWriter: SharedPreferences.Editor = StoreReader.edit()
}

private val Context.mapStore: DataStore<Preferences> by preferencesDataStore("MapStore")

class MapKeyValueStore(val context: Context){
    private object MapStoreKeys{
        val MapID: Preferences.Key<Int> = intPreferencesKey("MapID")
    }

    suspend fun updateMapID(ID: Int) {
        context.mapStore.edit { preferences: MutablePreferences -> preferences[MapID] = ID}
        Log.d("beeguide-beacon-monitor", "Updated MapID")
    }

    fun watchMapID(): Flow<Int>{
        Log.d("beeguide-beacon-monitor", "Watched MapID")
        return context.mapStore.data.map { preferences : Preferences -> return@map preferences[MapID] ?: 0 }

    }
}

