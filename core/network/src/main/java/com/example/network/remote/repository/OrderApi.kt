package com.example.network.remote.repository

import com.example.model.remoteModel.Order
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface OrderApi : IOrderApi {
    @Headers("Content-Type:application/json")
    @POST("frontorder/add")
    override suspend fun postOrder(@Body order: Order) : HashMap<String,Any>


    @GET("frontorder/queryOrderByUsername")
    override suspend fun getUserOrders(@Query("username") username:String) : HashMap<String,Any>
}