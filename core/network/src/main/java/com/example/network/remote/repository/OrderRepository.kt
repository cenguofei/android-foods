package com.example.network.remote.repository

import com.example.common.di.Dispatcher
import com.example.common.di.FoodsDispatchers
import com.example.model.remoteModel.Order
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    @Dispatcher(FoodsDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val apiParam: ApiParam
) {
    private val orderApi:OrderApi = apiParam.retrofit.create(OrderApi::class.java)

    suspend fun postOrder(order: Order) : HashMap<String,Any> {
        return withContext(dispatcher) {
            orderApi.postOrder(order)
        }
    }

    suspend fun getUserOrders(username:String): Flow<HashMap<String, Any>> = flow {
        val result = withContext(dispatcher) {
            orderApi.getUserOrders(username)
        }
        emit(result)
    }
}