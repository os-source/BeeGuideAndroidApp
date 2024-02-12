package com.example.beeguide.model
import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val JWT :String,
    val refresh :String
)
@Serializable
data class LoginRequest(

    val firstName: String,
    val lastName: String)

@Serializable
data class User(
    val firstName: String,
    val lastName: String
)