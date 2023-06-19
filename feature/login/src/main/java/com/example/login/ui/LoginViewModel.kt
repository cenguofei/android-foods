package com.example.login.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.di.Dispatcher
import com.example.common.di.FoodsDispatchers
import com.example.model.remoteModel.User
import com.example.network.remote.repository.ApiParam
import com.example.network.remote.repository.RemoteRepository
import com.google.gson.internal.LinkedTreeMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    @Dispatcher(FoodsDispatchers.IO) private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    init {
        Log.v("cgf","LoginViewModel RemoteRepository -> $remoteRepository")
    }
    fun login(
        username: String, password: String,
        onSuccess: (user: User) -> Unit,
        onError: (msg: String) -> Unit
    ) {
        viewModelScope.launch(dispatcher) {
            val result = remoteRepository.login(username, password)
            try {
                Log.v("login_test","result = ${result.toString()}")
                val isSuccess = result["isSuccess"] as Boolean
                if (isSuccess) {
                    val userMap = result["user"] as LinkedTreeMap<*, *>
                    val user = User(
                        username = username,
                        password = password,
                        email = userMap["email"] as? String ?: "",
                        tel = userMap["tel"] as? String ?: "",
//                        createTime = userMap["createTime"] as? String ?: "",
                        headImg = ApiParam.IMAGE_USER_URL+(userMap["headImg"] as? String ?: "")
                    )
//                    user.headImg = RemoteApi.IMAGE_BASE_URL + user.headImg
                    launch(Dispatchers.Main) {
                        onSuccess(user)
                    }
                } else {
                    val msg = result["msg"] as? String ?: ""
                    onError(msg)
                }
            } catch (e : Exception) {
                Log.v("login_test","Exception = ${e.message}")
                onError(e.cause?.message ?: "unknown error")
            }
        }
    }

    fun register(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: (user: User) -> Unit,
        onError: (msg: String) -> Unit,
        onInputError: () -> Unit
    ) {
        if (username.isBlank() or password.isBlank() or email.isBlank() or (password != confirmPassword)) {
            onInputError()
            return
        }
        viewModelScope.launch(dispatcher) {
            val result = remoteRepository.register(
                User(
                    username = username,
                    password = password,
                    email = email
                )
            )
            try {
                val isSuccess = result["isSuccess"] as Boolean
//                val headImg = result["headImg"] as String
                val msg = result["msg"] as String

                if (isSuccess) {
                    launch(Dispatchers.Main) {
                        onSuccess(User(username = username, email = email))
                    }
                } else {
                    onError(msg)
                }
            } catch (e: Exception) {
                val msg = e.cause?.message ?: "unknown error"
                onError(msg)
            }
        }
    }
}