package com.example.network.remote.repository

import com.example.model.remoteModel.Order

interface IOrderApi {
    suspend fun postOrder(order: Order) : HashMap<String,Any>

    suspend fun getUserOrders(username:String) : HashMap<String,Any>
}