package com.example.favorite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.designsystem.common.ActionsRow
import com.example.designsystem.component.ErrorScreen
import com.example.designsystem.component.FoodsLoadingWheel
import com.example.designsystem.component.FoodsTopAppBar
import com.example.model.remoteModel.NetworkResult
import com.example.model.remoteModel.User

@Composable
fun FavoriteScreen(
    onBack: () -> Unit,
    favoriteViewModel: FavoriteViewModel,
) {
//    LaunchedEffect(key1 = currentUser, block = {
//        favoriteViewModel.getFavorites(currentUser.username)
//    })

    val uiState = favoriteViewModel.myFavorites.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            FoodsTopAppBar(
                onBack = onBack,
            )
        }
        item { ActionsRow(title = "我的喜欢") }

        when (uiState.value) {
            is NetworkResult.Loading -> {
                item {
                    FoodsLoadingWheel(
                        contentDesc = "玩命加载中...",
                        modifier = Modifier.padding(top = 350.dp)
                    )
                }
            }

            is NetworkResult.Success -> {
                favoriteViewModel.favorites.forEach {
                    item(key = it.id) { FoodFavoriteListItem(
                        item = it,
                        onClick = {}
                    ) }
                }
            }

            is NetworkResult.Error -> {
                item { ErrorScreen() }
            }
        }
    }
}
