package com.example.model.localmodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.remoteModel.Food

@Entity("food_table")
data class LocalFood(
    /**
     * 菜品Id
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val createUserId:Long = 0,

    /**
     * 菜品名称
     */
    val foodName: String = "",

    /**
     * 描述
     */
    val taste: String = "",

    /**
     * 价格
     */
    val price: Double = 0.00,

    /**
     * 添加到购物车中的数量
     */
    var count: Long = 0,

    /**
     * 菜的图片
     */
    val foodPic: String = "",

    //测试用的
    val foodImg:Int = 0,

    /**
     * 菜单类型
     */
    val foodType: Long = 1,

    val foodCategory:String = "",

    val createTime: String = "",

    val username:String = ""
)  {
    fun toFood() : Food = Food(
        id = id,
        createUserId = createUserId,
        foodName = foodName,
        taste = taste,
        price = price,
        count = count,
        foodPic = foodPic,
        foodImg = foodImg,
        foodType = foodType,
        foodCategory = foodCategory,
        createTime = createTime
    )
}
