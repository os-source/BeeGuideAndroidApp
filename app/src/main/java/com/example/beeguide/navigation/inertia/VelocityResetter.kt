package com.example.beeguide.navigation.inertia

import kotlin.math.absoluteValue

class VelocityResetter {
    private var resetCheckCounter: Int = 0
    fun checkReset(attractionForceXYZ: FloatArray): Boolean{
        if(attractionForceXYZ[2] > 9.4 && attractionForceXYZ[2] < 9.9){
            if(attractionForceXYZ[0].absoluteValue < 0.4 || attractionForceXYZ[1].absoluteValue < 0.4){
                resetCheckCounter++
            }
        }

        if(resetCheckCounter > 3){
            resetCheckCounter = 0
            return true
        }

        return false
    }

    fun checkIfMoving(attractionForceXYZ: FloatArray): Boolean {
        if(attractionForceXYZ[2] > 9.6 && attractionForceXYZ[2] < 9.7){
            if(attractionForceXYZ[0].absoluteValue < 0.15 || attractionForceXYZ[1].absoluteValue < 0.15){
                return false
            }
        }
        return true
    }
}