package com.example.network.remote.repository

import com.example.common.di.Dispatcher
import com.example.common.di.FoodsDispatchers
import com.example.model.remoteModel.Favorite
import com.example.model.remoteModel.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    @Dispatcher(FoodsDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val apiParam: ApiParam
){

    private val remoteService: FavoriteApi = apiParam.retrofit.create(FavoriteApi::class.java)

    suspend fun getAllFavorites(username: String): Flow<List<Favorite>> = flow {
        emit(withContext(dispatcher) { remoteService.getAllFavorites(username) })
    }

    suspend fun addFavorite(favorite: Favorite): HashMap<String, Any> {
        return withContext(dispatcher) {
            remoteService.addFavorite(favorite)
        }
    }

    suspend fun deleteFavorite(username: String, foodId:Long): HashMap<String, Any> {
        return withContext(dispatcher) {
            remoteService.deleteFavorite(username,foodId)
        }
    }
}