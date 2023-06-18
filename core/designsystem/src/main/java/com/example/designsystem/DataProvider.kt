package com.example.designsystem

import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlin.random.Random

object DataProvider {

    val foods = listOf(
        Food(
            id = Random.nextLong(),
            foodName = "鸡蛋番茄面",
            taste = "清爽",
            price = 18.0,
            count = 1,
            foodPic = R.drawable.food1.toString(),
            foodType = 1,
            foodImg = R.drawable.food1
        ),
        Food(
            id = Random.nextLong(),
            foodName = "桑葚",
            taste = "甜",
            price = 18.0,
            count = 1,
            foodPic = R.drawable.food2.toString(),
            foodType = 1,
            foodImg = R.drawable.food2
        ),
        Food(
            id = Random.nextLong(),
            foodName = "桑葚",
            taste = "清爽",
            price = 18.0,
            count = 1,
            foodPic = R.drawable.food3.toString(),
            foodType = 2,
            foodImg = R.drawable.food3
        ),
        Food(
            id = Random.nextLong(),
            foodName = "桑葚",
            taste = "甜",
            price = 18.0,
            count = 1,
            foodPic = R.drawable.food4.toString(),
            foodType = 1,
            foodImg = R.drawable.food4
        ),
        Food(
            id = Random.nextLong(),
            foodName = "桑葚",
            taste = "甜",
            price = 18.0,
            count = 1,
            foodPic = R.drawable.food5.toString(),
            foodType = 3,
            foodImg = R.drawable.food5
        ),
        Food(
            id = Random.nextLong(),
            foodName = "桑葚",
            taste = "甜",
            price = 18.0,
            count = 1,
            foodPic = R.drawable.food6.toString(),
            foodType = 3,
            foodImg = R.drawable.food6
        ),
        Food(
            id = Random.nextLong(),
            foodName = "桑葚",
            taste = "甜",
            price = 18.0,
            count = 1,
            foodPic = R.drawable.food7.toString(),
            foodType = 3,
            foodImg = R.drawable.food7
        ),
        Food(
            id = Random.nextLong(),
            foodName = "桑葚",
            taste = "甜",
            price = 18.0,
            count = 1,
            foodPic = R.drawable.food8.toString(),
            foodType = 4,
            foodImg = R.drawable.food8
        ),
        Food(
            id = Random.nextLong(),
            foodName = "桑葚",
            taste = "甜",
            price = 18.0,
            count = 1,
            foodPic = R.drawable.food9.toString(),
            foodType = 4,
            foodImg = R.drawable.food9
        ),
        Food(
            id = Random.nextLong(),
            foodName = "桑葚",
            taste = "甜",
            price = 18.0,
            count = 1,
            foodPic = R.drawable.food10.toString(),
            foodType = 4,
            foodImg = R.drawable.food10
        ),
        Food(
            id = Random.nextLong(),
            foodName = "桑葚",
            taste = "甜",
            price = 18.0,
            count = 1,
            foodPic = R.drawable.food11.toString(),
            foodType = 4,
            foodImg = R.drawable.food11
        ),
        Food(
            id = Random.nextLong(),
            foodName = "桑葚",
            taste = "甜",
            price = 18.0,
            count = 1,
            foodPic = R.drawable.food12.toString(),
            foodType = 5,
            foodImg = R.drawable.food12
        ),
        Food(
            id = Random.nextLong(),
            foodName = "桑葚",
            taste = "甜",
            price = 18.0,
            count = 1,
            foodPic = R.drawable.food13.toString(),
            foodType = 1,
            foodImg = R.drawable.food13
        ),
    )

    val foodIds = foods.map { it.foodImg }

    val Food = Food(
        id = Random.nextLong(),
        foodName = "冰淇淋",
        taste = "甜",
        price = 3.0,
        count = 1,
        foodPic = R.drawable.food13.toString(),
        foodType = 1,
        foodImg = R.drawable.food1
    )

    val user = User(
        username = "茶颜悦色",
        tel = "11100089756",
//        createTime = "2020-05-15 13:49:07",
        email = "email@swu.com",
        headImg = R.drawable.p9.toString(),
        img = R.drawable.p9
    )
}