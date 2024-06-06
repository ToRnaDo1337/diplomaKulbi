package com.example.ctrlbee.presentation.state

sealed class VerificationState {

    data object Loading: VerificationState()

    data class Failed(
        val message: String
    ): VerificationState()

    data object Success: VerificationState()
}