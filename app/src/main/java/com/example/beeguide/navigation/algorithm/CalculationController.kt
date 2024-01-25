package com.example.beeguide.navigation.algorithm

import android.util.Log
import com.example.beeguide.ui.viewmodels.MapViewModel
import org.altbeacon.beacon.RegionViewModel

class CalculationController(private val circles: List<Circle>) {
    fun log(){
        circles.forEach{
            Log.d("Circle", "\nCircle:\nX: ${it.x}\nY: ${it.y}\nRadius: ${it.radius}\n")
        }
    }
}