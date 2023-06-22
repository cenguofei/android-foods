package com.example.sellerdetail

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.common.di.ShoppingCardViewModel
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SellerDetailScreen(
    seller: User,
    foods: List<Food>,
    onBackClick: () -> Unit,
    currentLoginUser: MutableState<User>,
    mainViewModel: ShoppingCardViewModel,
    onSellerSingleFoodClick: (Food) -> Unit = {},
    shouldShowDialogForNav: MutableState<Boolean>,
    shouldStatusBarContentDark:(Boolean) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val selectedFood = mainViewModel.getSelectedFood(seller)

    Log.v("BottomScrollableContent","foods=$foods")
    val sellerDetailViewModel: SellerDetailViewModel = hiltViewModel()
    val categoryFoods = remember(foods) {
        foods.groupBy { it.foodCategory }
    }
    val categories = remember {
        categoryFoods.keys.toList()
    }
    val categoryFoodsList = remember {
        categories.map {
            categoryFoods[it]!!
        }
    }
    FoodsDetailContent(
        seller = seller,
        scaffoldState = scaffoldState,
        selectedFood = selectedFood,
        onBackClick = onBackClick,
        currentLoginUser = currentLoginUser,
        sellerDetailViewModel = sellerDetailViewModel,
        categories = categories,
        categoryFoodsList = categoryFoodsList,
        mainViewModel = mainViewModel,
        onSellerSingleFoodClick = onSellerSingleFoodClick,
        shouldShowDialogForNav = shouldShowDialogForNav,
        shouldStatusBarContentDark = shouldStatusBarContentDark
    )
}

