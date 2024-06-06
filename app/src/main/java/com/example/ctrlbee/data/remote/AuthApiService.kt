package com.example.ctrlbee.data.remote

import com.example.ctrlbee.domain.model.auth.LoginRequest
import com.example.ctrlbee.domain.model.auth.LoginResponse
import com.example.ctrlbee.domain.model.auth.RegistrationRequest
import com.example.ctrlbee.domain.model.auth.SendEmailVerificationRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/emails")
    suspend fun sendEmailVerification(
        @Body request: SendEmailVerificationRequest,
    ): String

    @POST("/api/auth/signup")
    suspend fun signUp(
        @Body request: RegistrationRequest,
    ): String

    @POST("/api/auth/signin")
    suspend fun signIn(
        @Body request: LoginRequest,
    ): LoginResponse

    @POST("/api/auth/password")
    suspend fun changePassword(
        @Body request: RegistrationRequest,
    ): String
}