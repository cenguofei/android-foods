package com.example.start

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IndicatorRow(
    pagerState: PagerState,
    onBoardingList: List<String>,
    onBeginClick: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .padding(bottom = 40.dp)
    ) {
        onBoardingList.forEachIndexed { index, _ ->
            OnBoardingPagerSlide(
                selected = index == pagerState.currentPage,
                MaterialTheme.colorScheme.primary,
                Icons.Filled.Favorite
            )
        }
    }
    Button(
        onClick = {
            if (pagerState.currentPage != onBoardingList.size - 1) {
                scope.launch { pagerState.animateToNextPage() }
            } else {
                onBeginClick()
            }
        },
        modifier = Modifier
            .animateContentSize(animationSpec = tween())
            .padding(bottom = 32.dp)
            .height(50.dp)
            .clip(CircleShape)
    ) {
        Text(
            text = if (pagerState.currentPage == onBoardingList.size - 1) "Let's Begin" else "Next",
            modifier = Modifier.padding(horizontal = 32.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun OnBoardingPagerSlide(selected: Boolean, selectedColor: Color, icon: ImageVector) {
    Icon(
        imageVector = icon,
        modifier = Modifier.padding(4.dp),
        contentDescription = null,
        tint = if (selected) selectedColor else Color.Gray
    )
}

@OptIn(ExperimentalFoundationApi::class)
suspend fun PagerState.animateToNextPage() {
    if (currentPage + 1 < pageCount) {
        animateScrollToPage(currentPage + 1)
    }
}