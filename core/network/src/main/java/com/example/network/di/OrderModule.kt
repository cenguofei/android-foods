package com.example.network.di

import com.example.network.remote.repository.OrderRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

//@Module
//@InstallIn(ActivityComponent::class)
//abstract class OrderModule {
//
//
//    @Binds
//    abstract fun bindsOrder(
//        commitOrderImpl: CommitOrderImpl
//    ) : CommitOrder
//}

//@Module
//@InstallIn(ActivityComponent::class)
//class ImplModule {
//
//    @Provides
//    fun providesOrder(
//        orderRepository: OrderRepository
//    ) : CommitOrderImpl = CommitOrderImpl(orderRepository)
//}