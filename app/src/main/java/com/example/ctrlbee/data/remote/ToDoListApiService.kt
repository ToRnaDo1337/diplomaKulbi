package com.example.ctrlbee.data.remote

import com.example.ctrlbee.domain.model.ToDoListItem
import com.example.ctrlbee.domain.model.ToDoRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ToDoListApiService {

    @GET("/api/calendars")
    suspend fun getToDoList(
        @Header("Authorization") token: String,
        @Query("time") time: String,
    ): List<ToDoListItem>

    @POST("/api/calendars")
    suspend fun addToDo(
        @Header("Authorization") token: String,
        @Body request: ToDoRequest
    ): String
}