package com.example.ctrlbee.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctrlbee.domain.repository.UserRepository
import com.example.ctrlbee.presentation.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepo: UserRepository
): ViewModel() {

    private val _loginStateLiveData = MutableLiveData<LoginState>()
    val loginStateLiveData: LiveData<LoginState> get() = _loginStateLiveData

    fun signIn(
        username: String,
        password: String
    ) {
        _loginStateLiveData.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val (result, errorMessage) = userRepo.signIn(username, password)
                if (result != null) {
                    _loginStateLiveData.value = LoginState.Success(result)
                } else {
                    _loginStateLiveData.value = LoginState.Failed(errorMessage ?: "Login Failed")
                }
            } catch (ex: Exception) {
                _loginStateLiveData.value = LoginState.Failed("An error occurred during login..")
            }
        }
    }

}