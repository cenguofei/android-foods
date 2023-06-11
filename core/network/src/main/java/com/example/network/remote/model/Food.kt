package com.example.network.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class Food(
    /**
     * 菜品Id
     */
    var id: Int,

    /**
     * 菜品名称
     */
    val foodName: String,

    /**
     * 菜品口味
     */
    val taste: String,

    /**
     * 价格
     */
    val price: Double,

    /**
     * 添加到购物车中的数量
     */
    val count: Int,

    /**
     * 菜的图片
     */
    val foodPic: String,

    /**
     * 菜单类型
     */
    val foodType: String
)
