package com.example.beeguide.navigation.permissions

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity

class PermissionChecker(private var componentActivity: ComponentActivity){
    private val bluetoothAvailable = componentActivity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
    private val bluetoothLEAvailable = componentActivity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    private val logTag: String = "Permission"

    public fun Check() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(arrayOf(
                android.Manifest.permission.BLUETOOTH_SCAN,
                android.Manifest.permission.BLUETOOTH_CONNECT,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADVERTISE
            ))
        }
        else{
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }

        Log.d("Bluetooth", "Bluetooth: $bluetoothAvailable")
        Log.d("Bluetooth", "BLE: $bluetoothLEAvailable")
    }

    private val requestPermissionLauncher =
        componentActivity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d(logTag, "Permissions granted")
            } else {
                Log.d(logTag, "Permissions rejected")
            }
        }

    private val requestMultiplePermissions =
        componentActivity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("test006", "${it.key} = ${it.value}")
            }
        }

    private var requestBluetooth = componentActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == ComponentActivity.RESULT_OK) {
            Log.d(logTag, "Bluetooth permissions granted")
        }else{
            Log.d(logTag, "Bluetooth permissions denied")
        }
    }
}