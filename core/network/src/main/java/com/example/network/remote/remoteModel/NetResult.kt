package com.example.network.remote.remoteModel

sealed class NetworkResult<T>(
    val data:T? = null,
    //错误信息
    val error:Throwable? = null
){
    class Error<T>(errMsg:Throwable?):NetworkResult<T>(error = errMsg)
    /**正在请求*/
    class Loading<T>:NetworkResult<T>()
    /**请求成功*/
    class Success<T>(data:T):NetworkResult<T>(data)
}
