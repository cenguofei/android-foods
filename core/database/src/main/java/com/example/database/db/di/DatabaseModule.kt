package com.example.database.db.di

import android.content.Context
import com.example.database.db.FoodsDatabase
import com.example.database.db.ShoppingCartDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun bindsFoodsDao(
        @ApplicationContext context: Context
    ) : ShoppingCartDao = FoodsDatabase.getInstance(context).getFoodDao()
}