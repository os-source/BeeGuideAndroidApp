package com.example.beeguide.navigation.beacons

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.Region
import androidx.lifecycle.Observer
import org.altbeacon.beacon.RegionViewModel

class Monitor(private var ctx: Context) {
    companion object{
        private val DEFAULT_REGION = Region("all-beacons", null, null, null)
    }
    private val logTag: String = "beeguide-beacon-monitor"

    // The example shows how to find iBeacon.
    private val iBeaconParser = BeaconParser().
    setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
        .apply {
            setHardwareAssistManufacturerCodes(arrayOf(0x004c).toIntArray())
        }

    private val beaconManager =  BeaconManager.getInstanceForApplication(ctx).apply {
        beaconParsers.add(iBeaconParser)
    }



    fun getRegionViewModel(): RegionViewModel =
        BeaconManager.getInstanceForApplication(ctx).getRegionViewModel(DEFAULT_REGION)

    fun startLogging(owner: LifecycleOwner){

        val regionViewModel = getRegionViewModel()

        regionViewModel.rangedBeacons.observe(owner, rangingObserver)
        regionViewModel.regionState.observe(owner, monitoringObserver)

        beaconManager.startMonitoring(DEFAULT_REGION)
        beaconManager.startRangingBeacons(DEFAULT_REGION)
    }

    private val rangingObserver = Observer<Collection<Beacon>> { beacons->
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

    private val monitoringObserver = Observer<Int> { state ->
        if (state == MonitorNotifier.OUTSIDE) {
            Log.d(logTag, "outside beacon region: ")
        }
        else {
            Log.d(logTag, "inside beacon region: ")

        }
    }

}

