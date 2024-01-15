package com.example.beeguide.navigation

import android.app.Activity
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.example.beeguide.R
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import androidx.lifecycle.Observer
import org.altbeacon.beacon.AltBeaconParser.TAG
import org.altbeacon.beacon.MonitorNotifier

class BeaconMonitoringService : Service() {
    override fun onCreate() {
//        Log.d("navDebug", "Monitoring started")
//
//        // TODO: Add beaconParsers for any properietry beacon formats you wish to detect
//
//        val beaconManager =  BeaconManager.getInstanceForApplication(this)
//        val region = Region("all-beacons-region", null, null, null)
//        // Set up a Live Data observer so this Activity can get monitoring callbacks
//        // observer will be called each time the monitored regionState changes (inside vs. outside region)
//        beaconManager.getRegionViewModel(region).regionState.observe(monitoringObserver)
//        beaconManager.startMonitoring(region)
    }

    val monitoringObserver = Observer<Int> { state ->
        if (state == MonitorNotifier.INSIDE) {
            Log.d(TAG, "Detected beacons(s)")
        }
        else {
            Log.d(TAG, "Stopped detecteing beacons")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
}