package com.example.shoppingcart

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import com.example.network.order.CommitOrderImpl
import com.example.network.remote.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    orderRepository: OrderRepository
) : ViewModel() {

    private val commitOrderImpl: CommitOrderImpl = CommitOrderImpl(orderRepository)
    /**
     * 初始的时候全部选中
     */
    var sellerWithSelectedFood = mutableStateMapOf<User, MutableList<Food>>()

    val sellerWithSelectedFoodIds = mutableStateListOf<Long>()

//    var isFirstSetSellersFoods = false
    fun setSellersFoods(
        shoppingCard: List<Food>,
        shoppingCartUsers: SnapshotStateList<User>
    ) {
        Log.v("setSellersFoods","shoppingCard = $shoppingCard")
        Log.v("setSellersFoods","shoppingCartUsers = ${shoppingCartUsers.toList()}")
//        if (isFirstSetSellersFoods) return
//        isFirstSetSellersFoods = true

        val map = shoppingCartUsers.map { user ->
            user to shoppingCard.filter { food ->
                user.id == food.createUserId
            }
        }.filter { it.second.isNotEmpty() }/*.distinctBy { it.first.id }*/

        val map1 = map.map {
            it.first to it.second.toMutableList()
        }
        sellerWithSelectedFood.putAll(map1)
        map1.forEach {
            it.second.forEach { food ->
                sellerWithSelectedFoodIds.add(food.id)
            }
        }
    }

    fun clearIds(ids: List<Long>) {
        sellerWithSelectedFoodIds.removeAll(ids)
    }

    fun addIds(ids: List<Long>) {
        sellerWithSelectedFoodIds.addAll(ids)
    }

    fun shouldShowDialog(
        seller: User,
        onShow:(Boolean) -> Unit
    ) {
        val sellerFoods = sellerWithSelectedFood[seller]
        if (sellerFoods.isNullOrEmpty()) {
            Log.v("commitOrder","出错了，商家没有food")
            onShow(false)
            return
        }
        val selectedFood = sellerFoods.filter { it.id in sellerWithSelectedFoodIds }
        if (selectedFood.isEmpty()) {
            Log.v("commitOrder","没有选择商品")
            onShow(false)
            return
        }
        Log.v("commitOrder","填写选中foods的地址信息：selectedFood:$selectedFood")
        onShow(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun commitOrder(
        currentLoginUser: User,
        address: String,
        tel: String,
        onInputError: () -> Unit,
        onCommitSuccess: () -> Unit,
        onCommitError: (msg: String) -> Unit,
        seller: User
    ) {
        val sellerFoods = sellerWithSelectedFood[seller]
        val selectedFood = sellerFoods?.filter { it.id in sellerWithSelectedFoodIds }
        if (selectedFood.isNullOrEmpty()) {
            onCommitError("出错啦~")
            return
        }

        with(commitOrderImpl) {
            viewModelScope.commitOrder(
                selectedFood = selectedFood,
                currentLoginUser = currentLoginUser,
                address = address,
                tel = tel,
                onInputError = onInputError,
                onCommitSuccess = onCommitSuccess,
                onCommitError = onCommitError,
                seller = seller
            )
        }
    }
}