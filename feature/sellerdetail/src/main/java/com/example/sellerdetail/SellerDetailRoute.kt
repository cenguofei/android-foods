package com.example.sellerdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.common.di.MainViewModel
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SellerDetailRoute(
    seller: User,
    foods: List<Food>,
    onBackClick: () -> Unit,
    currentLoginUser: MutableState<User>,
    mainViewModel: MainViewModel
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val selectedFood = remember { mutableStateMapOf<Food, Int>() }
    val sellerDetailViewModel: SellerDetailViewModel = hiltViewModel()
    val categoryFoods = remember { foods.groupBy { it.foodCategory } }

    FoodsDetailScreen(
        seller = seller,
        scaffoldState = scaffoldState,
        selectedFood = selectedFood,
        onBackClick = onBackClick,
        currentLoginUser = currentLoginUser,
        sellerDetailViewModel = sellerDetailViewModel,
        categoryFoods = categoryFoods,
        mainViewModel = mainViewModel
    )
}

