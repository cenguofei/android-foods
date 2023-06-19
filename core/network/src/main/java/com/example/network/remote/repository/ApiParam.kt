package com.example.network.remote.repository

interface ApiParam {

    companion object {
        const val BASE_URL = "http://10.129.67.213:80/"

        const val IMAGE_USER_URL = BASE_URL + "static/upload/"
        const val IMAGE_FOOD_URL = BASE_URL + "food/showimg/"
    }
}