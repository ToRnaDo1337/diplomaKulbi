package com.example.ctrlbee.domain.model

data class RoomUno(
    val id: String,
    val name: String,
    val ownerUserName: String,
    val members: List<RoomMember>
)

data class RoomMember(
    val id: String,
    val username: String?,
    val profileImage: String?
)
