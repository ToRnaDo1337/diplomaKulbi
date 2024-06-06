package com.example.ctrlbee.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ProfileRepository {
    suspend fun updateProfile(
        token: String,
        username: RequestBody,
//        dateOfBirthday: RequestBody,
//        country: RequestBody,
        profileImage: MultipartBody.Part,
    ): Pair<String?, String?>
}
