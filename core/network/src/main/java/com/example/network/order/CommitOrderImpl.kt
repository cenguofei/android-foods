package com.example.network.order

import android.util.Log
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.Order
import com.example.model.remoteModel.OrderDetail
import com.example.model.remoteModel.User
import com.example.network.remote.repository.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CommitOrderImpl constructor(
    private val orderRepository: OrderRepository
)  {
    fun CoroutineScope.commitOrder(
        selectedFood: List<Food>,
        currentLoginUser: User,
        address: String,
        tel: String,
        onInputError: () -> Unit,
        onCommitSuccess: () -> Unit,
        onCommitError: (msg: String) -> Unit,
        seller: User
    ) {
        if (address.isBlank() || tel.isBlank()) {
            onInputError()
            return
        }
        val orderNum = System.currentTimeMillis().toString()

        val foodsDetails = selectedFood.map { food: Food ->
            OrderDetail(
                foodName = food.foodName,
                price = food.price * food.count.toInt(),
                ordernum = orderNum,
                foodPic = food.foodPic,
                num = food.count.toDouble(),
                username = currentLoginUser.username
            )
        }

        val map = selectedFood.associateWith {
            it.count.toInt()
        }
        val totalPrice = calculatePrice(map) + selectedFood.size * 1 + 0.5
        val order = Order(
            price = totalPrice,
            address = address,
            username = currentLoginUser.username,
            orderDetailList = foodsDetails,
            ordernum = orderNum,
            tel = tel,
            canteenName = seller.canteenName,
        )
        Log.v("cgf", "提交订单：$order")

        launch {
            val postOrder: HashMap<String, Any> = orderRepository.postOrder(order)
            try {
                val isSuccess = postOrder["isSuccess"] as Boolean
                if (isSuccess) {
                    onCommitSuccess()
                } else {
                    val msg = postOrder["msg"] as String
                    onCommitError(msg)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onCommitError(e.message ?: "unknown error")
            }
        }
    }

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