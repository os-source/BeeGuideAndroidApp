package com.example.beeguide.model
import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val JWT: String,
    val refresh: String?
)
@Serializable
data class LoginRequest(
    val firstName: String,
    val lastName: String
)

@Serializable
data class UserDetails(
    val bioHeading: String,
    val bio: String
)

@Serializable
data class User(
    val email: String,
    val name: String,
    val userDetails: UserDetails?,
    val enabled: Boolean,
    val id: Int,
    val comments: List<String>
)