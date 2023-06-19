package com.example.myorder

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.designsystem.common.ActionsRow
import com.example.designsystem.component.ErrorScreen
import com.example.designsystem.component.FoodsTopAppBar
import com.example.designsystem.component.ShimmerList
import com.example.model.remoteModel.NetworkResult
import com.example.model.remoteModel.Order
import com.example.model.remoteModel.User

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyOrderScreen(
    onBack: () -> Unit,
    currentLoginUser: User
) {
    val myOrderViewModel: MyOrderViewModel = hiltViewModel()
    LaunchedEffect(key1 = currentLoginUser, block = {
        myOrderViewModel.setUsername(currentLoginUser.username)
    })
    val netState = myOrderViewModel.userOrders.collectAsState()
    when (netState.value) {
        is NetworkResult.Loading -> {
            ShimmerList()
        }

        is NetworkResult.Success<*> -> {
            val orders = netState.value.data as List<Order>
            MyOrderScreenRoute(
                onBack = onBack,
                orders = orders
            )
        }

        is NetworkResult.Error -> {
            ErrorScreen()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun MyOrderScreenRoute(
    onBack: () -> Unit,
    orders: List<Order>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        item {
            FoodsTopAppBar(
                startContent = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.clickable(onClick = onBack),
                        tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }

        item {
            ActionsRow()
        }

        orders.forEach {
            item {
                MyOrderItem(order = it)
            }
        }
    }
}
