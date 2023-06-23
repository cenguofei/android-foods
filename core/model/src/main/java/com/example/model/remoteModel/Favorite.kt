package com.example.model.remoteModel


data class Favorite(

    val id:Long = 0,

    val foodId:Long = 0, //喜欢的Food的id

    val username:String = "", //客户端登录用户

    val sellerId:Long = 0,

    val sellerPic:String = "", //商家图片

    val canteenName:String = "", //餐厅名

    val foodName:String = "",

    val score:Double = 0.0, //商家评分

    val foodType:String = "", //商家经营食物类型

    val foodPic: String = ""
)
