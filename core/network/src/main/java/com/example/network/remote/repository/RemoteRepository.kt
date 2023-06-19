package com.example.network.remote.repository

import android.util.Log
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.Order
import com.example.model.remoteModel.User
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepository @Inject constructor() {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val remoteService: RemoteApi = Retrofit.Builder()
        .baseUrl(RemoteApi.BASE_URL)
        .client(
            OkHttpClient.Builder()
                .readTimeout(1,TimeUnit.MINUTES)
                .connectTimeout(1,TimeUnit.MINUTES)
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RemoteApi::class.java)


    suspend fun getAllFood(): Flow<List<Food>> = flow {
        val result = withContext(dispatcher) {
            remoteService.getAllFood()
        }
        emit(result)
    }

    suspend fun getAllUser(): List<User> = withContext(dispatcher) {
        return@withContext remoteService.getAllUser()
    }

    suspend fun postOrder(order: Order): HashMap<String, Any> {
        return withContext(dispatcher) {
            remoteService.postOrder(order)
        }
    }

    suspend fun getUserOrders(username: String): Flow<HashMap<String, Any>> = flow {
        val result = withContext(dispatcher) {
            remoteService.getUserOrders(username)
        }
        emit(result)
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
                user.email!!,
                user.tel ?: "",
                user.sex.toString()
            )
        }
    }
}

