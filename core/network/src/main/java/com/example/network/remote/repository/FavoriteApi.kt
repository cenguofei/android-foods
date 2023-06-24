package com.example.network.remote.repository

import com.example.model.remoteModel.Favorite
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface FavoriteApi : IFavoriteApi {

    @GET("frontfavorite/queryAllFavorites")
    override suspend fun getAllFavorites(@Query("username") username:String) : List<Favorite>

    @Headers("Content-Type:application/json")
    @POST("frontfavorite/addFavorite")
    override suspend fun addFavorite(@Body favorite: Favorite) : HashMap<String,Any>

    @Headers("Content-Type:application/json")
    @POST("frontfavorite/deleteFavorite")
    override suspend fun deleteFavorite(@Query("username") username: String,@Query("foodId") foodId:Long) : HashMap<String,Any>
}