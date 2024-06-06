package com.example.ctrlbee.presentation.state

import com.example.ctrlbee.domain.model.RoomAll

sealed class RoomState {
    data object Loading: RoomState()
    data class Failed(
        val message: String
    ): RoomState()
    data class Success(
        val result: List<RoomAll>
    ): RoomState()
}