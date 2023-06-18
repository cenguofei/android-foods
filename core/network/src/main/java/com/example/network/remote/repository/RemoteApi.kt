package com.example.network.remote.repository

import com.example.model.remoteModel.Food
import com.example.model.remoteModel.Order
import com.example.model.remoteModel.User
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface RemoteApi {

    companion object {
        const val BASE_URL = "http://10.129.67.213:80/"

        const val IMAGE_USER_URL = BASE_URL + "static/upload/"
        const val IMAGE_FOOD_URL = BASE_URL + "food/showimg/"
    }

    @GET("frontfood/all")
    suspend fun getAllFood() : List<Food>



    @GET("frontuser/queryAllUsers")
    suspend fun getAllUser() : List<User>

    @Headers("Content-Type:application/json")
    @POST("frontorder/add")
    suspend fun postOrder(@Body order: Order) : HashMap<String,Any>


    @GET("frontorder/queryOrderByUsername")
    suspend fun getUserOrders(@Query("username") username:String) : HashMap<String,Any>


    @FormUrlEncoded
    @POST("frontuser/reg")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("tel") tel: String,
        @Field("sex") sex: String
    ) : HashMap<String,Any>

    @FormUrlEncoded
    @POST("frontuser/login")
    suspend fun login(@Field("username") username: String, @Field("password") password:String) : HashMap<String,Any>
}