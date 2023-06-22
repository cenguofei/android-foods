package com.example.fooddetail

import androidx.lifecycle.ViewModel
import com.example.model.remoteModel.Order
import com.example.network.remote.repository.OrderRepository
import javax.inject.Inject

class FoodDetailViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    fun postOrder(order: Order) {

    }
}