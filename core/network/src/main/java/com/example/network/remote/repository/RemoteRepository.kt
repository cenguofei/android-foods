package com.example.network.remote.repository

import com.example.network.remote.model.Food
import com.example.network.remote.model.Order
import com.example.network.remote.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepository @Inject constructor() {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val remoteService: RemoteApii = Retrofit.Builder()
        .baseUrl(RemoteApii.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RemoteApii::class.java)


    suspend fun getAllFood() : Flow<List<Food>> = flow {
        val result = withContext(dispatcher) {
            remoteService.getAllFood()
        }
        emit(result)
    }

    suspend fun postOrder(order: Order): HashMap<String,Any> {
        return withContext(dispatcher) {
            remoteService.postOrder(order)
        }
    }

    suspend fun getUserOrders(username:String) : Flow<HashMap<String,Any>> = flow {
        val result = withContext(dispatcher) {
            remoteService.getUserOrders(username)
        }
        emit(result)
    }

    suspend fun login(username: String, password:String) : HashMap<String,Any> {
        return withContext(dispatcher) {
            remoteService.login(username,password)
        }
    }

    suspend fun register(user: User) : HashMap<String,Any> {
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

