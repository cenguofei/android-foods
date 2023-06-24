package com.example.network.remote.repository

import com.example.model.remoteModel.Favorite

interface IFavoriteApi {

    suspend fun getAllFavorites(username:String) : List<Favorite>

    suspend fun addFavorite(favorite: Favorite) : HashMap<String,Any>

    suspend fun deleteFavorite(username: String,foodId:Long) : HashMap<String,Any>
}