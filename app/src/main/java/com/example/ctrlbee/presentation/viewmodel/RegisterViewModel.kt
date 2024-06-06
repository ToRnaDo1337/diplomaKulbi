package com.example.ctrlbee.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctrlbee.domain.repository.UserRepository
import com.example.ctrlbee.presentation.state.ChangePasswordState
import com.example.ctrlbee.presentation.state.RegistrationState
import com.example.ctrlbee.presentation.state.VerificationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject
constructor(
    private val userRepo: UserRepository,
) : ViewModel() {
    private val _registrationStateLiveData = MutableLiveData<RegistrationState>()
    val registrationStateLiveData: LiveData<RegistrationState> get() = _registrationStateLiveData

    private val _verificationStateLiveData = MutableLiveData<VerificationState>()
    val verificationStateLiveData: LiveData<VerificationState> get() = _verificationStateLiveData

    fun signUp(
        email: String,
        password: String,
        verificationCode: String,
    ) {
        _registrationStateLiveData.value = RegistrationState.Loading

        viewModelScope.launch {
            try {
                val (result, errorMessage) = userRepo.signUp(email, password, verificationCode)
                if (result != null) {
                    _registrationStateLiveData.value = RegistrationState.Success(result)
                } else {
                    _registrationStateLiveData.value =
                        RegistrationState.Failed(errorMessage ?: "Registration Failed")
                }
            } catch (ex: Exception) {
                _registrationStateLiveData.value =
                    RegistrationState.Failed("An error occurred during registration..")
            }
        }
    }

    fun sendEmailVerification(
        email: String,
        smsRequestType: String,
    ) {
        _verificationStateLiveData.value = VerificationState.Loading

        viewModelScope.launch {
            try {
                val (result, errorMessage) = userRepo.sendEmailVerification(email, smsRequestType)
                if (result != null) {
                    _verificationStateLiveData.value = VerificationState.Success
                } else {
                    _verificationStateLiveData.value =
                        VerificationState.Failed(errorMessage ?: "Verification failed")
                }
            } catch (ex: Exception) {
                _verificationStateLiveData.value =
                    VerificationState.Failed("An error occurred during sending verification mail..")
            }
        }
    }

    private val _changePasswordStateLiveData = MutableLiveData<ChangePasswordState>()
    val changePasswordStateLiveData: LiveData<ChangePasswordState> get() = _changePasswordStateLiveData

    fun changePassword(
        email: String,
        password: String,
        verificationCode: String,
    ) {
        _changePasswordStateLiveData.value = ChangePasswordState.Loading

        viewModelScope.launch {
            try {
                val (result, errorMessage) = userRepo.changePassword(email, password, verificationCode)
                if (result != null) {
                    _changePasswordStateLiveData.value = ChangePasswordState.Success(result)
                } else {
                    _changePasswordStateLiveData.value = ChangePasswordState.Failed(errorMessage ?: "Registration Failed")
                }
            } catch (ex: Exception) {
                _changePasswordStateLiveData.value = ChangePasswordState.Failed("An error occurred during registration..")
            }
        }
    }

}
