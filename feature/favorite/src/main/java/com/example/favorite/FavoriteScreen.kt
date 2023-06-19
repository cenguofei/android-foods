package com.example.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    currentUser: User,
    favoriteViewModel: FavoriteViewModel,
) {
    LaunchedEffect(key1 = currentUser, block = {
        favoriteViewModel.getFavorites(currentUser.username)
    })

    val uiState = favoriteViewModel.myFavorites.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            FoodsTopAppBar(
                startContent = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.clickable(onClick = onBack),
                        tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
        item { ActionsRow() }

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
                uiState.value.data?.forEach {
                    item { FavoriteItem(favorite = it) }
                }
            }

            is NetworkResult.Error -> {
                item { ErrorScreen() }
            }
        }
    }
}
