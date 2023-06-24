package com.example.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ErrorScreen(
    description:String = "",
    shouldShowAnim:Boolean = true
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (shouldShowAnim) {
            FoodsLottieAnimation(assetName = "error1.json")
        }
        Text(text = "出错啦！！！\uD83D\uDE23\uD83D\uDE23", style = MaterialTheme.typography.titleSmall)
        Text(text = description, style = MaterialTheme.typography.bodyMedium)
    }
}
