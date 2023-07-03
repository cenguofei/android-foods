package com.example.sellerdetail

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.common.di.ShoppingCardViewModel
import com.example.designsystem.R
import com.example.designsystem.common.AddAndRemoveFood
import com.example.designsystem.component.FoodsContainer
import com.example.designsystem.theme.LocalTintTheme
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User


@SuppressLint("AutoboxingStateCreation")
@Composable
fun SellerPager(
    targetState: MutableState<Int>,
    scrollState: MutableState<ScrollState>,
    mainViewModel: ShoppingCardViewModel,
    onSellerSingleFoodClick: (Food) -> Unit = {},
    categoryFoodsList: List<List<Food>>,
    currentLoginUser: User,
    selectedFood: List<Food>,
) {
    Crossfade(targetState = targetState.value) { pageIndex ->
        if (pageIndex < categoryFoodsList.size) {
            val foods = categoryFoodsList[pageIndex]
            FoodsScrollingSection(
                foods = foods,
                scrollState = scrollState,
                mainViewModel = mainViewModel,
                onSellerSingleFoodClick = onSellerSingleFoodClick,
                currentLoginUser = currentLoginUser,
                selectedFood = selectedFood
            )
        }
    }
}

@Composable
fun FoodsScrollingSection(
    foods: List<Food>,
    scrollState: MutableState<ScrollState>,
    mainViewModel: ShoppingCardViewModel,
    onSellerSingleFoodClick: (Food) -> Unit = {},
    currentLoginUser: User,
    selectedFood: List<Food>
) {
    scrollState.value = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState.value)
    ) {
        Spacer(modifier = Modifier.height(400.dp))
        foods.forEach {
            FoodsListItem(
                selectedFood = selectedFood,
                food = it,
                Modifier,
                mainViewModel = mainViewModel,
                currentLoginUser,
            ) {
                onSellerSingleFoodClick(it)
            }

        }
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun FoodsListItem(
    selectedFood: List<Food>,
    food: Food, modifier: Modifier,
    mainViewModel: ShoppingCardViewModel,
    currentLoginUser: User,
    onClick: () -> Unit = {}
) {
    FoodsContainer(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp), size = DpSize(0.dp, 110.dp),
        onClick = onClick,
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f), verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = food.foodPic,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(100.dp)
                        .fillMaxHeight(),
                    onLoading = {
                        Log.v("cgf", "商家详情正在加载item图片：$food")
                    },
                    onError = {
                        Log.v("cgf", "加载图片失败：$food")
                    },
                    onSuccess = {
                        Log.v("cgf", "加载图片成功：$food")
                    },
                    placeholder = painterResource(id = R.drawable.food3),
                    error = painterResource(id = R.drawable.food11),
                    alignment = Alignment.Center
                )
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = food.foodName,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = food.taste,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(vertical = 4.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "￥ ${food.price}",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            CompositionLocalProvider(
                LocalTintTheme provides
                        LocalTintTheme.current.copy(iconTint = MaterialTheme.colorScheme.primary)
            ) {
                AddAndRemoveFood(
                    modifier = Modifier
                        .align(Alignment.Bottom)
                        .padding(end = 4.dp, bottom = 4.dp),
                    num = mainViewModel.getFoodNumInShoppingCart(selectedFood, food.id),
                    onAdd = {
                        mainViewModel.addFoodToShoppingCard(selectedFood, food, currentLoginUser)
                    },
                    onRemove = {
                        mainViewModel.removeFoodFromShoppingCard(
                            selectedFood,
                            food,
                            currentLoginUser
                        )
                    }
                )
            }
        }
    }
}
