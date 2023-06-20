package com.example.network.remote.repository

import com.example.common.di.Dispatcher
import com.example.common.di.FoodsDispatchers
import com.example.model.page.PageList
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.Order
import com.example.network.remote.repository.ApiParam.Companion.retrofit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    @Dispatcher(FoodsDispatchers.IO) private val dispatcher: CoroutineDispatcher
) {
//    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val remoteService: FoodApi = retrofit.create(FoodApi::class.java)

    suspend fun getAllFood(): Flow<List<Food>> = flow {
        val result = withContext(dispatcher) {
            remoteService.getAllFood()
        }
        emit(result)
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

    suspend fun queryLike(
        foodName:String,
        offset:Int = 0,
        pageSize:Int = 10
    ) : Flow<PageList<Food>> = flow {
        val result = withContext(dispatcher) {
            remoteService.queryLike(foodName,offset,pageSize)
        }
        emit(result)
    }
}

