package com.example.common.di

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.database.db.FoodsDatabaseRepository
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingCardViewModel @Inject constructor(
    private val foodsDatabaseRepository: FoodsDatabaseRepository,
) : ViewModel() {

    val shoppingCard = mutableStateListOf<Food>()

    fun getAllShoppingCartFood(currentUser: User) {
        viewModelScope.launch {
            foodsDatabaseRepository.getAllCartFoods(currentUser.username)
                .collect {
                    shoppingCard.clear()
                    val foods = it.map { m ->
                        m.toFood()
                    }
                    shoppingCard.addAll(foods)
                }
        }
    }

    fun getSelectedFood(seller: User): List<Food> {
        return shoppingCard.filter { it.createUserId == seller.id }
    }

    fun getSellerFoods(seller: User): List<Food> {
        return shoppingCard.filter { it.createUserId == seller.id }
    }

    fun addFoodToShoppingCard(food: Food, currentUser: User) {
        viewModelScope.launch {
            val num = getFoodNumInShoppingCart(food.id) + 1
            foodsDatabaseRepository.addFoodToShoppingCart(food.toLocalFood(currentUser.username).copy(count = num))
        }
    }

    fun removeFoodFromShoppingCard(food: Food, currentUser: User) {
        viewModelScope.launch {
            val num = getFoodNumInShoppingCart(food.id) - 1
            if (num == 0L) {
//                shoppingCard.removeIf { it.id == food.id }
                foodsDatabaseRepository.delete(food.toLocalFood(currentUser.username))
            } else {
                foodsDatabaseRepository.updateFoodFromShoppingCart(food.toLocalFood(currentUser.username).copy(count = num))
            }
        }
    }

    fun clearSellerFoods(seller: User) {
        viewModelScope.launch {
            foodsDatabaseRepository.clearAllShoppingCart(seller.username)
        }
    }

    fun getFoodNumInShoppingCart(id: Long, currentUser: User = User.NONE): Long {
        val cartFood = shoppingCard.firstOrNull { it.id == id }
        return cartFood?.count ?: 0
    }

    fun deleteZeroCount() {
        viewModelScope.launch {
            foodsDatabaseRepository.deleteWhereCountLessThanZero()
        }
    }
}