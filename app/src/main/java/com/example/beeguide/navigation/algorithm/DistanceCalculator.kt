package com.example.beeguide.navigation.algorithm

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class DistanceCalculator {
    fun getDistance(point1: Point, point2: Point): Int {
        val a: Int = abs(point1.y - point2.y)
        val b: Int = abs(point1.x - point2.x)

        return sqrt(a.toFloat().pow(2) + b.toFloat().pow(2)).toInt()
    }
}