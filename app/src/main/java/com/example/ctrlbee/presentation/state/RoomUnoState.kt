package com.example.ctrlbee.presentation.state

import com.example.ctrlbee.domain.model.RoomAll
import com.example.ctrlbee.domain.model.RoomUno

sealed class RoomUnoState {
    data object Loading: RoomUnoState()
    data class Failed(
        val message: String
    ): RoomUnoState()
    data class Success(
        val result: RoomUno
    ): RoomUnoState()
}