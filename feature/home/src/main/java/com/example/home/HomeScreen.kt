package com.example.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.res.stringResource
import com.example.designsystem.component.ErrorScreen
import com.example.designsystem.component.ShimmerList
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.NetworkResult
import com.example.model.remoteModel.User


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onSellerFoodClick: (food: List<Food>, seller: User) -> Unit = { _, _ -> },
    saveFavorite: (food: Food, seller: User) -> Unit,
    deleteFavorite: (food: Food, seller: User) -> Unit,
    favoriteFoodIds: SnapshotStateList<Long>,
    onSearchClick: () -> Unit,
    shoppingCard: SnapshotStateList<Food>,
) {
    val foods by homeViewModel.homeUiState.collectAsState()

    when(foods) {
        is NetworkResult.Loading -> {
            ShimmerList()
        }
        is NetworkResult.Success<*> ->{
            SuccessContent(
                onSearchClick = onSearchClick,
                saveFavorite = saveFavorite,
                deleteFavorite = deleteFavorite,
                onFoodClick = onSellerFoodClick,
                favoriteFoodIds = favoriteFoodIds,
                shoppingCard = shoppingCard,
                homeViewModel = homeViewModel
            )
        }
        is NetworkResult.Error -> {
            ErrorScreen(foods.error?.cause?.message ?: stringResource(id = R.string.unkown_erro))
        }
    }
}


