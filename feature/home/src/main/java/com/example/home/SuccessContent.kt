package com.example.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ChangeHistory
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.CropSquare
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.component.FoodCard
import com.example.designsystem.component.FoodsBackground
import com.example.designsystem.component.FoodsTopAppBar
import com.example.designsystem.component.MyStaggeredVerticalGrid
import com.example.designsystem.component.SearchToolbar
import com.example.designsystem.theme.FoodsTheme
import com.example.designsystem.R as designR
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object FoodType {

    val ChineseStyleFastFood = designR.drawable.food1 to "中式快餐"

    val WesternStyleFastFood = designR.drawable.food2 to "西式快餐"

    val Drink = designR.drawable.food3 to "饮品"

    val Noodle = designR.drawable.food4 to "面条"

    val Sweet = designR.drawable.food5 to "甜点"

    val Snack = designR.drawable.food6 to "小吃"

    val types = listOf(
        ChineseStyleFastFood,
        WesternStyleFastFood,
        Drink,
        Noodle,
        Sweet,
        Snack
    )
}

/*
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
    data: Map<Int, List<Food>>?,
    onSearch: (String) -> Unit,
    searchQuery: String = "今天想吃点什么?",
    position: Position = Position.NONE,
    types: List<Pair<Int, String>> = FoodType.types,
    saveFavorite: (food: Food, seller: User) -> Unit,
    deleteFavorite: (food: Food, seller: User) -> Unit,
    onFoodClick: (food: List<Food>,seller: User) -> Unit,
    viewModel: HomeViewModel
) {
    val outerScrollState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = outerScrollState, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            FoodsHomeToolBar(position)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                SearchToolbar(
                    modifier = Modifier,
                    searchQuery = searchQuery,
                    onLeadingClick = {
                        onSearch(searchQuery)
                    },
                    enabled = false
                )
                FoodTypes(types, coroutineScope)
            }
        }
        item {
            MyStaggeredVerticalGrid(
                maxColumnWidth = 250.dp,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                val lists = data?.values?.shuffled()?.flatten()
                Log.v("cgf", "MyStaggeredVerticalGrid:foods=$lists")
                lists?.forEachIndexed { idx, food ->
                    val padding = if (idx % 2 == 0) 8.dp else 0.dp
                    val seller = viewModel.getSeller(food.createUserId)
                    if (seller != null) {
                        FoodCard(
                            padding = padding,
                            food = food,
                            seller = seller,
                            saveFavorite = saveFavorite,
                            onClick = {
                                onFoodClick(
                                    lists.filter { it.createUserId == food.createUserId },seller
                                )
                            },
                            deleteFavorite = deleteFavorite
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeFoodsCardGrid(
    data: Map<String, List<Food>>?,
    saveFavorite: (food: Food, seller: User) -> Unit,
    deleteFavorite: (food: Food, seller: User) -> Unit,
    nestedScrollConnection: NestedScrollConnection
) {

}

val foodTypesHeight = 125.dp

@Composable
fun FoodTypes(types: List<Pair<Int, String>>, coroutineScope: CoroutineScope) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val visible = remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.Circle,
                        contentDescription = null,
                        modifier = Modifier.size(10.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.CropSquare,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = Icons.Outlined.ChangeHistory,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "热门类别",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            val rotation = remember { Animatable(0f) }
            LaunchedEffect(key1 = visible.value, block = {
                if (visible.value) {
                    coroutineScope.launch {
                        rotation.animateTo(-180f)
                    }
                } else {
                    coroutineScope.launch {
                        rotation.animateTo(0f)
                    }
                }
            })
            IconButton(
                onClick = { visible.value = !visible.value },
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotation.value
                }) {
                Icon(imageVector = Icons.Default.ArrowDownward, contentDescription = null)
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            types.take(3).forEach {
                item { FoodsTypeImage(it) }
            }
        }
        AnimatedVisibility(visible = visible.value) {
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                types.reversed().take(3).forEach {
                    item { FoodsTypeImage(it) }
                }
            }
        }
    }
}

@Composable
fun FoodsTypeImage(foodType: Pair<Int, String>) {
    Box(
        modifier = Modifier
            .height(75.dp)
            .width(125.dp)
            .clip(RoundedCornerShape(16)), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = foodType.first),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )
        Text(
            text = foodType.second,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        )
    }
}


val toolbarHeight = 100.dp

@Composable
fun FoodsHomeToolBar(position: Position) {
    FoodsTopAppBar(
        modifier = Modifier
            .systemBarsPadding()
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        startContent = {
            StartContent(position)
        },
        endContent = {
            Row(modifier = Modifier, horizontalArrangement = Arrangement.End) {
                Box(modifier = Modifier.size(45.dp)) {
                    IconButton(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        onClick = {
                            // TODO 消息通知
                        }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    FoodsMessage()
                }

                Box(
                    modifier = Modifier.size(45.dp)
                ) {
                    IconButton(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center), onClick = {
                            // TODO 购物车
                        }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun StartContent(position: Position) {
    Row(modifier = Modifier, horizontalArrangement = Arrangement.Start) {
        Spacer(
            modifier = Modifier
                .size(width = 4.dp, height = 50.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            val color =
                if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
            Text(text = "你的位置", style = MaterialTheme.typography.labelSmall, color = color)
            Text(
                text = position.city,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = position.zone,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = color
            )
        }
    }
}

@Composable
fun FoodsMessage(
    num: Int = 0
) {

}

@Preview
@Composable
fun PreviewSuccess() {
    FoodsBackground {
        FoodsTheme {
//            SuccessContent(
//                data = mapOf(),
//                onSearch = {},
//                saveFavorite = { food, seller ->
//
//                },
//                deleteFavorite = { food, seller ->
//                },
//                onFoodClick = {food, seller ->
//
//                },
////                viewModel = homeViewModel
//            )
        }
    }
}
