package com.example.ctrlbee.presentation.state

sealed class UpdateProfileState {
    data object Loading : UpdateProfileState()

    data class Failed(
        val message: String,
    ) : UpdateProfileState()

    data class Success(
        val message: String,
    ) : UpdateProfileState()
}
