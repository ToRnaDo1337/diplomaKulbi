package com.example.ctrlbee.domain.repository

import com.example.ctrlbee.domain.model.auth.LoginResponse

interface UserRepository {

    suspend fun sendEmailVerification(
        email: String,
        smsRequestType: String,
    ): Pair<String?, String?>

    suspend fun signUp(
        email: String,
        password: String,
        verificationCode: String,
    ): Pair<String?, String?>

    suspend fun signIn(
        email: String,
        password: String,
    ): Pair<LoginResponse?, String?>

    suspend fun changePassword(
        email: String,
        password: String,
        verificationCode: String,
    ): Pair<String?, String?>
}