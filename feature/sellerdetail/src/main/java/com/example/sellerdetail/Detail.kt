package com.example.sellerdetail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User

object FoodsDataProvider {
    @Composable
    fun foodsSurfaceGradient(isDark: Boolean) =
        if (isDark) listOf(
            MaterialTheme.colorScheme.onSecondaryContainer,
            MaterialTheme.colorScheme.onBackground
        )
        else listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.background
        )
}

sealed class DominantState {
    object Loading : DominantState()
    data class Success(val bitmap: Bitmap) : DominantState()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FoodsDetailScreen(
    seller: User,
    foods: List<Food>,
    scaffoldState: BottomSheetScaffoldState,
    selectedFood: SnapshotStateMap<Food, Int>
) {
    val scrollState = rememberScrollState(0)

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.Center,
        sheetContent = {
            FoodsSheetBillContent(selectedFood = selectedFood)
        },
        sheetElevation = 8.dp,
        sheetShape = RoundedCornerShape(topStartPercent = 10, topEndPercent = 10),
        drawerGesturesEnabled = false
    ) {
        SellerDetailContent(
            seller = seller,
            foods = foods,
            scrollState = scrollState,
            selectedFood = selectedFood
        )
    }
}


private fun String.log(s: String) {
    Log.v(s, this)
}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun SideBar(foods: List<Food>, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
//            .verticalGradientBackground(
//                FoodsDataProvider.foodsSurfaceGradient(isDark = isSystemInDarkTheme())
//            ),
        , contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            foods.forEach {
                Text(
                    it.foodName,
                    Modifier.padding(vertical = 16.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun AnimatedToolBar(
    scrollState: ScrollState,
    surfaceGradient: List<Color>,
    seller: User
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
//            .horizontalGradientBackground(
////                if (Dp(scrollState.value.toFloat()) < 1080.dp)
////                    listOf(Color.Transparent, Color.Transparent) else surfaceGradient
//                surfaceGradient
//            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack, tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null
        )
        Text(
            text = seller.canteenName,
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
