package com.example.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.designsystem.component.FoodCard
import com.example.home.widgets.FoodMainTypes
import com.example.home.widgets.FoodsHomeToolBar
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainContent(
    lazyListState: LazyListState,
    coroutineScope: CoroutineScope,
    onSearchClick: () -> Unit,
    position: Position,
    types: List<Pair<Int, String>>,
    saveFavorite: (food: Food, seller: User) -> Unit,
    deleteFavorite: (food: Food, seller: User) -> Unit,
    onFoodClick: (foods: List<Food>, seller: User) -> Unit,
    favoriteFoodIds: SnapshotStateList<Long>,
    data: Map<User, List<Food>>,
    shoppingCard: SnapshotStateMap<Food, Int>
) {
    val map = remember { mutableMapOf<Food, User>() }
    data.entries.forEach { entry -> entry.value.forEach { food -> map[food] = entry.key } }
    val pairs = remember { data.values.flatten().toPairs() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = lazyListState
    ) {
        item {
            FoodsHomeToolBar(position, onSearchClick = onSearchClick, shoppingCard = shoppingCard)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                FoodMainTypes(types, coroutineScope)
            }
        }
        pairs.forEach { pair ->
            item(pair.first.id,pair.second?.id) {
                val firstSeller = map[pair.first]!!
                val firstFoods = data[firstSeller]!!

                val secondSeller = pair.second?.let { map[it] }
                val secondFoods = secondSeller?.let { data[it] }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    secondFoods?.let {
                        FoodCard(
                            modifier = Modifier.weight(1f),
                            seller = secondSeller,
                            food = pair.second!!,
                            onClick = {
                                onFoodClick(secondFoods, secondSeller)
                            },
                            saveFavorite = { food, seller ->
                                saveFavorite(food, seller)
                                favoriteFoodIds.add(food.id)
                            },
                            deleteFavorite = { food, seller ->
                                deleteFavorite(food, seller)
                                favoriteFoodIds.remove(food.id)
                            },
                            isFavoriteFood = pair.second!!.id in favoriteFoodIds
                        )
                    }
                    FoodCard(
                        modifier = Modifier.weight(1f),
                        food = pair.first,
                        seller = firstSeller,
                        saveFavorite = { food, seller ->
                            saveFavorite(food, seller)
                            favoriteFoodIds.add(food.id)
                        },
                        onClick = {
                            onFoodClick(firstFoods, firstSeller)
                        },
                        deleteFavorite = { food, seller ->
                            deleteFavorite(food, seller)
                            favoriteFoodIds.remove(food.id)
                        },
                        isFavoriteFood = pair.first.id in favoriteFoodIds
                    )
                }
            }
        }
    }
}
