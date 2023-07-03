package com.example.shoppingcart

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.common.di.ShoppingCardViewModel
import com.example.designsystem.common.UserInfoDialog
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShoppingCartScreen(
    onBack: () -> Unit,
    shoppingCardViewModel: ShoppingCardViewModel,
    currentLoginUser: User,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val viewModel: CartViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        CartTopBar(onBack)

        val shoppingCard = shoppingCardViewModel.getAllShoppingCartFood(currentLoginUser).collectAsState(
            initial = listOf()
        )
        viewModel.setSellersFoods(
            shoppingCard.value.map { it.toFood() },
            shoppingCardViewModel.shoppingCartUsers
        )

        var address by remember { mutableStateOf(TextFieldValue("")) }
        var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }

        var shouldShowDialog by remember { mutableStateOf(false) }
        var hasError by remember { mutableStateOf(false) }

        var sellerToConsume by remember { mutableStateOf(User.NONE) }

        if (shouldShowDialog) {
            UserInfoDialog(
                onDismiss = { shouldShowDialog = false },
                hasError = hasError,
                address = address,
                onAddressChanged = {
                    address = it
                    hasError = false
                },
                phone = phoneNumber,
                onPhoneChanged = {
                    phoneNumber = it
                    hasError = false
                },
                onCommitOrder = {
                    if (address.text.isBlank() || phoneNumber.text.isBlank()) {
                        hasError = true
                    } else {
                        if (sellerToConsume != User.NONE) {
                            viewModel.commitOrder(
                                seller = sellerToConsume,
                                currentLoginUser = currentLoginUser,
                                address = address.text,
                                tel = phoneNumber.text,
                                onInputError = {
                                    hasError = true
                                },
                                onCommitSuccess = {
                                    shouldShowDialog = false
                                    Log.v("commitOrder", "提交成功")
                                    coroutineScope.launch {
                                        onShowSnackbar("提交成功","Remove")
                                    }
                                },
                                onCommitError = {
                                    shouldShowDialog = false
                                    coroutineScope.launch {
                                        onShowSnackbar("提交失败","Remove")
                                    }
                                    Log.v("commitOrder", "提交失败：$it")
                                }
                            )
                        }
                    }
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            viewModel.sellerWithSelectedFood.forEach { entry ->
                item {
                    SellerFoodsItem(
                        foods = entry.value,
                        seller = entry.key,
                        viewModel = viewModel,
                        onSettleAccount = {
                            sellerToConsume = it
                            viewModel.shouldShowDialog(
                                seller = entry.key
                            ) { show ->
                                shouldShowDialog = show
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CommitOrderDialog() {

}