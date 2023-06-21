package com.example.sellerdetail

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.example.common.di.MainViewModel
import com.example.model.remoteModel.Food

@Composable
fun SellerPager(
    selectedFood: SnapshotStateMap<Food, Int>,
    categoryFoods: Map<String, List<Food>>,
    targetState: MutableState<Int>,
    scrollState: MutableState<ScrollState>,
    mainViewModel: MainViewModel
) {
    BottomScrollableContent(
        categoryFoods = categoryFoods,
        selectedFood = selectedFood,
        targetState = targetState,
        scrollState = scrollState,
        mainViewModel = mainViewModel
    )
}