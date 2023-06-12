package com.example.datastore.di

import android.content.Context
import com.example.datastore.UserSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    fun providesDatastore(
        @ApplicationContext context: Context
    ) : UserSettings {
        return UserSettings(context)
    }

}