package com.example.network.remote.repository

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface ApiParam {

    companion object {
        const val BASE_URL = "http://10.129.67.213:80/"

        const val IMAGE_USER_URL = BASE_URL + "static/upload/"
        const val IMAGE_FOOD_URL = BASE_URL + "food/showimg/"

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}