package com.example.common.di

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingCardViewModel @Inject constructor() : ViewModel() {

    val shoppingCard = mutableStateMapOf<Food,Int>()


    fun getSelectedFood(seller: User) : List<Food> {
        return shoppingCard.keys.filter { it.createUserId == seller.id }
    }

    fun getSellerFoods(seller:User) : Map<Food,Int> {
        val foods = shoppingCard.keys.filter { it.createUserId == seller.id }
//        val foodIntMap = foods.map {
//            it to shoppingCard[it]!!
//        }.toMap()
        val foodIntMap = foods.associateWith {
            shoppingCard[it]!!
        }
        return foodIntMap
    }

    fun addFoodToShoppingCard(food: Food) {
        val foodCount = shoppingCard[food] ?: 0
        shoppingCard[food] = foodCount + 1
    }

    fun removeFoodFromShoppingCard(food: Food) {
        val f = shoppingCard[food]
        if (f != null && f > 1) {
            shoppingCard[food] = f - 1
        } else {
            shoppingCard -= food
        }
    }

    fun clearSellerFoods(seller: User) {
        shoppingCard.keys.forEach {
            if (it.createUserId == seller.id) {
                shoppingCard -= it
            }
        }
    }
}