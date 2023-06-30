package com.example.designsystem.component

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.example.designsystem.DataProvider
import com.example.designsystem.R
import com.example.designsystem.common.StarButton
import com.example.designsystem.generateDominantColorState
import com.example.designsystem.theme.FoodsTheme
import com.example.designsystem.verticalGradientBackground
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@SuppressLint("AutoboxingStateCreation")
@Composable
fun FoodCard(
    modifier: Modifier = Modifier,
    seller: User = DataProvider.user,
    food: Food = DataProvider.Food,
    onClick: () -> Unit,
    saveFavorite: (food: Food, seller: User) -> Unit,
    deleteFavorite: (food: Food, seller: User) -> Unit,
    isFavoriteFood: Boolean
) {
    val rgb = remember { mutableStateOf(0) }
    val resource = LocalContext.current.resources
    val bitmap = remember {
        mutableStateOf(
            BitmapFactory.decodeResource(
                resource,
                R.drawable.food2
            )
        )
    }
    val gradientModifier = modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 16.dp)
        .height(250.dp)
        .clip(RoundedCornerShape(10))
        .clickable(onClick = onClick)
        .verticalGradientBackground(listOf(Color(rgb.value), MaterialTheme.colorScheme.surface))

    Surface(
        modifier = gradientModifier, color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FoodSpacer()
                    SellerRow(seller)
                    FoodSpacer()

                    FoodCardImage(
                        model = food.foodPic,
                        rgb = rgb,
                        bitmap = bitmap
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp)
                    ) {
                        Text(
                            text = food.foodName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.8f
                                )
                            ),
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
                ScoreIcon(modifier = Modifier.align(Alignment.TopEnd),seller = seller)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd),
                    horizontalArrangement = Arrangement.End
                ) {
                    StarButton(
                        seller = seller,
                        food = food,
                        saveFavorite = saveFavorite,
                        deleteFavorite = deleteFavorite,
                        isFavoriteFood = isFavoriteFood
                    )
                }
            }
        }
    }
}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun FoodCardImage(
    model: String,
    rgb: MutableState<Int>,
    bitmap: MutableState<Bitmap>,
) {
    val onSurface = MaterialTheme.colorScheme.onSurface.toArgb()
    var textColor by remember { mutableStateOf(onSurface) }
    val request = rememberImageRequest(
        model = model,
        onLoaded = { dominantRgb, tc ->
            rgb.value = dominantRgb
            textColor = tc
        }, onBitmapLoaded = {
            bitmap.value = it
        }
    )
    val context = LocalContext.current

    LaunchedEffect(key1 = model) {
        context.imageLoader.execute(request)
    }
    Image(
        bitmap = bitmap.value.asImageBitmap(),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun ScoreIcon(modifier: Modifier, seller: User) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(50),
        modifier = modifier
            .size(35.dp)
            .padding(top = 10.dp, end = 10.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = seller.score.toString(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
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
        AsyncImage(
            model = seller.headImg,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(
                    width = 48.dp,
                    height = 38.dp
                )
                .clip(RoundedCornerShape(16))
                .padding(start = 8.dp, top = 4.dp),
            alignment = Alignment.Center
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = seller.username, maxLines = 1, style = MaterialTheme.typography.titleSmall)
    }
}


@Preview
@Composable
private fun FoodCardPreview() {
    FoodsTheme {
        Box(modifier = Modifier.width(width = 200.dp)) {
            FoodCard(
                onClick = {},
                saveFavorite = { food, seller ->
                },
                deleteFavorite = { food, seller ->

                },
                isFavoriteFood = true
            )
        }
    }
}


val LocalCoroutineScope = staticCompositionLocalOf { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

@Composable
fun rememberImageRequest(
    model:String,
    onLoaded:(dominantRgb:Int,textColor:Int) -> Unit,
    onBitmapLoaded:(bitmap: Bitmap) -> Unit
) : ImageRequest {
    val context = LocalContext.current
    val scope = LocalCoroutineScope.current
    return remember {
        ImageRequest.Builder(context)
            .data(model)
            .target { drawable ->
                // Handle the result.
                //java.lang.IllegalStateException: unable to getPixels(), pixel access is not supported on Config#HARDWARE bitmaps
                val notMutableBitmap = drawable.toBitmap()/*.asImageBitmap()*/
                //转换为可变bitmap
                val mutableBitmap = notMutableBitmap.copy(Bitmap.Config.ARGB_8888, true)
                onBitmapLoaded(mutableBitmap)
//                bitmap.value = mutableBitmap
                Log.v("coil", "下载drawable, bitmap=$mutableBitmap")

                scope.launch(Dispatchers.Default) {
                    val swatch = mutableBitmap.generateDominantColorState()
                    onLoaded(swatch.rgb,swatch.bodyTextColor)
//                    rgb.value = swatch.rgb
//                    textColor.value = swatch.bodyTextColor
                    Log.v("coil", "下载drawable, bitmap=$mutableBitmap")
                }
            }
            .build()
    }
}
