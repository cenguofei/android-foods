package com.example.network.remote.repository

import com.example.model.remoteModel.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @GET("frontuser/queryAllUsers")
    suspend fun getAllUser() : List<User>

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