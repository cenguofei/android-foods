package com.example.foods

import android.app.Application
import com.example.model.remoteModel.User
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FoodsApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    private var _currentUser: User = User.NONE

    fun setCurrentUser(user: User) {
        _currentUser = user
    }

    fun getCurrentUser():User = _currentUser
}