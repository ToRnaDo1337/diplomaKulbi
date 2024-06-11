package com.example.ctrlbee.data.remote

import com.example.ctrlbee.domain.model.profile.ProfileResponse
import com.example.ctrlbee.domain.model.profile.UpdateStatusRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileApiService {
    @Multipart
    @POST("/api/profiles")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part("username") username: RequestBody,
//        @Part("dateOfBirthday") dateOfBirthday: RequestBody,
//        @Part("country") country: RequestBody,
        @Part profileImage: MultipartBody.Part,
    ): String

    @Multipart
    @PUT("/api/profiles/status")
    suspend fun updateBio(
        @Header("Authorization") token: String,
        @Part("status") status: RequestBody
    ): String

    @GET("/api/profiles")
    suspend fun getProfile(@Header("Authorization") token: String): ProfileResponse
}

