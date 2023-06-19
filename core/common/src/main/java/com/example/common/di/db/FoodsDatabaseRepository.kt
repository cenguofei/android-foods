//package cn.example.foods.db
//
//import android.content.Context
//import com.example.common.di.Dispatcher
//import com.example.common.di.FoodsDispatchers
//import com.example.model.remoteModel.Favorite
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.CoroutineDispatcher
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.withContext
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class FoodsDatabaseRepository @Inject constructor(
//    @Dispatcher(FoodsDispatchers.IO) private val dispatcher: CoroutineDispatcher,
//    @ApplicationContext private val context:Context
//) {
//    private val foodDao: FavoriteDao = FoodsDatabase.getInstance(context).getFoodDao()
//
//    suspend fun addFoods(favorite: Favorite) {
//        withContext(dispatcher) {
//            foodDao.insert(favorite)
//        }
//    }
//
//    suspend fun deleteFoods(favorite: Favorite) {
//        withContext(dispatcher) {
//            foodDao.delete(favorite)
//        }
//    }
//
//    fun queryAllFavorites(username:String) : Flow<List<Favorite>> = foodDao.queryFavorites(username)
//}