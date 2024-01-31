package com.example.beeguide.navigation.algorithm

class LocationDeterminator(private val intersections: MutableList<Point>) {
    var location: Point = Point(0, 0)

    fun calcLocation(){
        var xSum = 0
        var ySum = 0
        intersections.forEach{intersection ->
            xSum += intersection.x
            ySum += intersection.y
        }
        val x = xSum / intersections.count()
        val y = ySum / intersections.count()

        location = Point(x, y)
    }
}