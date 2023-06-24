package com.example.network.remote.repository

import android.content.Context
import com.example.network.interceptor.CacheInterceptor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiParam @Inject constructor(
    @ApplicationContext private val appContext:Context
) {

    private val cacheControl = CacheInterceptor(appContext = appContext)

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .apply {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        },
                )
//                .addInterceptor(cacheControl)
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    companion object {
        private const val BASE_URL = "http://10.129.67.213:80/"

        const val IMAGE_USER_URL = BASE_URL + "static/upload/"
        const val IMAGE_FOOD_URL = BASE_URL + "food/showimg/"
    }

    val coroutineScope = CoroutineScope(SupervisorJob(Job()) + Dispatchers.IO)
}