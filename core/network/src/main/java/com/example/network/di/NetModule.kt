package com.example.network.di

import com.example.network.netstate.ConnectivityManagerNetworkMonitor
import com.example.network.netstate.NetworkMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetModule {

    @Singleton
    @Binds
    fun bindsConnectivityManagerNetMonitor(
        networkMonitor: ConnectivityManagerNetworkMonitor
    ):NetworkMonitor
}

//@Module
//@InstallIn(SingletonComponent::class)
//object RetrofitModule {
//
//    @Provides
//    @Singleton
//    fun providesRemoteRepository() : RemoteRepository = RemoteRepository()
//}
