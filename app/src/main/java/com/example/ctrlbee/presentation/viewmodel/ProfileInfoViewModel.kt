package com.example.ctrlbee.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.domain.model.posts.PostResponse
import com.example.ctrlbee.domain.model.profile.ProfileResponse
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
    @Inject
    lateinit var sharedPreferencesRepo: SharedPreferencesRepo


    private val _profileStateLiveData = MutableLiveData<UpdateProfileState>()
    val profileStateLiveData: LiveData<UpdateProfileState> get() = _profileStateLiveData


    private val _profileDataLiveData = MutableLiveData<ProfileResponse?>()
    val profileDataLiveData: LiveData<ProfileResponse?> get() = _profileDataLiveData

    private val _postsLiveData = MutableLiveData<List<PostResponse>>()
    val postsLiveData: LiveData<List<PostResponse>> get() = _postsLiveData


    fun updateProfile(
        token: String,
        username: RequestBody,
//            dateOfBirthday: MultipartBody.Part,
        profileImage: MultipartBody.Part,
    ) {
        _profileStateLiveData.value = UpdateProfileState.Loading

        viewModelScope.launch {
            try {
                val (result, errorMessage) = profileRepo.updateProfile(
                    token,
                    username,
                    profileImage
                )
                if (result != null) {
                    _profileStateLiveData.value = UpdateProfileState.Success(result)
                } else {
                    _profileStateLiveData.value =
                        UpdateProfileState.Failed(errorMessage ?: "Updating profile failed")
                }
            } catch (ex: Exception) {
                _profileStateLiveData.value =
                    UpdateProfileState.Failed("An error occurred during updating profile..")
            }
        }
    }

    fun updateStatus(token: String, status: RequestBody) {
        _profileStateLiveData.value = UpdateProfileState.Loading

        viewModelScope.launch {
            try {
                val (result, errorMessage) = profileRepo.updateStatus(token, status)
                Log.d("Response profile","RESP"+ result+errorMessage)
                if (result != null) {
                    _profileStateLiveData.value = UpdateProfileState.Success(result)
                } else {
                    _profileStateLiveData.value =
                        UpdateProfileState.Failed(errorMessage ?: "Updating status failed")
                }
            } catch (ex: Exception) {
                _profileStateLiveData.value =
                    UpdateProfileState.Failed("An error occurred during updating status.")
            }
        }
    }

    fun fetchProfile(token: String) {
        viewModelScope.launch {
            try {
                val (profile, errorMessage) = profileRepo.getProfile(token)
//                Log.d("Profile view model outside if", "VALUE:"+profile)
                if (profile != null) {
                    _profileDataLiveData.value = profile
                    sharedPreferencesRepo.setUsername(profile.username)
                    sharedPreferencesRepo.setUserBio(profile.status)
                    sharedPreferencesRepo.setUserImage(profile.profileImage)
                } else {
                    _profileDataLiveData.value = null // or handle error appropriately
                }
            } catch (ex: Exception) {
                Log.e("ProfileInfoViewModel", "Error fetching profile: ${ex.message}")
                _profileDataLiveData.value = null // or handle error appropriately
            }
        }
    }
    fun fetchPosts(token: String) {
        viewModelScope.launch {
            try {
                val (posts, errorMessage) = profileRepo.getPosts(token)
                if (posts != null) {
                    _postsLiveData.value = posts
                } else {
                    // Handle error appropriately
                }
            } catch (ex: Exception) {
                // Handle error appropriately
            }
        }
    }

}
