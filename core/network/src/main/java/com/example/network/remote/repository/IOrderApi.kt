package com.example.network.remote.repository

import com.example.model.remoteModel.Order
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface IOrderApi {
    suspend fun postOrder(order: Order) : HashMap<String,Any>

    suspend fun getUserOrders(username:String) : HashMap<String,Any>
}