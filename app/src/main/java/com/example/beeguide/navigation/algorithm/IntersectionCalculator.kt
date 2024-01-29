package com.example.beeguide.navigation.algorithm

import com.example.beeguide.ui.viewmodels.MapViewModel
import org.altbeacon.beacon.RegionViewModel
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class IntersectionCalculator() {
    fun getIntersections(circle1: Circle, circle2: Circle) : Array<Point>? {

        val r: Double; val R: Double; val d: Double; val dx: Double; val dy: Double
        val cx: Double; val cy: Double; val Cx: Double; val Cy: Double
        val eps = 0.0000000000001

        if (circle1.radius < circle2.radius) {
            r  = circle1.radius.toDouble();  R = circle2.radius.toDouble()
            cx = circle1.x.toDouble(); cy = circle1.y.toDouble()
            Cx = circle2.x.toDouble(); Cy = circle2.y.toDouble()
        } else {
            r  = circle2.radius.toDouble(); R  = circle1.radius.toDouble();
            Cx = circle1.x.toDouble(); Cy = circle1.y.toDouble()
            cx = circle2.x.toDouble(); cy = circle2.y.toDouble()
        }

        // Compute the vector <dx, dy>
        dx = cx - Cx
        dy = cy - Cy

        // pythagoras
        d = sqrt( dx * dx + dy * dy )

        // There are an infinite number of solutions
        // Seems appropriate to also return null
        if (d < eps && abs(R-r) < eps) return null

        // No intersection (circles centered at the
        // same place with different size)
        else if (d < eps) return null

        val x = (dx / d) * R + Cx
        val y = (dy / d) * R + Cy
        val P = Point(x.toInt(), y.toInt())

        // Single intersection (kissing circles)
        if (abs((R+r)-d) < eps || abs(R-(r+d)) < eps) return arrayOf(P)

        // No intersection. Either the small circle contained within
        // big circle or circles are simply disjoint.
        if ( (d+r) < R || (R+r < d) ) return null

        val C = Point(Cx.toInt(), Cy.toInt())
        val angle = acossafe((r*r-d*d-R*R)/(-2.0*d*R))
        val pt1: Point = rotatePoint(C, P, +angle)
        val pt2: Point = rotatePoint(C, P, -angle)
        return arrayOf<Point>(pt1, pt2)
    }


    fun acossafe(x: Double): Double {
        if (x >= +1.0) return 0.0
        if (x <= -1.0) return Math.PI
        return acos(x)
    }

    fun rotatePoint(fixedPoint: Point, point: Point, angle: Double): Point {
        val x = point.x - fixedPoint.x
        val y = point.y - fixedPoint.y
        val xRot = x * cos(angle) + y * sin(angle)
        val yRot = y * cos(angle) - x * sin(angle)
        return Point(fixedPoint.x + xRot.toInt(),fixedPoint.y + yRot.toInt())
    }
}