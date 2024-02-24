package com.example.beeguide.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDetails(
    val bio: String?,
    @SerialName("profile_picture")
    val profilePicture: String?,
    @SerialName("profile_banner")
    val profileBanner: String?,
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

@Serializable
data class NameRequest(
    val name: String,
)

@Serializable
data class BioRequest(
    val bio: String,
)