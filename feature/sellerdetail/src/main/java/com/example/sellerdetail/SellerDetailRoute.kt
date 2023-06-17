package com.example.sellerdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SellerDetailRoute(seller: User, foods: List<Food>) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val selectedFood = remember { mutableStateMapOf<Food,Int>() }


    Box {
        FoodsDetailScreen(
            seller = seller,
            foods = foods,
            scaffoldState = scaffoldState,
            selectedFood = selectedFood
        )
        FoodsFAB(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 4.dp),
            scaffoldState = scaffoldState,
            coroutineScope = coroutineScope,
            onCommitClick = {},
            size = selectedFood.size
        )
        Image(
            painter = painterResource(id = R.drawable.bottom_kid),
            modifier = Modifier
                .align(
                    Alignment.BottomStart
                )
                .size(height = 120.dp, width = 100.dp),
            contentDescription = null
        )
    }
}

