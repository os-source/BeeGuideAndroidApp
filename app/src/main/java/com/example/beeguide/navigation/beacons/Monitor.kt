package com.example.beeguide.navigation.beacons

import android.util.Log
import androidx.activity.ComponentActivity
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.Region
import androidx.lifecycle.Observer

class Monitor(private var componentActivity: ComponentActivity){
    private val logTag: String = "beeguide-beacon-monitor"

    fun start(){
        val beaconManager =  BeaconManager.getInstanceForApplication(componentActivity)

        // The example shows how to find iBeacon.
        val parser = BeaconParser().
        setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
        parser.setHardwareAssistManufacturerCodes(arrayOf(0x004c).toIntArray())
        beaconManager.beaconParsers.add(
            parser)

        val region = Region("all-beacons", null, null, null)
        // Set up a Live Data observer for beacon data
        val regionViewModel = BeaconManager.getInstanceForApplication(componentActivity).getRegionViewModel(region)
        // observer will be called each time the monitored regionState changes (inside vs. outside region)

        regionViewModel.rangedBeacons.observe(componentActivity){beacons->
            val rangeAgeMillis = System.currentTimeMillis() - (beacons.firstOrNull()?.lastCycleDetectionTimestamp ?: 0)
            Log.d(logTag, "System Time ${System.currentTimeMillis()}")
            Log.d(logTag, "Last Beacon Detection Timestamp ${(beacons.firstOrNull()?.lastCycleDetectionTimestamp ?: 0)}")
            if (rangeAgeMillis < 10000) {
                Log.d(logTag, "Ranged: ${beacons.count()} beacons")
                Log.d(logTag, "Regions count ${beaconManager.rangedRegions.size}")
                for (beacon: Beacon in beacons) {
                    Log.d(logTag, "$beacon about ${beacon.distance} meters away")
                }
            }
            else {
                Log.d(logTag, "Ignoring stale ranged beacons from $rangeAgeMillis millis ago")
            }
        }
        regionViewModel.regionState.observe(componentActivity, monitoringObserver)
        beaconManager.startMonitoring(region)
        beaconManager.startMonitoring(region)
        beaconManager.startRangingBeacons(region)
    }

    private val monitoringObserver = Observer<Int> { state ->
        if (state == MonitorNotifier.OUTSIDE) {
            Log.d(logTag, "outside beacon region: ")
        }
        else {
            Log.d(logTag, "inside beacon region: ")

        }
    }

}

