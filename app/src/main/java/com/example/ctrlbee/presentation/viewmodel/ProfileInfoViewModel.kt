package com.example.ctrlbee.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctrlbee.domain.repository.ProfileRepository
import com.example.ctrlbee.presentation.state.UpdateProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ProfileInfoViewModel
    @Inject
    constructor(
        private val profileRepo: ProfileRepository,
    ) : ViewModel() {
        private val _profileStateLiveData = MutableLiveData<UpdateProfileState>()
        val profileStateLiveData: LiveData<UpdateProfileState> get() = _profileStateLiveData

        fun updateProfile(
            token: String,
            username: RequestBody,
//            dateOfBirthday: MultipartBody.Part,
            profileImage: MultipartBody.Part,
        ) {
            _profileStateLiveData.value = UpdateProfileState.Loading

            viewModelScope.launch {
                try {
                    val (result, errorMessage) = profileRepo.updateProfile(token, username, profileImage)
                    if (result != null) {
                        _profileStateLiveData.value = UpdateProfileState.Success(result)
                    } else {
                        _profileStateLiveData.value = UpdateProfileState.Failed(errorMessage ?: "Updating profile failed")
                    }
                } catch (ex: Exception) {
                    _profileStateLiveData.value = UpdateProfileState.Failed("An error occurred during updating profile..")
                }
            }
        }
    }
