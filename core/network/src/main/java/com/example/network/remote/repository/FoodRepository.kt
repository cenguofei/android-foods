package com.example.network.remote.repository

import com.example.common.di.Dispatcher
import com.example.common.di.FoodsDispatchers
import com.example.model.page.PageList
import com.example.model.remoteModel.Food
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    @Dispatcher(FoodsDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val apiParam: ApiParam
) {
//    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
    private val remoteService: FoodApi = apiParam.retrofit.create(FoodApi::class.java)

    // TODO 使用Paging进行分页下载
    suspend fun getAllFood(): Flow<List<Food>> = flow {
        val result = withContext(dispatcher) {
            remoteService.getAllFood()
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

