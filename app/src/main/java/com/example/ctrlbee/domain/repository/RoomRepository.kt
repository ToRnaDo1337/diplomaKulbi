package com.example.ctrlbee.domain.repository

import com.example.ctrlbee.domain.model.RoomAll
import com.example.ctrlbee.domain.model.RoomRequest
import com.example.ctrlbee.domain.model.RoomUno

interface RoomRepository {

    suspend fun getAllRooms(
        token: String,
    ): List<RoomAll>

    suspend fun addRoom(
        token: String,
        room: RoomRequest
    ): RoomAll

    suspend fun addPrivateRoom(
        token: String,
        password: String,
        room: RoomRequest
    ): RoomAll

    suspend fun joinRoom(
        token: String,
        roomId: String,
    )

    suspend fun joinPrivateRoom(
        token: String,
        roomId: String,
        password: String,
    )

    suspend fun getRoomById(
        token: String,
        roomId: String,
    ): RoomUno

    suspend fun getAllRoomSearch(
        token: String,
        name: String
    ): List<RoomAll>
}