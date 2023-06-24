package com.example.home

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
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
    shoppingCard: SnapshotStateList<Food>,
    homeViewModel: HomeViewModel,
    onShoppingCartClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    val isTypesAllVisible = remember { mutableStateOf(false) }
    val state: CollapsableLayoutState = rememberCollapsableLayoutState(0.dp)
    val foodsTypeLineHeight = remember { Animatable(0f) }
//    state.updateMaxTopHeight(state.maxTopHeightPx + with(LocalDensity.current) { foodsTypeLineHeight.value.dp.roundToPx() })

    CollapsableLayout(
        state = state,
        topContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                FoodsHomeToolBar(
                    position,
                    onSearchClick = onSearchClick,
                    shoppingCard = shoppingCard,
                    modifier = Modifier,
                    onShoppingCartClick = onShoppingCartClick,
                    onNotificationClick = onNotificationClick
                )
                FoodMainTypes(
                    modifier = Modifier,
                    types = types,
                    coroutineScope = coroutineScope,
                    visible = isTypesAllVisible,
                    foodTypesHeight = foodsTypeLineHeight
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
                    items(
                        homeViewModel.showFoods.size,
                        key = { homeViewModel.showFoods[it].second.id }) { index ->
                        val pair = homeViewModel.showFoods[index]
                        val seller = pair.first
                        val food = pair.second
                        FoodCard(
                            seller = seller,
                            food = pair.second,
                            onClick = {
                                val foods = homeViewModel.sellerFoods(seller)
                                Log.v(
                                    "BottomScrollableContent",
                                    "MainSuccessContent categoryFoodsList=$foods"
                                )
                                onFoodClick(foods, seller)
                            },
                            saveFavorite = { _, _ ->
                                saveFavorite(food, seller)
                            },
                            deleteFavorite = { _, _ ->
                                deleteFavorite(food, seller)
                            },
                            isFavoriteFood = food.id in favoriteFoodIds
                        )
                    }
                }
            )
        },
    )
}

