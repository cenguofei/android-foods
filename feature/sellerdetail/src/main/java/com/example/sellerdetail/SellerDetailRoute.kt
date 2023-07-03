package com.example.sellerdetail

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.common.di.ShoppingCardViewModel
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlinx.coroutines.CoroutineScope


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SellerDetailScreen(
    seller: User,
    foods: List<Food>,
    onBackClick: () -> Unit,
    currentLoginUser: User,
    mainViewModel: ShoppingCardViewModel,
    onSellerSingleFoodClick: (Food) -> Unit = {},
    shouldShowDialogForNav: MutableState<Boolean>,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    coroutineScope: CoroutineScope,
    shouldStatusBarContentDark: (Boolean) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val shoppingCart =
        mainViewModel.getAllShoppingCartFood(currentLoginUser).collectAsState(listOf())
    val selectedFood = shoppingCart.value.filter { it.createUserId == seller.id }.map { it.toFood() }
    Log.v("ShoppingCardViewModel","collect:$selectedFood")

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
        shouldStatusBarContentDark = shouldStatusBarContentDark,
        onShowSnackbar = onShowSnackbar,
        coroutineScope = coroutineScope
    )
}

