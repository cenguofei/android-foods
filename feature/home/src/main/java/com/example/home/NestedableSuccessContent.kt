package com.example.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.designsystem.component.FoodCard
import com.example.home.widgets.FoodMainTypes
import com.example.home.widgets.FoodsHomeToolBar
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainSuccessContent(
    lazyStaggeredState: LazyStaggeredGridState,
    coroutineScope: CoroutineScope,
    onSearchClick: () -> Unit,
    position: Position,
    types: List<Pair<Int, String>>,
    saveFavorite: (food: Food, seller: User) -> Unit,
    deleteFavorite: (food: Food, seller: User) -> Unit,
    onFoodClick: (foods: List<Food>, seller: User) -> Unit,
    favoriteFoodIds: SnapshotStateList<Long>,
    shoppingCard: SnapshotStateMap<Food, Int>,
    homeViewModel: HomeViewModel
) {
    val isTypesAllVisible = remember {
        mutableStateOf(false)
    }
    val state: CollapsableLayoutState = rememberCollapsableLayoutState(0.dp)

    CollapsableLayout(
        state = state,
        topContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                FoodsHomeToolBar(
                    position,
                    onSearchClick = onSearchClick,
                    shoppingCard = shoppingCard,
                    modifier = Modifier
                )
                FoodMainTypes(
                    modifier = Modifier,
                    types = types,
                    coroutineScope = coroutineScope,
                    visible = isTypesAllVisible
                )
            }
        },
        bottomContent = {
            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxWidth(),
                columns = StaggeredGridCells.Fixed(2),
                state = lazyStaggeredState,
                content = {
                    items(homeViewModel.showFoods.size,key = { homeViewModel.showFoods[it].second.id }) { index ->
                        val pair = homeViewModel.showFoods[index]
                        val seller = pair.first
                        val food = pair.second
                        FoodCard(
                            seller = seller,
                            food = pair.second,
                            onClick = {
                                onFoodClick(homeViewModel.userToFoodsMap[seller]!!, seller)
                            },
                            saveFavorite = { _, _ ->
                                saveFavorite(food, seller)
                                favoriteFoodIds.add(food.id)
                            },
                            deleteFavorite = { _, _ ->
                                deleteFavorite(food, seller)
                                favoriteFoodIds.remove(food.id)
                            },
                            isFavoriteFood = food.id in favoriteFoodIds
                        )
                    }
                }
            )
        },
        isTypesAllVisible = isTypesAllVisible
    )
}

