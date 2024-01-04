package com.example.beeguide.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Data class that defines an Mars Photo
@Serializable
data class MarsPhoto(
    val id: String,
    @SerialName(value = "img_src")
    val imgSrc: String
)