package com.example.network.remote.repository

import com.example.network.remote.model.Food
import com.example.network.remote.model.Order
import com.example.network.remote.model.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteService {

    companion object {
        const val BASE_URL = "http://10.129.67.213:80/"
    }

    @GET("frontfood/all")
    suspend fun getAllFood() : List<Food>

    @Headers("Content-Type:application/json")
    @POST
    suspend fun postOrder(@Body order: Order) : HashMap<String,Any>

    @GET("frontorder/queryOrderByUsername")
    suspend fun getUserOrders(@Query("username") username:String) : List<Order>


    @POST("frontuser/reg")
    suspend fun register(user: User) : HashMap<String,Any>

    @POST("frontuser/login")
    suspend fun login(@Body user:User) : HashMap<String,Any>
}