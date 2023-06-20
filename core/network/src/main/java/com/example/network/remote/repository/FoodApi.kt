package com.example.network.remote.repository

import com.example.model.page.PageList
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.Order
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface FoodApi {
    @GET("frontfood/all")
    suspend fun getAllFood() : List<Food>

    @Headers("Content-Type:application/json")
    @POST("frontorder/add")
    suspend fun postOrder(@Body order: Order) : HashMap<String,Any>


    @GET("frontorder/queryOrderByUsername")
    suspend fun getUserOrders(@Query("username") username:String) : HashMap<String,Any>

    @GET("food/listpage")
    suspend fun queryLike(
        @Query("foodName") foodName:String,
        @Query("offset") offset:Int,
        @Query("pageSize") pageSize:Int
    ) : PageList<Food>
}