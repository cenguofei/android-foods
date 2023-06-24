package com.example.myorder

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.di.Dispatcher
import com.example.common.di.FoodsDispatchers
import com.example.model.remoteModel.NetworkResult
import com.example.model.remoteModel.Order
import com.example.model.remoteModel.OrderDetail
import com.example.network.remote.repository.OrderRepository
import com.google.gson.internal.LinkedTreeMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyOrderViewModel @Inject constructor(
    private val remoteRepository: OrderRepository,
    @Dispatcher(FoodsDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _userOrders: MutableStateFlow<NetworkResult<List<Order>>> =
        MutableStateFlow(NetworkResult.Loading())
    val userOrders = _userOrders

    private var initialted = false

    companion object {
        const val USER_ORDERS_KEY = "_user_orders_key"
    }

    init {
        _userOrders.value = savedStateHandle.get<NetworkResult<List<Order>>>(USER_ORDERS_KEY)
            ?: NetworkResult.Loading()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getUserOrders(username: String) {
        try {
            viewModelScope.launch(dispatcher) {
                remoteRepository.getUserOrders(username)
                    .catch { _userOrders.emit(NetworkResult.Error(it)) }
                    .collect {
                        Log.v("cgf", "获取订单：$it")
                        val isSuccess = it["isSuccess"] as Boolean
                        if (isSuccess) {
                            val orders = it["myorders"] as List<LinkedTreeMap<*, *>>
                            val mapOrders = orders.map { e ->
                                val orderNum = e["ordernum"] as String
                                val isPay = e["isPay"] as String
                                val createTime = e["createTime"] as String
                                val price = e["price"] as Double
                                val address = e["address"] as String
                                val userName = e["username"] as String
                                val tel = e["tel"] as String
                                val canteenName = e["canteenName"] as String
                                Log.v("cgf","e[orderDetailList]=${e["orderDetailList"]}")
                                val orderDetailsMap = e["orderDetailList"] as List<LinkedTreeMap<*,*>>

                                val orderDetails = orderDetailsMap.mapIndexed { index, linkedTreeMap ->
                                    Log.v("cgf", "$index linkedTreeMap.keys=" + linkedTreeMap.keys)

                                    OrderDetail(
                                        id = (linkedTreeMap["id"] as Double).toLong(),
                                        foodName = linkedTreeMap["foodName"] as String,
                                        price = linkedTreeMap["price"] as Double,
                                        ordernum = linkedTreeMap["ordernum"] as String,
                                        num = linkedTreeMap["num"] as Double,
                                        foodPic = linkedTreeMap["foodPic"] as String,
                                        username = linkedTreeMap["username"] as String
                                    )
                                }
                                Log.v("cgf", "orderDetailsMap=$orderDetailsMap")
                                Log.v("cgf", "orderDetailsMap.kes=${orderDetailsMap}")

                                if (e["orderDetailList"] is List<*>) {
                                    Log.v("cgf","e[\"orderDetailList\"] is List<*>")
                                } else {
                                    Log.v("cgf","e[\"orderDetailList\"] 不是 List<*>")
                                }

//                            val orderDetailList = e["orderDetailList"] as LinkedTreeMap<*, *>

                                Order(
                                    ordernum = orderNum,
                                    isPay = isPay,
                                    createTime = createTime,
                                    username = userName,
                                    price = price,
                                    address = address,
                                    tel = tel,
                                    canteenName = canteenName,
                                    orderDetailList = orderDetails
                                ).also {
                                    Log.v("cgf", "order=$it")
                                }
                            }
                            _userOrders.emit(NetworkResult.Success(mapOrders))
                        } else {
                            val msg = it["msg"] as String
                            _userOrders.emit(NetworkResult.Error(Error(msg)))
                        }
                    }
            }
        } catch (e:Exception) {
            e.printStackTrace()
            viewModelScope.launch {
                _userOrders.emit(NetworkResult.Error(e))
            }
        }

    }

    fun setUsername(username: String) {
        if (!initialted) {
            getUserOrders(username)
            initialted = true
        }
    }
}