package cn.example.foods.composefoods.datasource

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.datastore.UserSettings
import com.example.network.remote.repository.RemoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourceContainer @Inject constructor() {
    @Inject lateinit var remoteRepository: RemoteRepository

    @Inject lateinit var userSettings: UserSettings
}