package com.example.ctrlbee.domain.model.auth

data class SendEmailVerificationRequest(
    val email: String,
    val smsRequestType: String,
)
