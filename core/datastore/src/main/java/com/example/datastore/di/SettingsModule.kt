package com.example.datastore.di

import com.example.datastore.UserDataRepository
import com.example.datastore.UserDataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface SettingsModule {
    @Binds
    @Singleton
    fun bindsUserDataRepository(
        userDataRepositoryImpl: UserDataRepositoryImpl
    ) : UserDataRepository
}