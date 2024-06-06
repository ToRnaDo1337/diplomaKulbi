package com.example.ctrlbee.domain.model

data class RoomAll(
    val id: String,
    val name: String,
    val isPrivate: Boolean,
    val membersCount: Int
)
