package com.example.ctrlbee.presentation.state

import com.example.ctrlbee.domain.model.RoomAll

sealed class RoomAddState {
    data object Loading: RoomAddState()
    data class Failed(
        val message: String
    ): RoomAddState()
    data class Success(
        val result: RoomAll
    ): RoomAddState()
}