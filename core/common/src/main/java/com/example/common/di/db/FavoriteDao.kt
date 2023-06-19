//package cn.example.foods.db
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.example.model.remoteModel.Favorite
//import com.example.model.remoteModel.Food
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface FavoriteDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(favorite: Favorite)
//
//    @Query("SELECT * FROM favorite_table WHERE username = :username")
//    fun queryFavorites(username: String): Flow<List<Favorite>>
//
//    @Delete
//    suspend fun delete(favorite: Favorite)
//}