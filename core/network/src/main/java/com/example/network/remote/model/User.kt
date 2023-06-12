package com.example.network.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var id:Int = 0,
    var username:String = "",
    var password:String = "",
    var email:String = "",
    var tel:String = "", //电话
    var createTime:String = "",
    var sex:Boolean = false

)
