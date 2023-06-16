package com.example.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ShowShimmerLoading() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)) {

        val transition = rememberInfiniteTransition()
        val animateOpacity by transition.animateFloat(
            initialValue = 0.5f, targetValue = 1f, animationSpec = infiniteRepeatable(
                animation = tween(500,0, LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )


        val animateTransitionX by transition.animateFloat(
            initialValue = 100f,
            targetValue = 500f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000,0, LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        val colors = listOf(Color.Gray, Color.Gray.copy(alpha = 0.5f))
        ListShimmerItem(alpha = animateOpacity, startTranslation = animateTransitionX, colors = colors)
        repeat(5) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.background(
                Color.Transparent
            )) {
                SquareShimmerItem(
                    Modifier.weight(1f),
                    alpha = animateOpacity,
                    startTranslation = animateTransitionX,
                    colors = colors
                )
                SquareShimmerItem(
                    Modifier.weight(1f),
                    alpha = animateOpacity,
                    startTranslation = animateTransitionX,
                    colors = colors
                )
            }
        }
    }

}

@Composable
private fun SquareShimmerItem(
    modifier: Modifier = Modifier,
    alpha:Float,
    startTranslation:Float,
    colors:List<Color>
) {
    val brush = Brush.horizontalGradient(colors,startTranslation)
    Spacer(modifier = modifier
        .background(brush = brush)
        .padding(16.dp)
        .alpha(alpha)
        .height(height = 250.dp))
}

@Composable
fun ListShimmerItem(
    modifier: Modifier = Modifier,
    alpha:Float,
    startTranslation:Float,
    colors:List<Color>
) {
    val brushModifier = modifier.background(
        brush = Brush.horizontalGradient(
            colors,
            startTranslation
        )
    )
    Row(modifier = Modifier.height(200.dp).fillMaxWidth()) {
        Spacer(modifier = brushModifier
            .size(180.dp)
            .alpha(alpha))
        Column {
            Spacer(modifier = brushModifier.height(60.dp).weight(1f).alpha(alpha))
            Spacer(modifier = brushModifier.height(60.dp).weight(1f).alpha(alpha))
            Spacer(modifier = brushModifier.height(60.dp).weight(1f).alpha(alpha))
        }
    }
}