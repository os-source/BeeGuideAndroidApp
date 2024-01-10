package com.example.beeguide

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.beeguide.navigation.MonitoringActivity
import com.example.beeguide.ui.BeeGuideApp
import com.example.beeguide.ui.screens.AppearanceViewModel
import com.example.beeguide.ui.theme.BeeGuideTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    //private val preferencesDataStore by lazy { PreferencesDataStore(this) }

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    companion object {
        private const val REQUEST_ENABLE_BT = 1
        private const val REQUEST_LOCATION_PERMISSION = 2
    }

    private val appearanceViewModel by viewModels<AppearanceViewModel>(
        factoryProducer = {
            AppearanceViewModel.Companion.Factory(false)
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var preferencesDataStore = PreferencesDataStore(this)

        // Initialisiere den Launcher für die Berechtigungsanfrage
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    // Standortberechtigung wurde erteilt, führen Sie Ihre Bluetooth-Aktionen durch
                } else {
                    // Standortberechtigung wurde nicht erteilt, behandeln Sie dies entsprechend
                }
            }


        // BluetoothManager initialisieren
        val bluetoothManager =
            getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        // Bluetooth-Status überprüfen
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            // Bluetooth ist deaktiviert, fordern Sie den Benutzer auf, es zu aktivieren
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            // Bluetooth ist aktiviert, überprüfen Sie die Standortberechtigung
            checkLocationPermission()
        }




        val intent = Intent(this, MonitoringActivity::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("AppearanceViewModel", "create: ${preferencesDataStore.getDarkThemeMode()}")
        }

        setContent {
            BeeGuideTheme(appearanceViewModel) {
                BeeGuideApp(appearanceViewModel)
            }
        }

    }

    private fun checkLocationPermission() {
        // Überprüfen, ob die Standortberechtigung erteilt wurde
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Wenn nicht, die Berechtigung anfordern
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Die Standortberechtigung wurde bereits erteilt
            // Führen Sie hier Ihre Bluetooth-Aktionen durch
        }
    }

}



fun getDarkThemeMode(): Boolean {
    CoroutineScope(Dispatchers.IO).launch {
        val preferencesDataStore = PreferencesDataStore(BeeGuideApplication())
        Log.d("AppearanceViewModel", "create: ${preferencesDataStore.getDarkThemeMode()}")
    }
    return true
}