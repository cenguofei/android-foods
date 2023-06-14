package cn.example.foods

import android.app.Application
import com.example.network.remote.remoteModel.User
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoodsApp : Application() {

    override fun onCreate() {
        super.onCreate()

    }

    var currentUser:User = User.NONE
}