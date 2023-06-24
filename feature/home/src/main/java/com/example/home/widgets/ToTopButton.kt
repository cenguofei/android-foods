package com.example.home.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ToTopButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    lazyStaggeredState: LazyStaggeredGridState
) {
    val scrollOffset = remember {
        derivedStateOf {
            lazyStaggeredState.firstVisibleItemScrollOffset * (1 + lazyStaggeredState.firstVisibleItemIndex)
        }
    }
//    Log.v("scrollOffset","scrollOffset=${scrollOffset.value},index=${lazyListState.firstVisibleItemIndex}")
    AnimatedVisibility(
        modifier = modifier,
        visible = scrollOffset.value > 500,
        enter =  slideIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ) { fullSize: IntSize ->
            IntOffset(x = 0, y = fullSize.height)
        },
        exit =  fadeOut()
    ) {
        Surface(modifier = Modifier.size(70.dp), onClick = onClick,shape = RoundedCornerShape(50)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.colorScheme.surface
                            ),
                            tileMode = TileMode.Decal
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(text = "顶部", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}