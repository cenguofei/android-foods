package com.example.model.remoteModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Order(
    var id:Long,
    var orderNum:String, //订单号
    var isPay:String, //是否付款
    var createTime:Date?,
    var price:Double,
    var address:String,
    var username:String, //下单人
    var tel:String, //联系电话

    var orderDetailList: List<OrderDetail>
) : Parcelable
