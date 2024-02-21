package com.example.beeguide.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val token: String,
)

@Serializable
data class AuthRequest(
    val email: String,
    val password: String,
)
