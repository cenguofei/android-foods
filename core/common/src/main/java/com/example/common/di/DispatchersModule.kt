package com.example.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Module
@InstallIn(ActivityComponent::class)
object DispatchersModule {

    @IODispatcher
    @Provides
    fun providesDispatcherIO():CoroutineDispatcher = Dispatchers.IO

    @MainDispatcher
    @Provides
    fun providesDispatcherMain():CoroutineDispatcher = Dispatchers.Main
}