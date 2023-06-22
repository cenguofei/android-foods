package com.example.sellerdetail

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.di.Dispatcher
import com.example.common.di.FoodsDispatchers
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.Order
import com.example.model.remoteModel.OrderDetail
import com.example.model.remoteModel.User
import com.example.network.remote.repository.FoodRepository
import com.example.network.remote.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject


data class InputInfo(
    val address: String,
    val tel:String
)

@HiltViewModel
class SellerDetailViewModel @Inject constructor(
    private val remoteRepository: OrderRepository,
    @Dispatcher(FoodsDispatchers.IO) private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun commitOrder(
        selectedFood: Map<Food, Int>,
        currentLoginUser: MutableState<User>,
        address: String,
        tel: String,
        onInputError: () -> Unit,
        onCommitSuccess: () -> Unit,
        onCommitError: (msg: String) -> Unit,
        seller: User
    ) {
        if (address.isEmpty()) {
            onInputError()
            return
        }

        val orderNum = System.currentTimeMillis().toString()

        val foodsDetails = selectedFood.keys.map { food: Food ->
            OrderDetail(
                foodName = food.foodName,
                price = food.price * (selectedFood[food]?.toDouble() ?: 0.0),
                ordernum = orderNum,
                foodPic = food.foodPic,
                num = selectedFood[food]?.toDouble() ?: 0.0,
                username = currentLoginUser.value.username
            )
        }

        val totalPrice = calculatePrice(selectedFood) + selectedFood.size * 1 + 0.5
        val order = Order(
            price = totalPrice,
            address = address,
            username = currentLoginUser.value.username,
            orderDetailList = foodsDetails,
            ordernum = orderNum,
            tel = tel,
            canteenName = seller.canteenName,
        )

        Log.v("cgf", "提交订单：$order")

        viewModelScope.launch(dispatcher) {
            val postOrder: HashMap<String, Any> = remoteRepository.postOrder(order)
            postOrder.string()

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

    private fun <K, V> Map<K, V>.string() {
        val builder = StringBuilder()
        builder.append("{")
        keys.forEach {
            val item = get(it)
            builder.append(it).append(":").append(item).append(",")
        }
        builder.append("}")
        Log.v("post_order_test", builder.toString())
    }
}