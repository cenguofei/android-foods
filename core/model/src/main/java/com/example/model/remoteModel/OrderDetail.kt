package com.example.model.remoteModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetail(
    var id:Long = 0,
    var foodName:String, //食物名称
    var price:Double, //价格
    var ordernum:String, //订单号
    var num:Double = 1.0, //食品数量
    var foodPic:String = "",
    var username:String = ""
) : Parcelable
