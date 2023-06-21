package com.example.common.di

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.example.model.remoteModel.Food
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor() : ViewModel() {

    val shoppingCard = mutableStateMapOf<Food,Int>()

    fun addFoodToShoppingCard(food: Food) {
        shoppingCard
    }

    fun removeFoodFromShoppingCard(food: Food) {
    }
}