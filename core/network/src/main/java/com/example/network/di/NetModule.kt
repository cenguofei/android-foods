package com.example.network.di

import com.example.network.netstate.ConnectivityManagerNetworkMonitor
import com.example.network.netstate.NetworkMonitor
import com.example.network.remote.repository.RemoteService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetModule {

    @Binds
    fun bindsConnectivityManagerNetMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ):NetworkMonitor
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RetrofitModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(RemoteService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesRemoteService(
        retrofit: Retrofit
    ):RemoteService = retrofit.create(RemoteService::class.java)
}