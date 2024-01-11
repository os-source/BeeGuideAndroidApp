package com.example.beeguide

import android.bluetooth.BluetoothAdapter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.beeguide.navigation.preconditions.PermissionChecker
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

        val preferencesDataStore = PreferencesDataStore(this)
        val permissionChecker : PermissionChecker = PermissionChecker(this); permissionChecker.check()

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
            Log.d(TAG, "System Time ${System.currentTimeMillis()}")
            Log.d(TAG, "Last Beacon Detection Timestamp ${(beacons.firstOrNull()?.lastCycleDetectionTimestamp ?: 0)}")
            if (rangeAgeMillis < 10000) {
                Log.d(MainActivity.TAG, "Ranged: ${beacons.count()} beacons")
                Log.d(TAG, "Regions count ${beaconManager.rangedRegions.size}")
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
        beaconManager.startMonitoring(region)
        beaconManager.startRangingBeacons(region)
    }
}

fun getDarkThemeMode(): Boolean {
    CoroutineScope(Dispatchers.IO).launch {
        val preferencesDataStore = PreferencesDataStore(BeeGuideApplication())
        Log.d("AppearanceViewModel", "create: ${preferencesDataStore.getDarkThemeMode()}")
    }
    return true
}