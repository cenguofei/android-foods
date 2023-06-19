//package cn.example.foods.db
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.model.remoteModel.Favorite
//import com.example.model.remoteModel.User
//
//@Database(
//    entities = [
//        Favorite::class, User::class
//    ],
//    version = 1,
//    exportSchema = false
//)
//abstract class FoodsDatabase : RoomDatabase() {
//
//    abstract fun getFoodDao() : FavoriteDao
//
//    companion object {
//        @Volatile private var instance : FoodsDatabase? = null
//
//        fun getInstance(context:Context) : FoodsDatabase =
//            instance ?: synchronized(FoodsDatabase::class.java) {
//                instance ?: Room.databaseBuilder(context, FoodsDatabase::class.java,"foods_dp")
//                    .build().
//                    also {
//                        instance = it
//                    }
//            }
//    }
//}