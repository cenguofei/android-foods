package com.example.sellerdetail

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SellerDetailRoute(
    seller: User,
    foods: List<Food>,
    onBackClick: () -> Unit,
    currentLoginUser: MutableState<User>
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val selectedFood = remember { mutableStateMapOf<Food, Int>() }
        FoodsDetailScreen(
            seller = seller,
            foods = foods,
            scaffoldState = scaffoldState,
            selectedFood = selectedFood,
            onBackClick = onBackClick,
            currentLoginUser = currentLoginUser
        )
}

