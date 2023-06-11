package com.example.network.remote.repository

import com.example.common.di.IODispatcher
import com.example.network.remote.model.Food
import com.example.network.remote.model.Order
import com.example.network.remote.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.http.Body
import retrofit2.http.Query
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    @Inject private val remoteService: RemoteService,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun getAllFoodAllFood() : Flow<List<Food>> = flow {
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

    suspend fun getUserOrders(username:String) : Flow<List<Order>> = flow {
        val result = withContext(dispatcher) {
            remoteService.getUserOrders(username)
        }
        emit(result)
    }

    suspend fun register(user: User) : HashMap<String,Any> {
        return withContext(dispatcher) {
            remoteService.register(user)
        }
    }
}

