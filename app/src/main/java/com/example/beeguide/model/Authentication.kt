package com.example.beeguide.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val JWT: String,
    val refresh: String?
)

@Serializable
data class AuthRequest(
    val email: String,
    val password: String,
)