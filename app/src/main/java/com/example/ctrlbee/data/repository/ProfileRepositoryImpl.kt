package com.example.ctrlbee.data.repository

import com.example.ctrlbee.data.remote.ProfileApiService
import com.example.ctrlbee.domain.repository.ProfileRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileRepositoryImpl
    @Inject
    constructor(
        private val profileService: ProfileApiService,
    ) : ProfileRepository {
        override suspend fun updateProfile(
            token: String,
            username: RequestBody,
//            dateOfBirthday: RequestBody,
//            country: RequestBody,
            profileImage: MultipartBody.Part,
        ): Pair<String?, String?> {
            return try {
                val response =
                    profileService.updateProfile(
                        token = "Bearer $token",
                        username = username,
//                        dateOfBirthday = dateOfBirthday,
//                        country = country,
                        profileImage = profileImage,
                    )
                Pair(response, null)
            } catch (ex: Exception) {
                Pair(null, ex.message)
            }
        }
    }
