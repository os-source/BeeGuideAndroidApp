package com.example.beeguide.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(
    val JWT: String,
    val refresh: String?
)

@Serializable
data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
)

@Serializable
data class SignInRequest(
    val email: String,
    val password: String,
)