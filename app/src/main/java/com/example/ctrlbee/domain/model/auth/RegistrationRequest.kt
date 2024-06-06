package com.example.ctrlbee.domain.model.auth

data class RegistrationRequest(
    val email: String,
    val password: String,
    val verificationCode: String,
)
