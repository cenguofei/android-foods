package com.example.network.remote.model

data class OrderDetail(
    var id:Long,
    var foodName:String, //食物名称
    var price:Double, //价格
    var orderNum:String, //订单号
    var num:Int //食品数量
)
