package com.example.ctrlbee.data.repository

import android.util.Log
import com.example.ctrlbee.data.remote.AuthApiService
import com.example.ctrlbee.domain.model.auth.LoginRequest
import com.example.ctrlbee.domain.model.auth.LoginResponse
import com.example.ctrlbee.domain.model.auth.RegistrationRequest
import com.example.ctrlbee.domain.model.auth.SendEmailVerificationRequest
import com.example.ctrlbee.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val authService: AuthApiService,
) : UserRepository {
    override suspend fun sendEmailVerification(
        email: String,
        smsRequestType: String,
    ): Pair<String?, String?> {
        return try {
            val response = authService.sendEmailVerification(SendEmailVerificationRequest(email, smsRequestType))
            Pair(response, null)
        } catch (ex: Exception) {
            Pair(null, ex.message)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        verificationCode: String,
    ): Pair<String?, String?> {
        return try {
            val response = authService.signUp(RegistrationRequest(email, password, verificationCode))
            Log.d("msg", response)
            Pair(response, null)
        } catch (ex: Exception) {
            Pair(null, ex.message)
        }
    }

    override suspend fun signIn(
        email: String,
        password: String,
    ): Pair<LoginResponse?, String?> {
        return try {
            val response = authService.signIn(LoginRequest(email, password))
            Pair(response, null)
        } catch (ex: Exception) {
            Pair(null, ex.message)
        }
    }

    override suspend fun changePassword(
        email: String,
        password: String,
        verificationCode: String
    ): Pair<String?, String?> {
        return try {
            val response = authService.changePassword(RegistrationRequest(email, password, verificationCode))
            Log.d("msg", response)
            Pair(response, null)
        } catch (ex: Exception) {
            Pair(null, ex.message)
        }
    }
}