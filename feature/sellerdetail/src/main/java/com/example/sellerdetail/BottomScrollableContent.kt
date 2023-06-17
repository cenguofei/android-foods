package com.example.sellerdetail

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CheckboxColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.designsystem.component.FoodsContainer
import com.example.model.remoteModel.Food


@Composable
fun BottomScrollableContent(
    scrollState: ScrollState,
    foods: List<Food>,
    selectedFood: SnapshotStateMap<Food, Int>
) {
    Column(modifier = Modifier.verticalScroll(state = scrollState)) {
        Spacer(modifier = Modifier.height(450.dp))
//        ShuffleButton()
//        DownloadedRow()

        Column(modifier = Modifier/*.horizontalGradientBackground(surfaceGradient)*/) {
            FoodsScrollingSection(foods = foods, selectedFood = selectedFood)
        }
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun FoodsScrollingSection(
    foods: List<Food>,
    selectedFood: SnapshotStateMap<Food, Int>
) {
    foods.forEach {
        FoodsListItem(food = it, Modifier, selectedFood = selectedFood)
    }
}

@Composable
fun FoodsListItem(
    food: Food, modifier: Modifier,
    isSelectedFood: Boolean = false,
    isFoodCancelled: MutableState<Boolean> = mutableStateOf(false),
    selectedFood: SnapshotStateMap<Food, Int>
) {
    FoodsContainer(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp), size = DpSize(0.dp, 80.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f), verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSelectedFood) {
                    Checkbox(
                        checked = isFoodCancelled.value,
                        onCheckedChange = { isFoodCancelled.value = !isFoodCancelled.value },
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                AsyncImage(
                    model = food.foodPic,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.width(75.dp).fillMaxHeight(),
                    onLoading = {
                        Log.v("cgf", "商家详情正在加载item图片：$food")
                    },
                    onError = {
                        Log.v("cgf", "加载图片失败：$food")
                    },
                    onSuccess = {
                        Log.v("cgf", "加载图片成功：$food")
                    }
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(text = food.foodName, style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "￥ ${food.price}", style = MaterialTheme.typography.labelMedium)
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
                        .size(20.dp)
                        .clickable {
                            val numFood = selectedFood[food] ?: 0
                            if (numFood != 0) {
                                selectedFood[food] = numFood - 1
                            }
                        }
                )
                Text(text = (selectedFood[food] ?: 0).toString())
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            val numFood = selectedFood[food] ?: 0
                            selectedFood[food] = numFood + 1
                        }
                )
            }
        }
    }
}


@Composable
fun DownloadedRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Download",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
        )
        var switched by remember { mutableStateOf(true) }

        Switch(
            checked = switched,
            colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(8.dp),
            onCheckedChange = { switched = it }
        )
    }
}

@Composable
fun ShuffleButton() {
    Button(
        onClick = {},
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 100.dp)
            .clip(CircleShape),
    ) {
        Text(
            text = "SHUFFLE PLAY",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}
