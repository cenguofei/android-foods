package com.example.model.remoteModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.util.Random


@Parcelize
data class User(
    var id:Long = 0,
    var username:String = "",
    var password:String = "",
    var email:String = "",
    var tel:String = "", //电话
//    var createTime:Date? = null,
    var sex:Boolean = false,
    var headImg:String = "",
    val img:Int = 0, //测试用
    val canteenName: String = "",
    val foodType:String = ""
) : Parcelable {

    /**
     * //TODO BUG
     * java.lang.NullPointerException: Attempt to invoke virtual method 'int java.lang.String.hashCode()' on a null object reference
     * 使用对象做map的key时需要重写hashCode,equals，不然有些场景会报错
     */
    override fun hashCode(): Int {
        return super.hashCode() + id.toInt() % 10000
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (email != other.email) return false
        if (tel != other.tel) return false
        if (sex != other.sex) return false
        if (headImg != other.headImg) return false
        if (img != other.img) return false
        if (canteenName != other.canteenName) return false
        if (foodType != other.foodType) return false

        return true
    }

    companion object {
        val NONE = User(username = "Foods For You")
    }
}
