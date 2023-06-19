package com.example.network.remote.repository

import com.example.model.remoteModel.Favorite
import com.example.model.remoteModel.Order
import com.example.model.remoteModel.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FavoriteApi : IFavoriteApi {

    @GET("frontfavorite/queryAllFavorites")
    override suspend fun getAllFavorites(@Query("username") username:String) : List<Favorite>

    @Headers("Content-Type:application/json")
    @POST("frontfavorite/addFavorite")
    override suspend fun addFavorite(@Body favorite: Favorite) : HashMap<String,Any>

    @Headers("Content-Type:application/json")
    @POST("frontfavorite/addFavorite/{id}")
    override suspend fun deleteFavorite(@Path("id") id:Long) : HashMap<String,Any>
}