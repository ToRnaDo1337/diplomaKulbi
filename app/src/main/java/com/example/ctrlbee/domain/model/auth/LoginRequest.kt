package com.example.ctrlbee.domain.model.auth

data class LoginRequest(
    val email: String,
    val password: String,
)
