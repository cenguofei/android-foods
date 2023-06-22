package com.example.database.db

import android.content.Context
import com.example.common.di.Dispatcher
import com.example.common.di.FoodsDispatchers
import com.example.database.db.di.FavoriteTable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodsDatabaseRepository @Inject constructor(
    @Dispatcher(FoodsDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    @ApplicationContext private val context:Context
) {
    private val foodDao: FavoriteDao = FoodsDatabase.getInstance(context).getFoodDao()

    suspend fun addFoods(favorite: FavoriteTable) {
        withContext(dispatcher) {
            foodDao.insert(favorite)
        }
    }

    suspend fun deleteFoods(favorite: FavoriteTable) {
        withContext(dispatcher) {
            foodDao.delete(favorite)
        }
    }

    fun queryAllFavorites(username:String) : Flow<List<FavoriteTable>> = foodDao.queryFavorites(username)
}