package com.example.ctrlbee.presentation.state

import com.example.ctrlbee.domain.model.auth.LoginResponse

sealed class LoginState {

    data object Loading: LoginState()

    data class Failed(
        val message: String
    ): LoginState()

    data class Success(
        val tokens: LoginResponse
    ): LoginState()

}