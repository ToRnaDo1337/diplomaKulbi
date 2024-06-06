package com.example.ctrlbee.data.remote

import com.example.ctrlbee.domain.model.RoomAll
import com.example.ctrlbee.domain.model.RoomRequest
import com.example.ctrlbee.domain.model.RoomUno
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RoomApiService {

    @GET("/api/rooms")
    suspend fun getAllRooms(
        @Header("Authorization") token: String,
        @Query("filter") filter: String = "all"
    ): List<RoomAll>

    @POST("/api/rooms")
    suspend fun addRoom(
        @Header("Authorization") token: String,
        @Body room: RoomRequest,
    ): RoomAll

    @POST("/api/rooms")
    suspend fun addPrivateRoom(
        @Header("Authorization") token: String,
        @Query("password") password: String,
        @Body room: RoomRequest,
    ): RoomAll

    @POST("/api/rooms/join/{id}")
    suspend fun joinRoom(
        @Header("Authorization") token: String,
        @Path("id") roomId: String,
    )

    @POST("/api/rooms/join/{id}")
    suspend fun joinPrivateRoom(
        @Header("Authorization") token: String,
        @Path("id") roomId: String,
        @Query("password") password: String
    )

    @GET("/api/rooms/{id}")
    suspend fun getRoomById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): RoomUno

    @GET("/api/rooms/search")
    suspend fun getAllRoomsSearch(
        @Header("Authorization") token: String,
        @Query("q") name: String
    ): List<RoomAll>
}