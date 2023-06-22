package com.example.sellerdetail

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.example.common.di.ShoppingCardViewModel
import com.example.model.remoteModel.Food

@Composable
fun SellerPager(
    targetState: MutableState<Int>,
    scrollState: MutableState<ScrollState>,
    mainViewModel: ShoppingCardViewModel,
    onSellerSingleFoodClick: (Food) -> Unit = {},
    categoryFoodsList: List<List<Food>>,
) {
    BottomScrollableContent(
        categoryFoodsList = categoryFoodsList,
        targetState = targetState,
        scrollState = scrollState,
        mainViewModel = mainViewModel,
        onSellerSingleFoodClick = onSellerSingleFoodClick,
    )
}