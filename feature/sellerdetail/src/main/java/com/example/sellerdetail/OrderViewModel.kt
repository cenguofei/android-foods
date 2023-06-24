package com.example.sellerdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import com.example.network.order.CommitOrderImpl
import com.example.network.remote.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


data class InputInfo(
    val address: String,
    val tel:String
)

@HiltViewModel
class SellerDetailViewModel @Inject constructor(
    orderRepository: OrderRepository
) : ViewModel() {

    private val commitOrderImpl: CommitOrderImpl = CommitOrderImpl(orderRepository)

    @RequiresApi(Build.VERSION_CODES.O)
    fun commitOrder(
        selectedFood: List<Food>,
        currentLoginUser: User,
        address: String,
        tel: String,
        onInputError: () -> Unit,
        onCommitSuccess: () -> Unit,
        onCommitError: (msg: String) -> Unit,
        seller: User
    ) {
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

    fun calculatePrice(selectedFood: Map<Food, Int>): Double = commitOrderImpl.calculatePrice(selectedFood)
}