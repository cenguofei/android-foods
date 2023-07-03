package com.example.common.di

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.database.db.FoodsDatabaseRepository
import com.example.model.localmodel.LocalFood
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingCardViewModel @Inject constructor(
    private val foodsDatabaseRepository: FoodsDatabaseRepository,
) : ViewModel() {

    fun getAllShoppingCartFood(currentUser: User): Flow<List<LocalFood>> {
//        viewModelScope.launch {
//            foodsDatabaseRepository.getAllCartFoods(currentUser.username)
//                .collect {
//                    Log.v("ShoppingCardViewModel","collect:$it")
//                    shoppingCard.clear()
//                    val foods = it.map { m ->
//                        m.toFood()
//                    }
//                    shoppingCard.addAll(foods)
//                }
//        }
        return foodsDatabaseRepository.getAllCartFoods(currentUser.username)
    }
    fun addFoodToShoppingCard(selectedFood: List<Food>, food: Food, currentUser: User) {
        viewModelScope.launch {
            val num = getFoodNumInShoppingCart(selectedFood, food.id) + 1
            if (num == 1L) {
                foodsDatabaseRepository.addFoodToShoppingCart(
                    food.toLocalFood(currentUser.username)
                        .copy(count = num).also {
                            Log.v("ShoppingCardViewModel","add $it")
                        }
                )
            } else {
                foodsDatabaseRepository.updateFoodFromShoppingCart(
                    food.toLocalFood(currentUser.username)
                        .copy(count = num).also {
                        Log.v("ShoppingCardViewModel","update $it")
                    }
                )
            }
        }
    }

    fun removeFoodFromShoppingCard(selectedFood: List<Food>, food: Food, currentUser: User) {
        viewModelScope.launch {
            val num = getFoodNumInShoppingCart(selectedFood,food.id) - 1
            if (num < 0) return@launch

            if (num == 0L) {
//                shoppingCard.removeIf { it.id == food.id }
                foodsDatabaseRepository.delete(
                    food.toLocalFood(currentUser.username).also {
                        Log.v("ShoppingCardViewModel","delete $it")
                    }
                )
            } else {
                foodsDatabaseRepository.updateFoodFromShoppingCart(
                    food.toLocalFood(currentUser.username).copy(count = num).also {
                        Log.v("ShoppingCardViewModel","delete update $it")
                    }
                )
            }
        }
    }

    fun clearSellerFoods(seller: User, currentLoginUser: User) {
        viewModelScope.launch {
            foodsDatabaseRepository.clearAllShoppingCart(currentLoginUser.username,seller.id)
        }
    }

    fun getFoodNumInShoppingCart(selectedFood: List<Food>, id: Long): Long {
        val cartFood = selectedFood.firstOrNull { it.id == id }
        return cartFood?.count ?: 0
    }

    fun deleteZeroCount() {
        viewModelScope.launch {
            foodsDatabaseRepository.deleteWhereCountLessThanZero()
        }
    }

    val shoppingCartUsers = mutableStateListOf<User>()
    fun setUsers(users: List<User>) {
        shoppingCartUsers.addAll(users.distinctBy { it.id })
    }
}