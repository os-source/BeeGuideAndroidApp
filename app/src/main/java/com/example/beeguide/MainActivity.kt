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
import androidx.lifecycle.Observer
import com.example.beeguide.ui.BeeGuideApp
import com.example.beeguide.ui.screens.AppearanceViewModel
import com.example.beeguide.ui.theme.BeeGuideTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.Region

class MainActivity : ComponentActivity() {

    //private val preferencesDataStore by lazy { PreferencesDataStore(this) }

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    companion object {
        private const val REQUEST_ENABLE_BT = 1
        private const val REQUEST_LOCATION_PERMISSION = 2
        private val TAG = "MainActivity"
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




        //val intent = Intent(this, MonitoringActivity::class.java)
        //startActivity(intent)

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("AppearanceViewModel", "create: ${preferencesDataStore.getDarkThemeMode()}")
        }

        setContent {
            BeeGuideTheme(appearanceViewModel) {
                BeeGuideApp(appearanceViewModel)
            }
        }

        Log.d(TAG, "Heloo")
        startBeaconMonitor()
    }

    val monitoringObserver = Observer<Int> {state ->
        if (state == MonitorNotifier.OUTSIDE) {
            Log.d(TAG, "outside beacon region: ")
        }
        else {
            Log.d(TAG, "inside beacon region: ")

        }
    }

    private fun startBeaconMonitor(){
        val beaconManager =  BeaconManager.getInstanceForApplication(this)

        // The example shows how to find iBeacon.
        val parser = BeaconParser().
            setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
        parser.setHardwareAssistManufacturerCodes(arrayOf(0x004c).toIntArray())
        beaconManager.beaconParsers.add(
            parser)


        val region = Region("all-beacons", null, null, null)
        // Set up a Live Data observer for beacon data
        val regionViewModel = BeaconManager.getInstanceForApplication(this).getRegionViewModel(region)
        // observer will be called each time the monitored regionState changes (inside vs. outside region)

        regionViewModel.rangedBeacons.observe(this){beacons->
            val rangeAgeMillis = System.currentTimeMillis() - (beacons.firstOrNull()?.lastCycleDetectionTimestamp ?: 0)
            if (rangeAgeMillis < 10000) {
                Log.d(MainActivity.TAG, "Ranged: ${beacons.count()} beacons")
                for (beacon: Beacon in beacons) {
                    Log.d(TAG, "$beacon about ${beacon.distance} meters away")
                }
            }
            else {
                Log.d(MainActivity.TAG, "Ignoring stale ranged beacons from $rangeAgeMillis millis ago")
            }
        }
        regionViewModel.regionState.observe(this, monitoringObserver)
        beaconManager.startMonitoring(region)
        beaconManager.startRangingBeacons(region)
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