package com.example.network.order

import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlinx.coroutines.CoroutineScope

interface CommitOrder {

    fun CoroutineScope.commitOrder(
        selectedFood: List<Food>,
        currentLoginUser: User,
        address: String,
        tel: String,
        onInputError: () -> Unit,
        onCommitSuccess: () -> Unit,
        onCommitError: (msg: String) -> Unit,
        seller: User
    )

    fun calculatePrice(selectedFood: Map<Food, Int>): Double {
        if (selectedFood.isEmpty()) {
            return 0.0
        }
        return selectedFood.entries.map { e ->
            val food = e.key
            val foodNum = e.value
            food.price * foodNum
        }.reduce { acc, d ->
            acc + d
        }
    }
}