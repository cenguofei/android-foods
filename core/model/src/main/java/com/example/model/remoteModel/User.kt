package com.example.model.remoteModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(
    var id:Int = 0,
    var username:String = "",
    var password:String = "",
    var email:String? = null,
    var tel:String? = null, //电话
    var createTime:String? = null,
    var sex:Boolean = false,
    var headImg:String? = null,
    val img:Int = 0,
    val canteenName: String = "",
    val foodType:String = ""
) : Parcelable {


    companion object {
        val NONE = User(username = "Foods For You")
    }
}
