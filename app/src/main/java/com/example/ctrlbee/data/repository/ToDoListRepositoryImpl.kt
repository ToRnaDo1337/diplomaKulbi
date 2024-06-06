package com.example.ctrlbee.data.repository

import com.example.ctrlbee.data.remote.ToDoListApiService
import com.example.ctrlbee.domain.model.ToDoListItem
import com.example.ctrlbee.domain.model.ToDoRequest
import com.example.ctrlbee.domain.repository.ToDoListRepository
import javax.inject.Inject

class ToDoListRepositoryImpl @Inject constructor(
    private val toDoListService: ToDoListApiService
): ToDoListRepository {
    override suspend fun getToDoList(
        token: String,
        time: String
    ): Pair<List<ToDoListItem>?, String?> {
        return try {
            val response = toDoListService
                .getToDoList(
                    token = "Bearer $token",
                    time = time
                )
            Pair(response, null)
        } catch (ex: Exception) {
            Pair(null, ex.message)
        }
    }

    override suspend fun addToDo(
        token: String,
        message: String,
        time: String
    ): Pair<String?, String?> {
        return try {
            val response = toDoListService
                .addToDo(
                    token = "Bearer $token",
                    request = ToDoRequest(message, time)
                )
            Pair(response, null)
        } catch (ex: Exception) {
            Pair(null, ex.message)
        }
    }
}