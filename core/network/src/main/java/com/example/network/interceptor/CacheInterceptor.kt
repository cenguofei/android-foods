package com.example.network.interceptor

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import com.example.network.netstate.isCurrentlyConnected
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class CacheInterceptor(appContext:Context) : Interceptor {
        private val connectivityManager = appContext.getSystemService<ConnectivityManager>()
        override fun intercept(chain: Interceptor.Chain): Response {
            val connected = connectivityManager?.isCurrentlyConnected() ?: false
            
            var request = chain.request()
            if (!connected) {
                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
            }
            val response = chain.proceed(request)
            if (!connected) {
                val maxAge = 60 * 60
                response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .build()
            } else {
                val maxStale = 60 * 60 * 24 * 7 // tolerate 4-weeks stale
                response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public,max-stale=$maxStale")
                    .build()
            }
            return response
        }
    }