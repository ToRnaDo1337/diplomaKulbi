package com.example.ctrlbee.domain.repository

import com.example.ctrlbee.domain.model.ToDoListItem

interface ToDoListRepository {
    suspend fun getToDoList(
        token: String,
        time: String
    ): Pair<List<ToDoListItem>?, String?>

    suspend fun addToDo(
        token: String,
        message: String,
        time: String
    ): Pair<String?, String?>
}