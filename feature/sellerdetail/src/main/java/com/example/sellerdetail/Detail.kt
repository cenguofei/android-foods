package com.example.sellerdetail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.io.Serializable

data class Album(
    val id: Int,
    val genre: String = "Pop",
    val artist: String,
    val song: String,
    val descriptions: String,
    val imageId: Int,
    val swiped: Boolean = false
) : Serializable

object SpotifyDataProvider {
    fun foodsSurfaceGradient(isDark: Boolean) =
        if (isDark) listOf(Color.Gray, Color.Black) else listOf(Color.White, Color.LightGray)
}

@Composable
fun FoodsDetailScreen(album: Album) {
    val scrollState = rememberScrollState(0)
    val context = LocalContext.current
    val image = ImageBitmap.imageResource(context.resources, id = album.imageId).asAndroidBitmap()
    val swatch = remember(album.id) { image.generateDominantColorState() }


    //使Box1的背景从 Album 图片的主颜色 变化到 surface -> White
    val dominantColors = listOf(Color(swatch.rgb), MaterialTheme.colorScheme.surface)
    val dominantGradient = remember { dominantColors }
    val surfaceGradient = SpotifyDataProvider
        .foodsSurfaceGradient(isSystemInDarkTheme()).asReversed()

    //有background
    Box( //Box1
        modifier = Modifier
            .fillMaxSize()
            .verticalGradientBackground(dominantGradient)
    ) {
        //没有background
        BoxTopSection(album = album, scrollState = scrollState) //对图片大小做变化，250dp->10dp

        //有background
        TopSectionOverlay(scrollState = scrollState) // White 的alpha 不断升高，使Box1的竖直渐变背景色不断变淡

        //有background
        //可滚动的内容，占全屏，有480高的Spacer占位符
        BottomScrollableContent(scrollState = scrollState, surfaceGradient = surfaceGradient)

        //有background
        //当滚动距离小于1000px时为TRANSPARENT，否则为surfaceGradient
        AnimatedToolBar(album, scrollState, surfaceGradient)
    }
}

@Composable
fun AnimatedToolBar(album: Album, scrollState: ScrollState, surfaceGradient: List<Color>) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .horizontalGradientBackground(
                if (Dp(scrollState.value.toFloat()) < 1080.dp)
                    listOf(Color.Transparent, Color.Transparent) else surfaceGradient
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack, tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null
        )
        Text(
            text = album.song,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(16.dp)
                .alpha(((scrollState.value + 0.001f) / 1000).coerceIn(0f, 1f))
        )
        Icon(
            imageVector = Icons.Default.MoreVert, tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null
        )
    }
}
