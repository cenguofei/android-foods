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
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.example.designsystem.DataProvider
import com.example.designsystem.R
import com.example.designsystem.generateDominantColorState
import com.example.designsystem.theme.FoodsTheme
import com.example.designsystem.verticalGradientBackground
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

@SuppressLint("AutoboxingStateCreation")
@Composable
fun FoodCard(
    modifier: Modifier = Modifier,
    seller: User = DataProvider.user,
    food: Food = DataProvider.Food,
    onClick: () -> Unit,
    saveFavorite: (food: Food, seller: User) -> Unit,
    deleteFavorite: (food: Food, seller: User) -> Unit
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
        modifier = gradientModifier,color = Color.Transparent
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
                            Log.v("cgf", "首页card Rounded.Favorite")
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            previousState = true
                            saveFavorite(food, seller)
                        } else {
                            Log.v("cgf", "首页card Outlined.Favorite")
                            Icon(
                                imageVector = Icons.Outlined.FavoriteBorder,
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
}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun FoodCardImage(
    model: String,
    rgb: MutableState<Int>,
    bitmap: MutableState<Bitmap>
) {
    val onSurface = MaterialTheme.colorScheme.onSurface.toArgb()
    var textColor by remember { mutableStateOf(onSurface) }
    val scope = rememberCoroutineScope()
    val request = ImageRequest.Builder(LocalContext.current)
        .data(model)
        .target { drawable ->
            // Handle the result.
            //java.lang.IllegalStateException: unable to getPixels(), pixel access is not supported on Config#HARDWARE bitmaps
            val notMutableBitmap = drawable.toBitmap()/*.asImageBitmap()*/
            //转换为可变bitmap
            val mutableBitmap = notMutableBitmap.copy(Bitmap.Config.ARGB_8888, true)
            bitmap.value = mutableBitmap
            Log.v("coil", "下载drawable, bitmap=$mutableBitmap")

            scope.launch(Dispatchers.Main) {
                val swatch = mutableBitmap.generateDominantColorState()
                rgb.value = swatch.rgb
                textColor = swatch.bodyTextColor
                Log.v("coil", "下载drawable, bitmap=$mutableBitmap")
            }
        }
        .build()
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
fun ScoreIcon(modifier: Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(50),
        modifier = modifier
            .size(35.dp)
            .padding(top = 10.dp, end = 10.dp)
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
private fun FoodSpacer() {
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun SellerRow(
    seller: User,
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        //TODO 把User的头像换成后端的[showimg]
        AsyncImage(
            model = seller.headImg,
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
        Text(text = seller.username, maxLines = 1, style = MaterialTheme.typography.titleSmall)
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
                onClick = {}
            )
        }
    }
}
