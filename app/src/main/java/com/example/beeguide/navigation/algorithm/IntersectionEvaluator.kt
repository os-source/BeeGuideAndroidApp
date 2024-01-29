package com.example.beeguide.navigation.algorithm

import android.util.Log

class IntersectionEvaluator(private var intersections: MutableList<Point>) {
    var insaneClusterRoot = Point(0, 0)
    private val distanceCalculator = DistanceCalculator()

    fun findInsaneClusterRoot(){
        var lastDistance: Int = 0
        var estimatedRoot = Point(0, 0)
        intersections.forEach{calculatingPoint ->
            var currentDistance: Int = 0
            intersections.forEach{siblingPoint ->
                if(calculatingPoint != siblingPoint){
                    currentDistance += distanceCalculator.getDistance(calculatingPoint, siblingPoint)
                }
            }
            if(currentDistance < lastDistance || lastDistance == 0){
                Log.d("beeguide", "Changed estimated root")
                lastDistance = currentDistance
                estimatedRoot = calculatingPoint
            }
        }
        insaneClusterRoot = estimatedRoot
    }
}