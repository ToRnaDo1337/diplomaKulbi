package com.example.ctrlbee.presentation.state

import com.example.ctrlbee.domain.model.ToDoListItem


sealed class ToDoListState {
    data object Loading: ToDoListState()
    data class Failed(
        val message: String
    ): ToDoListState()
    data class Success(
        val result: List<ToDoListItem>
    ): ToDoListState()
}