package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SystemSettings @Inject constructor(
    @ApplicationContext val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        val IS_FIRST_USE = booleanPreferencesKey("isFirstUse")

    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore("settings",scope = scope)


    private var _userSettings = context.datastore.data
        .map {
            val isFirstUse = it[IS_FIRST_USE]
            UserSettings(
                isFirstUse ?: false
            )
        }
    val userSettings = _userSettings

    fun updateUseState(isFirstUse: Boolean) {
        scope.launch(dispatcher) {
            context.datastore.edit { preferences ->
                preferences[IS_FIRST_USE] = isFirstUse
            }
        }
    }
}

data class UserSettings(
    val isFirstUse:Boolean
)