package com.example.common.di//package com.example.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier


//@Qualifier
//@Retention(AnnotationRetention.BINARY)
//annotation class IODispatcher
//
//@Qualifier
//@Retention(AnnotationRetention.BINARY)
//annotation class MainDispatcher
//
//@Module
//@InstallIn(ActivityComponent::class)
//object DispatchersModule {
//
//    @IODispatcher
//    @Provides
//    fun providesDispatcherIO():CoroutineDispatcher = Dispatchers.IO
//
//    @MainDispatcher
//    @Provides
//    fun providesDispatcherMain():CoroutineDispatcher = Dispatchers.Main
//}

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(FoodsDispatchers.IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(FoodsDispatchers.Default)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: FoodsDispatchers)

enum class FoodsDispatchers {
    Default,
    IO,
}

