package com.example.home

data class Position(
    val province:String,
    val city:String,
    val zone:String //区
) {

    companion object {
        val NONE = Position(province = "重庆市", city = "重庆市", zone = "西南大学(荣昌校区)")
    }
}
