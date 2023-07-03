package com.example.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.home.widgets.ToTopButton
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlinx.coroutines.launch
import com.example.designsystem.R as designR

object FoodType {

    private val ChineseStyleFastFood = designR.drawable.food1 to "中式快餐"

    private val WesternStyleFastFood = designR.drawable.food2 to "西式快餐"

    private val Drink = designR.drawable.food3 to "饮品"

    private val Noodle = designR.drawable.food4 to "面条"

    private val Sweet = designR.drawable.food5 to "甜点"

    private val Snack = designR.drawable.food6 to "小吃"

    val types = listOf(
        ChineseStyleFastFood,
        WesternStyleFastFood,
        Drink,
        Noodle,
        Sweet,
        Snack
    )
}

/** TODO 嵌套滚动问题
val innerScrollState = rememberLazyStaggeredGridState()

val nestedScrollConnection = remember {
object : NestedScrollConnection {
var consumedY = 0f

override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
val delta = available.y
consumedY += delta
//向上滑动 delta < 0
consumedY = consumedY.coerceAtMost(0f)

val percent = consumedY / totalTopHeightPx


if (percent > -1 && percent < 0) {
Log.v(
"nestedScrollConnection",
"percent > -1 percent = $percent,consumedY = $consumedY, delta = $data"
)
coroutineScope.launch {
outerScrollState.scrollBy(-available.y)
}

return Offset(0F, available.y)
}

return Offset.Zero
}
}
}
 */
@Composable
fun SuccessContent(
    onSearchClick: () -> Unit,
    position: Position = Position.NONE,
    types: List<Pair<Int, String>> = FoodType.types,
    saveFavorite: (food: Food, seller: User) -> Unit,
    deleteFavorite: (food: Food, seller: User) -> Unit,
    onFoodClick: (foods: List<Food>, seller: User) -> Unit,
    favoriteFoodIds: SnapshotStateList<Long>,
    shoppingCard: List<Food>,
    homeViewModel: HomeViewModel,
    onShoppingCartClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val coroutineScope = rememberCoroutineScope()
    val lazyStaggeredState = rememberLazyStaggeredGridState()

    Box(modifier = Modifier.fillMaxSize()) {
        MainSuccessContent(
            coroutineScope = coroutineScope,
            onSearchClick = onSearchClick,
            position = position,
            types = types,
            saveFavorite = saveFavorite,
            deleteFavorite = deleteFavorite,
            onFoodClick = onFoodClick,
            favoriteFoodIds = favoriteFoodIds,
            shoppingCard = shoppingCard,
            lazyStaggeredState = lazyStaggeredState,
            homeViewModel = homeViewModel,
            onShoppingCartClick = onShoppingCartClick,
            onNotificationClick = onNotificationClick,
            onShowSnackbar = onShowSnackbar
        )
        ToTopButton(lazyStaggeredState = lazyStaggeredState, modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 16.dp, end = 8.dp),
            onClick = {
                coroutineScope.launch {
                    lazyStaggeredState.animateScrollToItem(0)
                }
            })
    }
}


