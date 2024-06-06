//package com.example.ctrlbee.presentation.viewmodel
//
//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.ctrlbee.domain.repository.ToDoListRepository
//import com.example.ctrlbee.presentation.state.AddToDoState
//import com.example.ctrlbee.presentation.state.ToDoListState
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class ToDoListViewModel @Inject constructor(
//    private val toDoListRepo: ToDoListRepository
//): ViewModel() {
//
//    private val _toDoListStateLiveData = MutableLiveData<ToDoListState>()
//    val toDoListState: LiveData<ToDoListState> get() = _toDoListStateLiveData
//
//    private val _addToDoStateLiveData = MutableLiveData<AddToDoState>()
//    val addToDoStateLiveData: LiveData<AddToDoState> get() = _addToDoStateLiveData
//
//    fun getToDoList(
//        token: String,
//        time: String
//    ) {
//        _toDoListStateLiveData.value = ToDoListState.Loading
//
//        viewModelScope.launch {
//            try {
//                val (result, errorMessage) = toDoListRepo.getToDoList(token, time)
//                if (result != null) {
//                    _toDoListStateLiveData.value = ToDoListState.Success(result)
//                } else {
//                    Log.d("check", errorMessage.toString())
//                    _toDoListStateLiveData.value = ToDoListState.Failed(errorMessage ?: "Fetching data failed")
//                }
//            } catch (ex: Exception) {
//                _toDoListStateLiveData.value = ToDoListState.Failed("An error occurred during fetching to-do-list..")
//            }
//        }
//    }
//
//    fun addToDo(
//        token: String,
//        message: String,
//        time: String
//    ) {
//        _addToDoStateLiveData.value = AddToDoState.Loading
//
//        viewModelScope.launch {
//            try {
//                val (result, errorMessage) = toDoListRepo.addToDo(token, message, time)
//                if (result != null) {
//                    _addToDoStateLiveData.value = AddToDoState.Success(result)
//                } else {
//                    _addToDoStateLiveData.value = AddToDoState.Failed(errorMessage ?: "Adding note failed")
//                }
//            } catch (ex: Exception) {
//                _addToDoStateLiveData.value = AddToDoState.Failed("An error occurred during adding note..")
//            }
//        }
//    }
//}
package com.example.ctrlbee.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctrlbee.domain.repository.ToDoListRepository
import com.example.ctrlbee.presentation.state.AddToDoState
import com.example.ctrlbee.presentation.state.ToDoListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    private val toDoListRepo: ToDoListRepository
): ViewModel() {

    private val _toDoListStateLiveData = MutableLiveData<ToDoListState>()
    val toDoListState: LiveData<ToDoListState> get() = _toDoListStateLiveData

    private val _addToDoStateLiveData = MutableLiveData<AddToDoState>()
    val addToDoStateLiveData: LiveData<AddToDoState> get() = _addToDoStateLiveData

    fun getToDoList(
        token: String,
        time: String
    ) {
        _toDoListStateLiveData.value = ToDoListState.Loading

        viewModelScope.launch {
            try {
                val (result, errorMessage) = toDoListRepo.getToDoList(token, time)
                if (result != null) {
                    _toDoListStateLiveData.value = ToDoListState.Success(result)
                } else {
                    Log.d("check", errorMessage.toString())
                    _toDoListStateLiveData.value = ToDoListState.Failed(errorMessage ?: "Fetching data failed")
                }
            } catch (ex: Exception) {
                _toDoListStateLiveData.value = ToDoListState.Failed("An error occurred during fetching to-do-list..")
            }
        }
    }

    fun addToDo(
        token: String,
        message: String,
        time: String
    ) {
        _addToDoStateLiveData.value = AddToDoState.Loading

        viewModelScope.launch {
            try {
                val (result, errorMessage) = toDoListRepo.addToDo(token, message, time)
                //if (result != null) {
                _addToDoStateLiveData.value = AddToDoState.Success
                /*} else {
                    _addToDoStateLiveData.value = AddToDoState.Failed(errorMessage ?: "Adding note failed")
                }*/
            } catch (ex: Exception) {
                _addToDoStateLiveData.value = AddToDoState.Failed("An error occurred during adding note..")
            }
        }
    }

    fun refreshAddToDoState(){
        _addToDoStateLiveData.value = AddToDoState.Initial
    }
}