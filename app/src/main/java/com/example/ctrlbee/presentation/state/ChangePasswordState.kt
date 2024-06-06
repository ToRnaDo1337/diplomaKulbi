package com.example.ctrlbee.presentation.state

sealed class ChangePasswordState {

    data object Loading: ChangePasswordState()

    data class Failed(
        val message: String
    ): ChangePasswordState()

    data class Success(
        val message: String
    ): ChangePasswordState()

}