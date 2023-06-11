package cn.example.foods

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoodsApp : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}