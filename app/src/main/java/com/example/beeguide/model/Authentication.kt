package com.example.beeguide.model

data class TokenResponse(
    val token: String,
)

data class AuthRequest(
    val email: String,
    val password: String,
)
