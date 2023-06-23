package com.example.network.remote.repository

import com.example.common.di.Dispatcher
import com.example.common.di.FoodsDispatchers
import com.example.model.remoteModel.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    @Dispatcher(FoodsDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val apiParam: ApiParam
) {

    private val remoteService: UserApi = apiParam.retrofit.create(UserApi::class.java)

    suspend fun getAllUser(): List<User> = withContext(dispatcher) {
        return@withContext remoteService.getAllUser()
    }

    suspend fun login(username: String, password: String): HashMap<String, Any> {
        return withContext(dispatcher) {
            remoteService.login(username, password)
        }
    }

    suspend fun register(user: User): HashMap<String, Any> {
        return withContext(dispatcher) {
            remoteService.register(
                user.username,
                user.password,
                user.email,
                user.tel,
                user.sex.toString()
            )
        }
    }
}