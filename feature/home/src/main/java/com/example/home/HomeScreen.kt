package com.example.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.designsystem.component.ShimmerList
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.NetworkResult
import com.example.model.remoteModel.User


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onShowError:(String) -> Unit,
    saveFavorite: (food: Food, seller: User) -> Unit = {_,_ ->},
    deleteFavorite: (food: Food, seller: User) -> Unit = {_,_ ->},
    onFoodClick:(food:List<Food>,seller:User) -> Unit = {_,_ ->}
) {
    val foods by homeViewModel.foods.collectAsState()

    when(foods) {
        is NetworkResult.Loading -> {
            ShimmerList()
        }
        is NetworkResult.Success<*> ->{
            SuccessContent(
                foods.data,
                viewModel = homeViewModel,
                onSearch = {},
                saveFavorite = saveFavorite,
                deleteFavorite = deleteFavorite,
                onFoodClick = onFoodClick
            )
        }
        is NetworkResult.Error -> {
            onShowError(foods.error?.cause?.message ?: stringResource(id = R.string.unkown_erro))
        }
    }
}


