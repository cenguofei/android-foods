package com.example.designsystem.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.designsystem.DataProvider
import com.example.designsystem.R
import com.example.designsystem.theme.FoodsTheme
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlin.random.Random

@Composable
fun FoodCard(
    padding: Dp,
    seller: User = DataProvider.user,
    food: Food = DataProvider.Food,
    onClick:() -> Unit,
    saveFavorite: (food: Food, seller: User) -> Unit,
    deleteFavorite: (food: Food, seller: User) -> Unit
) {
    FoodsContainer(modifier = Modifier
        .clickable(onClick = onClick)
        .padding(end = padding, top = 8.dp)) {
        Box(modifier = Modifier) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FoodSpacer()
                SellerRow(seller)
                FoodSpacer()
                FoodImage(food.foodPic)
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = food.foodName,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 5
                    )
                    Text(
                        text = "￥ ${food.price}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.8f
                            )
                        )
                    )
                }
            }
            ScoreIcon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomEnd),
                horizontalArrangement = Arrangement.End
            ) {
                var favorite by remember { mutableStateOf(false) }
                var previousState by remember { mutableStateOf(false) }

                IconButton(
                    onClick = {
                        favorite = !favorite
                    },
                    modifier = Modifier/*.padding(bottom = 4.dp, end = 4.dp)*/
                ) {
                    if (favorite) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        previousState = true
                        saveFavorite(food, seller)
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        if (previousState) {
                            deleteFavorite(food, seller)
                            previousState = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreIcon(modifier: Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(50),
        modifier = modifier.size(25.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = Random.nextInt(0, 9).toString() + ".0",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun FoodImage(foodImg: String) {
    AsyncImage(
        model = foodImg,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        contentScale = ContentScale.Crop,
        onLoading = {
            Log.v("FoodImage_test","onLoading image:$foodImg")
        },
        onError = {
            Log.v("FoodImage_test","onError image:$foodImg")
        },
        onSuccess = {
            Log.v("FoodImage_test","onSuccess image:$foodImg")
        }
    )
}

@Composable
private fun FoodSpacer() {
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun SellerRow(
    seller: User,
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        //TODO 把User的头像换成后端的[showimg]
        Image(
            painter = painterResource(id = R.drawable.default_head_img),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(
                    width = 40.dp,
                    height = 30.dp
                )
                .clip(RoundedCornerShape(16))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "步戏柳州螺狮粉", maxLines = 1, style = MaterialTheme.typography.titleSmall)
    }
}


@Preview
@Composable
private fun FoodCardPreview() {
    FoodsTheme {
        Box(modifier = Modifier.width(width = 200.dp)) {
            FoodCard(
                saveFavorite = { food, seller ->
                },
                deleteFavorite = { food, seller ->

                },
                padding = 8.dp,
                onClick = {}
            )
        }
    }
}
