package com.example.sellerdetail

import android.annotation.SuppressLint
import android.graphics.Bitmap
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.remoteModel.User

@SuppressLint("AutoboxingStateCreation")
@Composable
fun BoxTopSection(
    seller: User,
    scrollState: MutableState<ScrollState>,
    bitmap: MutableState<Bitmap>,
) {
    val dynamicAlpha = 1 - ((scrollState.value.value + 0.00f) / 1400).coerceIn(0f, 1f)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        )
        //animate as scroll value increase but not fast so divide by random number 50
        val dynamicValue =
            if (250.dp - Dp(scrollState.value.value / 50f) < 10.dp) 10.dp //prevent going 0 cause crash
            else 250.dp - Dp(scrollState.value.value / 20f)
        val animateImageSize = animateDpAsState(dynamicValue).value
        Image(
            bitmap = bitmap.value.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .size(animateImageSize)
                .padding(8.dp)
                .alpha(dynamicAlpha),
            contentScale = ContentScale.Crop
        )

        Text(
            text = seller.canteenName.ifEmpty { "Foods商家" },
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            modifier = Modifier.padding(8.dp).alpha(dynamicAlpha),
            color = MaterialTheme.colorScheme.onSurface,
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
                .alpha(dynamicAlpha)
        )
        Text(
            text = seller.foodType,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(4.dp).alpha(dynamicAlpha),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun TopSectionOverlay(scrollState: MutableState<ScrollState>) {
    //slowly increase alpha till it reaches 1
    val dynamicAlpha = ((scrollState.value.value + 0.00f) / 1400).coerceIn(0f, 1f)
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


//@SuppressLint("CheckResult")
//@Composable
//fun GlideImage(
//    imageUrl: String,
//    onImageBitmapLoaded: (Bitmap) -> Unit,
//    coroutineScope: CoroutineScope = rememberCoroutineScope()
//) {
//    Glide
//        .with(LocalContext.current)
//        .load(imageUrl)
//        .placeholder(R.drawable.food13)
//        .error(R.drawable.food11)
//        .listener(@SuppressLint("CheckResult")
//        object : RequestListener<Drawable?> {
//            override fun onLoadFailed(
//                e: GlideException?,
//                model: Any?,
//                target: com.bumptech.glide.request.target.Target<Drawable?>?,
//                isFirstResource: Boolean
//            ): Boolean {
//
//                Log.v("glide", "开始下载--onLoadFailed--")
//                return false
//            }
//
//            override fun onResourceReady(
//                resource: Drawable?,
//                model: Any?,
//                target: Target<Drawable?>?,
//                dataSource: DataSource?,
//                isFirstResource: Boolean
//            ): Boolean {
//                Log.v("glide", "GlideReady--onResourceReady--")
//                if (resource is BitmapDrawable) {
//                    val bitmap = resource.bitmap
////                val imageBitmap = bitmap.asImageBitmap()
//                    coroutineScope.launch(Dispatchers.Main) {
//                        onImageBitmapLoaded(bitmap)
//                    }
//                    Log.d("glide", bitmap.toString() + "是resource is BitmapDrawable")
//                } else {
//                    Log.d("glide", "不是 resource not a BitmapDrawable")
//                }
//                return false
//            }
//        })
//}

