package com.example.beeguide.model
import kotlinx.serialization.Serializable

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