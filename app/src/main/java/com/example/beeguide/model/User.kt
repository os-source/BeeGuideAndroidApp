package com.example.beeguide.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val firstName: String,
    val lastName: String
)

@Serializable
data class Test(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)