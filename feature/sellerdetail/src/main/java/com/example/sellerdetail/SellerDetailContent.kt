package com.example.sellerdetail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.designsystem.component.glideImage
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SellerDetailContent(
    seller: User,
    foods: List<Food>,
    scrollState: ScrollState,
    selectedFood: SnapshotStateMap<Food, Int>,
) {
    val surface = MaterialTheme.colorScheme.surface
    val dominantState: MutableState<DominantState> =
        remember(seller.id) { mutableStateOf(DominantState.Loading) }
    val modifier = remember(seller.id) {
        mutableStateOf(
            when (dominantState.value) {
                DominantState.Loading -> {
                    Modifier.fillMaxSize()
                }

                else -> {
                    val bitmap = (dominantState.value as DominantState.Success).bitmap
                    val swatch = bitmap.generateDominantColorState()
                    Modifier
                        .fillMaxSize()
                        .verticalGradientBackground(listOf(Color(swatch.rgb), surface))
                }
            }
        )
    }
    val imageView =
        glideImage(foods[0].foodPic, LocalContext.current, onImageBitmapLoaded = { bitmap ->
            dominantState.value = DominantState.Success(bitmap)
        })

    Column(modifier = Modifier.fillMaxSize()) {
        val surfaceGradient = FoodsDataProvider
            .foodsSurfaceGradient(isSystemInDarkTheme()).asReversed()

        //有background
        //当滚动距离小于1000px时为TRANSPARENT，否则为surfaceGradient
        AnimatedToolBar(
            seller = seller,
            scrollState = scrollState,
            surfaceGradient = surfaceGradient
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            SideBar(foods = foods, modifier = Modifier.width(70.dp))
            //有background
            Box( //Box1
                modifier = modifier.value.weight(1f)
            ) {
                //没有background
                BoxTopSection(
                    seller = seller,
                    dominantState = dominantState,
                    scrollState = scrollState,
                    imageView = imageView
                ) //对图片大小做变化，250dp->10dp

                //有background
                TopSectionOverlay(scrollState = scrollState) // White 的alpha 不断升高，使Box1的竖直渐变背景色不断变淡

                //有background
                //可滚动的内容，占全屏，有480高的Spacer占位符
                BottomScrollableContent(
                    scrollState = scrollState,
                    foods = foods,
                    selectedFood = selectedFood
                )
            }
        }
    }
}