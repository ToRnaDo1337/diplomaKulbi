package com.example.ctrlbee.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctrlbee.domain.model.RoomRequest
import com.example.ctrlbee.domain.repository.RoomRepository
import com.example.ctrlbee.presentation.state.RoomAddState
import com.example.ctrlbee.presentation.state.RoomJoinState
import com.example.ctrlbee.presentation.state.RoomState
import com.example.ctrlbee.presentation.state.RoomUnoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepo: RoomRepository
) : ViewModel() {

    private val _roomStateLiveData = MutableLiveData<RoomState>()
    val roomState: LiveData<RoomState> get() = _roomStateLiveData

    fun fetchAllRooms(
        token: String,
        roomName: String,
    ) {
        _roomStateLiveData.value = RoomState.Loading

        viewModelScope.launch {
            try {
                val result = roomRepo.getAllRooms("Bearer $token")
                val filteredRooms = if(roomName.isNotEmpty())
                    result.filter { room -> room.name.startsWith(roomName, ignoreCase = true) }
                else result
                _roomStateLiveData.value = RoomState.Success(filteredRooms)
            } catch (ex: Exception) {
                _roomStateLiveData.value =
                    RoomState.Failed("An error occurred during fetching rooms..")
            }
        }
    }

    private val _roomAddStateLiveData = MutableLiveData<RoomAddState>()
    val roomAddState: LiveData<RoomAddState> get() = _roomAddStateLiveData

    fun saveRoom(
        token: String,
        room: RoomRequest
    ) {
        _roomAddStateLiveData.value = RoomAddState.Loading

        viewModelScope.launch {
            try {
                val result = roomRepo.addRoom("Bearer $token", room)
                _roomAddStateLiveData.value = RoomAddState.Success(result)
            } catch (ex: Exception) {
                _roomAddStateLiveData.value =
                    RoomAddState.Failed("An error occurred during fetching rooms..")
            }
        }
    }

    fun savePrivateRoom(
        token: String,
        password: String,
        room: RoomRequest
    ) {
        _roomAddStateLiveData.value = RoomAddState.Loading

        viewModelScope.launch {
            try {
                val result = roomRepo.addPrivateRoom("Bearer $token", password, room)
                _roomAddStateLiveData.value = RoomAddState.Success(result)
            } catch (ex: Exception) {
                _roomAddStateLiveData.value =
                    RoomAddState.Failed("An error occurred during fetching rooms..")
            }
        }
    }

    // join room
    private val _roomJoinStateLiveData = MutableLiveData<RoomJoinState>()
    val roomJoinState: LiveData<RoomJoinState> get() = _roomJoinStateLiveData

    fun joinRoom(
        token: String,
        roomId: String
    ) {
        _roomJoinStateLiveData.value = RoomJoinState.Loading

        viewModelScope.launch {
            try {
                roomRepo.joinRoom("Bearer $token", roomId)
                _roomJoinStateLiveData.value = RoomJoinState.Success
            } catch (ex: Exception) {
                _roomJoinStateLiveData.value =
                    RoomJoinState.Failed("An error occurred during fetching rooms..")
            }
        }
    }

    fun joinPrivateRoom(
        token: String,
        roomId: String,
        password: String
    ) {
        _roomJoinStateLiveData.value = RoomJoinState.Loading

        viewModelScope.launch {
            try {
               roomRepo.joinPrivateRoom("Bearer $token", roomId, password)
                _roomJoinStateLiveData.value = RoomJoinState.Success
            } catch (ex: Exception) {
                _roomJoinStateLiveData.value =
                    RoomJoinState.Failed("An error occurred during fetching rooms..")
            }
        }
    }

    private val _roomUnoStateLiveData = MutableLiveData<RoomUnoState>()
    val roomUnoState: LiveData<RoomUnoState> get() = _roomUnoStateLiveData

    fun fetchRoomById(
        token: String,
        roomId: String,
    ) {
        _roomUnoStateLiveData.value = RoomUnoState.Loading

        viewModelScope.launch {
            try {
                val result = roomRepo.getRoomById("Bearer $token", roomId)

                _roomUnoStateLiveData.value = RoomUnoState.Success(result)
            } catch (ex: Exception) {
                _roomUnoStateLiveData.value =
                    RoomUnoState.Failed("An error occurred during fetching rooms..")
            }
        }
    }

    private val _roomSearchStateLiveData = MutableLiveData<RoomState>()
    val roomSearchState: LiveData<RoomState> get() = _roomSearchStateLiveData

    fun fetchAllRoomsSearch(
        token: String,
        roomName: String,
    ) {
        _roomSearchStateLiveData.value = RoomState.Loading

        viewModelScope.launch {
            try {
                val result = roomRepo.getAllRoomSearch("Bearer $token", roomName)
                _roomSearchStateLiveData.value = RoomState.Success(result)
            } catch (ex: Exception) {
                _roomSearchStateLiveData.value =
                    RoomState.Failed("An error occurred during fetching rooms..")
            }
        }
    }


}