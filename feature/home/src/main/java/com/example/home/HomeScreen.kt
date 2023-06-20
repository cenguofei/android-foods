package com.example.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.res.stringResource
import com.example.designsystem.component.ErrorScreen
import com.example.designsystem.component.ShimmerList
import com.example.model.remoteModel.Favorite
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.NetworkResult
import com.example.model.remoteModel.User


@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onFoodClick: (food: List<Food>, seller: User) -> Unit = { _, _ -> },
    saveFavorite: (food: Food, seller: User) -> Unit,
    deleteFavorite: (food: Food, seller: User) -> Unit,
    favoriteFoodIds: SnapshotStateList<Long>,
    onSearchClick: () -> Unit,
) {
    val foods by homeViewModel.sellerToFoods.collectAsState()

    when(foods) {
        is NetworkResult.Loading -> {
            ShimmerList()
        }
        is NetworkResult.Success<*> ->{
            SuccessContent(
                data = foods.data!!,
                onSearchClick = onSearchClick,
                saveFavorite = saveFavorite,
                deleteFavorite = deleteFavorite,
                onFoodClick = onFoodClick,
                favoriteFoodIds = favoriteFoodIds
            )

        }
        is NetworkResult.Error -> {
            ErrorScreen(foods.error?.cause?.message ?: stringResource(id = R.string.unkown_erro))
        }
    }
}


