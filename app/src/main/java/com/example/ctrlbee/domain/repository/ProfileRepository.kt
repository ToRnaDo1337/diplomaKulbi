package com.example.ctrlbee.domain.repository

import com.example.ctrlbee.domain.model.posts.PostResponse
import com.example.ctrlbee.domain.model.profile.ProfileResponse
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
    suspend fun addPost(
        token: String,
        description: RequestBody,
        media: MultipartBody.Part,
    ): Pair<String?, String?>

    suspend fun updateStatus(
        token: String,
        status: RequestBody
    ): Pair<String?, String?>

    suspend fun getProfile(token: String): Pair<ProfileResponse?, String?>

    suspend fun getPosts(token: String): Pair<List<PostResponse>?, String?>
}
