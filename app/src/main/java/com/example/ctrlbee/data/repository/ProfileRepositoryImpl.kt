package com.example.ctrlbee.data.repository

import android.util.Log
import com.example.ctrlbee.data.remote.ProfileApiService
import com.example.ctrlbee.domain.model.posts.PostResponse
import com.example.ctrlbee.domain.model.profile.ProfileResponse
import com.example.ctrlbee.domain.repository.ProfileRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    override suspend fun addPost(
        token: String,
        description: RequestBody,
        media: MultipartBody.Part
    ): Pair<String?, String?> {
        return try {
            val response = profileService.addPost(
                token = "Bearer $token",
                description = description,
                media = media
            )
            Pair(response, null)
        } catch (ex: Exception) {
            Pair(null, ex.message)
        }
    }


    override suspend fun updateStatus(token: String, status: RequestBody): Pair<String?, String?> {
        return try {
            Log.d("Request body", "TOKEN"+token+status)
            val response = profileService.updateBio(
                token = "Bearer $token",
                status = status
            )
            Log.d("PROFILE REPOS","REPOS"+response)
            Pair(response, null)
        } catch (ex: Exception) {
            Log.d("PROFILE REPOS","REPOS"+ex.message)
            Pair(null, ex.message)

        }
    }

    override suspend fun getProfile(token: String): Pair<ProfileResponse?, String?> {
        return try {
            val response = profileService.getProfile(token = "Bearer $token")
            Pair(response, null)
        } catch (ex: Exception) {
            Pair(null, ex.message)
        }
    }
    override suspend fun getPosts(token: String): Pair<List<PostResponse>?, String?> {
        return try {
            val response = profileService.getPosts("Bearer $token")
            Pair(response, null)
        } catch (ex: Exception) {
            Pair(null, ex.message)
        }
    }
}
