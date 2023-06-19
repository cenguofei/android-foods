package com.example.model.remoteModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Order(
    var id:Long = 0,
    var ordernum:String = System.currentTimeMillis().toString(), //订单号
    var isPay:String = "1", //是否付款
    var createTime:String = "",
    var price:Double,
    var address:String,
    var username:String, //下单人
    var tel:String, //联系电话
    var canteenName:String = "",

    var orderDetailList: List<OrderDetail> = listOf()
) : Parcelable
