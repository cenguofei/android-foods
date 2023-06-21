package com.example.sellerdetail

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.common.di.MainViewModel
import com.example.designsystem.R
import com.example.designsystem.component.FoodsContainer
import com.example.model.remoteModel.Food


@SuppressLint("AutoboxingStateCreation")
@Composable
fun BottomScrollableContent(
    selectedFood: SnapshotStateMap<Food, Int>,
    categoryFoods: Map<String, List<Food>>,
    targetState: MutableState<Int>,
    scrollState: MutableState<ScrollState>,
    mainViewModel: MainViewModel
) {
    Crossfade(targetState = targetState.value) { pageIndex ->
        val values: List<List<Food>> = categoryFoods.values.toList()
        val foods = values[pageIndex]
        FoodsScrollingSection(
            foods = foods,
            selectedFood = selectedFood,
            scrollState = scrollState,
            mainViewModel = mainViewModel
        )
    }
}

@Composable
fun FoodsScrollingSection(
    foods: List<Food>,
    selectedFood: SnapshotStateMap<Food, Int>,
    scrollState: MutableState<ScrollState>,
    mainViewModel: MainViewModel
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
                food = it,
                Modifier,
                selectedFood = selectedFood,
                mainViewModel = mainViewModel
            )
        }
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun FoodsListItem(
    food: Food, modifier: Modifier,
    selectedFood: SnapshotStateMap<Food, Int>,
    mainViewModel: MainViewModel
) {
    FoodsContainer(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp), size = DpSize(0.dp, 100.dp)
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
                Column(modifier = Modifier.padding(start = 8.dp), verticalArrangement = Arrangement.SpaceBetween) {
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
                        maxLines = 5,
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(end = 4.dp, bottom = 4.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(8.dp)
                        .clickable {
                            val numFood = selectedFood[food] ?: 0
                            if (numFood != 0) {
                                selectedFood[food] = numFood - 1
                            } else {
                                selectedFood.remove(food)

                            }
                        }
                )
                Text(text = (selectedFood[food] ?: 0).toString())
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(8.dp)
                        .clickable {
                            val numFood = selectedFood[food] ?: 0
                            selectedFood[food] = numFood + 1
                        }
                )
            }
        }
    }
}
