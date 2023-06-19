package com.example.network.remote.repository

import com.example.model.remoteModel.Favorite
import com.example.model.remoteModel.User
import retrofit2.http.Body
import retrofit2.http.Query

interface IFavoriteApi {

    suspend fun getAllFavorites(username:String) : List<Favorite>

    suspend fun addFavorite(favorite: Favorite) : HashMap<String,Any>

    suspend fun deleteFavorite(id:Long) : HashMap<String,Any>
}