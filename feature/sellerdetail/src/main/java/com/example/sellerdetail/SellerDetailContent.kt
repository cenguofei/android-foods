package com.example.sellerdetail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.example.common.di.ShoppingCardViewModel
import com.example.designsystem.R
import com.example.designsystem.generateDominantColorState
import com.example.designsystem.verticalGradientBackground
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("AutoboxingStateCreation")
@Composable
fun SellerDetailContent(
    seller: User,
    scrollState: MutableState<ScrollState>,
    onBackClick: () -> Unit,
    mainViewModel: ShoppingCardViewModel,
    onSellerSingleFoodClick: (Food) -> Unit = {},
    categories: List<String>,
    categoryFoodsList: List<List<Food>>,
    shouldStatusBarContentDark: (Boolean) -> Unit,
) {
    var rgb by remember { mutableStateOf(0) }
    val onSurface = MaterialTheme.colorScheme.onSurface.toArgb()
    var textColor by remember { mutableStateOf(onSurface) }
    val resource = LocalContext.current.resources
    val bitmap = remember {
        mutableStateOf(
            BitmapFactory.decodeResource(
                resource,
                R.drawable.food2
            )
        )
    }
    val darkTheme = isSystemInDarkTheme()

    val scope = rememberCoroutineScope()
    val request = ImageRequest.Builder(LocalContext.current)
        .data(seller.headImg)
        .target { drawable ->
            // Handle the result.
            // java.lang.IllegalStateException: unable to getPixels(), pixel access is not supported on Config#HARDWARE bitmaps
            val notMutableBitmap = drawable.toBitmap()/*.asImageBitmap()*/
            // 转换为可变bitmap
            val mutableBitmap = notMutableBitmap.copy(Bitmap.Config.ARGB_8888,true)
            bitmap.value = mutableBitmap
            Log.v("coil", "下载drawable, bitmap=$mutableBitmap")

            scope.launch(Dispatchers.Default) {
                val swatch = mutableBitmap.generateDominantColorState()
                rgb = swatch.rgb
                textColor = swatch.bodyTextColor

                val luminance = ColorUtils.calculateLuminance(swatch.rgb)
                if (darkTheme && luminance <= 0.5) {
                    //不变
                } else if (!darkTheme && luminance <= 0.5) {
                    //之前为亮色，计算得暗色
                    shouldStatusBarContentDark(false)
                    Log.v("darkTheme","darkTheme=false")
                } else if (darkTheme && luminance > 0.5) {
                    //之前为暗色，计算得亮色
                    shouldStatusBarContentDark(true)
                    Log.v("darkTheme","darkTheme=true")
                } else if (!darkTheme && luminance > 0.5) {
                    //之前为亮色，计算得亮色
                    //不变
                }
//                val grey = color.red * 0.299 + color.green * 0.587 + color.blue * 0.114
//                val dark = grey >= 192
//                if (darkTheme && !dark) { //之前是 暗，现在是 明
//                    Log.v("darkTheme","shouldUseDarkTheme(true)")
//                    shouldUseDarkTheme(true)
//                } else if (darkTheme && dark) {
//                    // 不变
//                    Log.v("darkTheme","不变")
//                } else if (!darkTheme && dark) { //之前是明，现在是 dark
//                    Log.v("darkTheme","shouldUseDarkTheme(false)")
//                    shouldUseDarkTheme(false)
//                } else if (!darkTheme && !dark) { //之前是明，现在是明
//                    //不变
//                    Log.v("darkTheme","不变")
//                }
                Log.v("coil", "下载drawable, bitmap=$mutableBitmap")
            }
        }
        .build()
    val context = LocalContext.current


    LaunchedEffect(key1 = seller.headImg) {
        context.imageLoader.enqueue(request)
    }

    val gradientModifier = Modifier
        .fillMaxSize()
        .verticalGradientBackground(listOf(Color(rgb), MaterialTheme.colorScheme.surface))

    Column(modifier = gradientModifier.fillMaxSize()) {
        //有background
        //当滚动距离小于1000px时为TRANSPARENT，否则为surfaceGradient
        AnimatedToolBar(
            seller = seller,
            scrollState = scrollState,
            onBackClick = onBackClick
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            val targetState = remember { mutableStateOf(0) }
            SideBar(
                categories = categories,
                modifier = Modifier.width(50.dp),
                onCategoryClick = { pagerIndex ->
                    targetState.value = pagerIndex
                },
                textColor = textColor
            )
            //有background
            Box( //Box1
                modifier = Modifier.weight(1f)
            ) {
                //没有background
                BoxTopSection(
                    seller = seller,
                    scrollState = scrollState,
                    bitmap = bitmap,
                ) //对图片大小做变化，250dp->10dp

                //有background
                //可滚动的内容，占全屏，有480高的Spacer占位符
                SellerPager(
                    categoryFoodsList = categoryFoodsList,
                    targetState = targetState,
                    scrollState = scrollState,
                    mainViewModel = mainViewModel,
                    onSellerSingleFoodClick = onSellerSingleFoodClick,
                )
            }
        }
    }
}

private fun Color.contraryColor(): Int {
    return Color(red - 255,green-255,blue-255).toArgb()
}

