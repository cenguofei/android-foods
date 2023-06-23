package com.example.database.db

import android.content.Context
import com.example.model.localmodel.LocalFood
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodsDatabaseRepository @Inject constructor(
    @ApplicationContext private val context:Context
) {
    private val dispatcher = Dispatchers.IO
    private val foodDao: ShoppingCartDao = FoodsDatabase.getInstance(context).getFoodDao()

    suspend fun addFoodToShoppingCart(food: LocalFood) {
        withContext(dispatcher) {
            foodDao.addFoodToShoppingCart(food)
        }
    }

    suspend fun updateFoodFromShoppingCart(food: LocalFood) {
        withContext(dispatcher) {
            foodDao.updateFoodFromShoppingCart(food)
        }
    }

    suspend fun delete(food: LocalFood) {
        withContext(dispatcher) {
            foodDao.delete(food)
        }
    }

    fun getAllCartFoods(username:String) : Flow<List<LocalFood>> = foodDao.queryAllCartFood(username)

    suspend fun clearAllShoppingCart(username: String) {
        withContext(dispatcher) {
            foodDao.clearAllShoppingCart(username)
        }
    }
    suspend fun deleteWhereCountLessThanZero() {
        withContext(dispatcher) {
            foodDao.deleteWhereCountLessThanZero()
        }
    }
}