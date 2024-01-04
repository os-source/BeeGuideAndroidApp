package com.example.beeguide.model

import kotlinx.serialization.Serializable

//TODO: Map model
@Serializable
data class Map(
    val map: String
)

@Serializable
data class MapMarker(
    val x: Double,
    val y: Double,
    val id: Int,
    val name: String,
    val description: String
)