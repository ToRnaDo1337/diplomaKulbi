package com.example.ctrlbee.core.di

import com.example.ctrlbee.data.repository.*
import com.example.ctrlbee.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun provideUserRepository(userRepo: UserRepositoryImpl): UserRepository

    @Binds
    fun provideProfileRepository(profileRepo: ProfileRepositoryImpl): ProfileRepository

    @Binds
    fun provideToDoListRepository(toDoListRepository: ToDoListRepositoryImpl): ToDoListRepository

    @Binds
    fun provideRoomRepository(roomRepository: RoomRepositoryImpl): RoomRepository
}
