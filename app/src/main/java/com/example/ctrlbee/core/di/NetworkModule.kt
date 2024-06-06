package com.example.ctrlbee.core.di

import com.example.ctrlbee.data.remote.AuthApiService
import com.example.ctrlbee.data.remote.ProfileApiService
import com.example.ctrlbee.data.remote.RoomApiService
import com.example.ctrlbee.data.remote.ToDoListApiService
import com.example.ctrlbee.data.repository.ProfileRepositoryImpl
import com.example.ctrlbee.data.repository.RoomRepositoryImpl
import com.example.ctrlbee.data.repository.ToDoListRepositoryImpl
import com.example.ctrlbee.data.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://138.68.125.57:8080"

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() =
        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().apply {
            this.addInterceptor(httpLoggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
        }.build()

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApiService = retrofit.create(AuthApiService::class.java)

    @Singleton
    @Provides
    fun provideUserRepo(authService: AuthApiService) = UserRepositoryImpl(authService)

    @Singleton
    @Provides
    fun provideProfileApi(retrofit: Retrofit): ProfileApiService = retrofit.create(ProfileApiService::class.java)

    @Singleton
    @Provides
    fun provideProfileRepo(profileService: ProfileApiService) = ProfileRepositoryImpl(profileService)

    @Singleton
    @Provides
    fun provideToDoListApi(retrofit: Retrofit): ToDoListApiService = retrofit.create(ToDoListApiService::class.java)

    @Singleton
    @Provides
    fun provideToDoListRepo(toDoListService: ToDoListApiService) = ToDoListRepositoryImpl(toDoListService)

    @Singleton
    @Provides
    fun provideRoomApi(retrofit: Retrofit): RoomApiService = retrofit.create(RoomApiService::class.java)

    @Singleton
    @Provides
    fun provideRoomRepo(roomApiService: RoomApiService) = RoomRepositoryImpl(roomApiService)
}