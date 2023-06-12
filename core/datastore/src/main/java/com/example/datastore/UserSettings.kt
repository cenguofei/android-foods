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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserSettings @Inject constructor(
    @ApplicationContext val context: Context,
) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private var scope: CoroutineScope
    var isFirstUse:MutableStateFlow<Boolean>
        private set

    companion object {

        val IS_FIRST_USE = booleanPreferencesKey("isFirstUse")
    }

    init {
        isFirstUse = MutableStateFlow(true)
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        isFirstUse()
    }
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore("settings",scope = scope)


    private fun isFirstUse() {
        scope.launch {
            isFirstUse.value = context.datastore.data
                .map { it[IS_FIRST_USE] ?: true }
                .distinctUntilChanged()
                .firstOrNull() ?: true
        }
    }

    fun updateUseState(isFirstUse: Boolean) {
        scope.launch(dispatcher) {
            context.datastore.edit { preferences ->
                preferences[IS_FIRST_USE] = isFirstUse
            }
            this@UserSettings.isFirstUse.value = false
        }
    }
}

