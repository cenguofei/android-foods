package com.example.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.designsystem.component.ShimmerList
import com.example.network.remote.remoteModel.NetworkResult

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onShowError:(String) -> Unit
) {
    val foods by homeViewModel.foods.collectAsState()


    when(foods) {
        is NetworkResult.Loading -> {
            ShimmerList()
        }
        is NetworkResult.Success<*> ->{
            SuccessContent(foods.data, onSearch = {})
        }
        is NetworkResult.Error -> {
            onShowError(foods.error?.cause?.message ?: stringResource(id = R.string.unkown_erro))
        }
    }
}


