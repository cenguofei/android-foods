package com.example.network.remote.repository

import com.example.network.remote.model.Food

interface RemoteService {

    suspend fun getAllFood() : List<Food>


}