package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.storagemodel.DarkThemeConfig
import com.example.model.storagemodel.ThemeBrand
import com.example.model.storagemodel.UserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FoodsPreferencesDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore("settings")
    private val userPreferences = context.datastore

    companion object {
        private val IS_FIRST_USE = booleanPreferencesKey("isFirstUse")

        private val USE_DYNAMIC = booleanPreferencesKey("useDynamic")

        private val THEM_BRAND_KEY = intPreferencesKey("themeBrand")
        private const val THEME_BRAND_DEFAULT_VALUE = 0
        private const val THEME_BRAND_ANDROID_VALUE = 1


        private val DARK_THEME_CONFIG_KEY = intPreferencesKey("darkThemeConfig")
        private const val DARK_THEME_CONFIG_FOLLOW_SYSTEM_VALUE = 2
        private const val DARK_THEME_CONFIG_LIGHT_VALUE = 3
        private const val DARK_THEME_CONFIG_DARK_VALUE = 4
    }

    val userData = userPreferences.data
        .map {
            UserData(
                themeBrand = when (it[THEM_BRAND_KEY]) {
                    THEME_BRAND_ANDROID_VALUE -> ThemeBrand.ANDROID
                    else -> ThemeBrand.DEFAULT
                },
                darkThemeConfig = when (it[DARK_THEME_CONFIG_KEY]) {
                    DARK_THEME_CONFIG_FOLLOW_SYSTEM_VALUE -> DarkThemeConfig.FOLLOW_SYSTEM
                    DARK_THEME_CONFIG_DARK_VALUE -> DarkThemeConfig.DARK
                    else -> DarkThemeConfig.LIGHT
                },
                useDynamicColor = it[USE_DYNAMIC] ?: false,
                isFirstUse = it[IS_FIRST_USE] ?: true
            )
        }

    suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        userPreferences.edit {
            it[THEM_BRAND_KEY] = if (ThemeBrand.ANDROID == themeBrand) {
                THEME_BRAND_ANDROID_VALUE
            } else {
                THEME_BRAND_DEFAULT_VALUE
            }
        }
    }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferences.edit {
            it[USE_DYNAMIC] = useDynamicColor
        }
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.edit {
            it[DARK_THEME_CONFIG_KEY] = when(darkThemeConfig) {
                DarkThemeConfig.FOLLOW_SYSTEM -> DARK_THEME_CONFIG_FOLLOW_SYSTEM_VALUE
                DarkThemeConfig.LIGHT -> DARK_THEME_CONFIG_LIGHT_VALUE
                else -> DARK_THEME_CONFIG_DARK_VALUE
            }
        }
    }

    suspend fun updateUseState(firstUse: Boolean) {
        userPreferences.edit {
            it[IS_FIRST_USE] = firstUse
        }
    }
}

