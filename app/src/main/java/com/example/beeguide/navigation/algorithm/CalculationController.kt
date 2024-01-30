package com.example.beeguide.navigation.algorithm

import android.util.Log

class CalculationController(private val circles: List<Circle>) {
    private var circleDuos = mutableListOf<kotlin.Array<Circle>>()
    var intersections = mutableListOf<Point>()

    fun control(): Point {
        generateCircleDuos()
        val intersectionCalculator = IntersectionCalculator()

        circleDuos.forEach { duo ->
            intersectionCalculator.getIntersections(duo[0], duo[1])
                ?.forEach { intersection -> intersections.add(intersection) }
        }

        if(intersections.count() > 6){
            val intersectionEvaluator = IntersectionEvaluator(intersections)
            intersectionEvaluator.findInsaneClusterRoot()
            return intersectionEvaluator.insaneClusterRoot
        }
        else{
            return Point(0, 0)
        }
    }

    fun logPoints(){
        intersections.forEach{point -> Log.d("Point", "Point-Coordinates: X: ${point.x} Y: ${point.y}")}
    }

    private fun generateCircleDuos(){
        circles.forEach{ circle1 ->
            circles.forEach{circle2 ->
                if(circle1 != circle2){
                    val circleArr: kotlin.Array<Circle>
                    if(circle1.radius < circle2.radius){
                        circleArr = arrayOf(circle1, circle2)
                    }
                    else
                    {
                        circleArr = arrayOf(circle2, circle1)
                    }
                    circleDuos.add(circleArr)
                }
            }
        }
        circleDuos = circleDuos.distinct().toMutableList()
    }
}