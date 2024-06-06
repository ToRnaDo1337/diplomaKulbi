package com.example.ctrlbee.presentation.state

import com.example.ctrlbee.domain.model.RoomAll

sealed class RoomJoinState {
    data object Loading: RoomJoinState()
    data class Failed(
        val message: String
    ): RoomJoinState()
    data object Success: RoomJoinState()
}