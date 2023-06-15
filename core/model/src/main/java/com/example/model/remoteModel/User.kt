package com.example.model.remoteModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    var id:Int = 0,
    var username:String = "",
    var password:String = "",
    var email:String = "",
    var tel:String = "", //电话
    var createTime:String = "",
    var sex:Boolean = false,
    var headImg:String = "",
    val img:Int = 0,
) : Parcelable {
    companion object {
        val NONE = User(username = "Foods For You")
    }
}
