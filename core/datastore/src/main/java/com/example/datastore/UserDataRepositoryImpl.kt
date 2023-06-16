package com.example.datastore

import com.example.model.storagemodel.DarkThemeConfig
import com.example.model.storagemodel.ThemeBrand
import com.example.model.storagemodel.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UserDataRepositoryImpl @Inject constructor(
    private val preferencesDataSource: FoodsPreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        preferencesDataSource.userData


    override suspend fun setThemeBrand(themeBrand: ThemeBrand) {
        preferencesDataSource.setThemeBrand(themeBrand)
    }

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        preferencesDataSource.setDarkThemeConfig(darkThemeConfig)
    }

    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        preferencesDataSource.setDynamicColorPreference(useDynamicColor)
    }

    override suspend fun updateUseState(firstUse: Boolean) {
        preferencesDataSource.updateUseState(firstUse)
    }
}