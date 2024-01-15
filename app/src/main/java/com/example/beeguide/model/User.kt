package com.example.beeguide.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val firstName: String,
    val lastName: String
)