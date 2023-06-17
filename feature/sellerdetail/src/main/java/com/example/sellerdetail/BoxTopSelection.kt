package com.example.sellerdetail

import android.util.Log
import android.widget.ImageView
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.model.remoteModel.User

@Composable
fun BoxTopSection(
    seller: User,
    scrollState: ScrollState,
    dominantState: MutableState<DominantState>,
    imageView: ImageView
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        )
        //animate as scroll value increase but not fast so divide by random number 50
        val dynamicValue =
            if (250.dp - Dp(scrollState.value / 50f) < 10.dp) 10.dp //prevent going 0 cause crash
            else 250.dp - Dp(scrollState.value / 20f)
        val animateImageSize = animateDpAsState(dynamicValue).value

        when(dominantState.value) {
            is DominantState.Loading -> {
                Image(
                    painter = painterResource(id = com.example.designsystem.R.drawable.food2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(animateImageSize)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            }
            is DominantState.Success -> {
                Log.v("cgf","DominantState.Success  商家详情图片")
                AndroidView(factory = {
                    imageView
                })
                Image(
                    bitmap = (dominantState.value as DominantState.Success)
                        .bitmap
                        .asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(animateImageSize)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Text(
            text = seller.canteenName.ifEmpty { "Foods商家" },
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "FOLLOWING",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 12.sp),
            modifier = Modifier
                .padding(4.dp)
                .border(
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 4.dp, horizontal = 24.dp)
        )
        Text(
            text = seller.foodType,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(4.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun TopSectionOverlay(scrollState: ScrollState) {
    //slowly increase alpha till it reaches 1
    val dynamicAlpha = ((scrollState.value + 0.00f) / 1400).coerceIn(0f, 1f)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .background(
                Color.White.copy( //white
                    alpha = animateFloatAsState(dynamicAlpha).value
                )
            )
    )
}
