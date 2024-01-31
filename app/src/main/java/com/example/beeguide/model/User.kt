package com.example.beeguide.model
import kotlinx.serialization.Serializable


data class Login(
    val html :String
)

@Serializable
data class User(
    val firstName: String,
    val lastName: String
)