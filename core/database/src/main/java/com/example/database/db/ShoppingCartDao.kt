package com.example.database.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.localmodel.LocalFood
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingCartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFoodToShoppingCart(food: LocalFood)

    @Query("SELECT * FROM food_table WHERE username = :username")
    fun queryAllCartFood(username: String): Flow<List<LocalFood>>

    @Update
    suspend fun updateFoodFromShoppingCart(food: LocalFood)

    @Query("DELETE FROM food_table WHERE username = :username AND createUserId = :sellerId")
    suspend fun clearAllShoppingCart(username: String, sellerId: Long)

    @Query("DELETE FROM food_table WHERE count <= 0")
    suspend fun deleteWhereCountLessThanZero()

    @Delete
    suspend fun delete(food: LocalFood)
}