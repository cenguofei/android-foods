package com.example.database.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.db.di.FavoriteTable
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteTable)

    @Query("SELECT * FROM favorite_table WHERE username = :username")
    fun queryFavorites(username: String): Flow<List<FavoriteTable>>

    @Delete
    suspend fun delete(favorite: FavoriteTable)
}