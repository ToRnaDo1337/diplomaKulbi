package com.example.ctrlbee.data.repository

import com.example.ctrlbee.data.remote.RoomApiService
import com.example.ctrlbee.domain.model.RoomAll
import com.example.ctrlbee.domain.model.RoomRequest
import com.example.ctrlbee.domain.model.RoomUno
import com.example.ctrlbee.domain.repository.RoomRepository
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val api: RoomApiService
) : RoomRepository {

    override suspend fun getAllRooms(token: String): List<RoomAll> {
        return api.getAllRooms(token)
    }

    override suspend fun addRoom(token: String, room: RoomRequest): RoomAll {
        return api.addRoom(token, room)
    }

    override suspend fun addPrivateRoom(
        token: String,
        password: String,
        room: RoomRequest
    ): RoomAll {
        return api.addPrivateRoom(token, password, room)
    }

    override suspend fun joinRoom(token: String, roomId: String) {
        api.joinRoom(token, roomId)
    }

    override suspend fun joinPrivateRoom(token: String, roomId: String, password: String) {
        api.joinPrivateRoom(token, roomId, password)
    }

    override suspend fun getRoomById(token: String, roomId: String): RoomUno {
        return api.getRoomById(token, roomId)
    }

    override suspend fun getAllRoomSearch(token: String, name: String): List<RoomAll> {
        return api.getAllRoomsSearch(token, name)
    }
}