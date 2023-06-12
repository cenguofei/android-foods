package com.example.network.remote.repository

import com.example.network.remote.model.Food
import com.example.network.remote.model.Order
import com.example.network.remote.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Query
import javax.inject.Inject

class RemoteRepository constructor(
//    @Inject private val remoteService: RemoteService,
) {
    private val remoteService: RemoteService = Retrofit.Builder()
        .baseUrl(RemoteService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(RemoteService::class.java)
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO


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

