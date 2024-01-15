package com.example.beeguide.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Map(
    val major: Int,
    val svg: String,
    val beacons: List<Beacon>,
    val pois: List<POI>,
    val name: String,
    @SerialName("y-axis")
    val yAxis: Int,
    @SerialName("x-axis")
    val xAxis: Int,
)

@Serializable
data class Beacon(
    val x: Int,
    val y: Int,
    val minor: Int
)

@Serializable
data class POI(
    val description: String,
    val type: String,
)

@Serializable
data class MapMarker(
    val x: Double,
    val y: Double,
    val id: Int,
    val name: String,
    val description: String
)

@Serializable
data class Building(
    val city: String,
    @SerialName("postal_code")
    val postalCode: Int,
    @SerialName("address_line")
    val addressLine: String,
)

@Serializable
data class UserPosition(
    val x: Double,
    val y: Double,
)