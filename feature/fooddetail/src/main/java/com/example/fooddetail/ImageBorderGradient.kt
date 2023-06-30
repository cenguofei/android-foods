package com.example.fooddetail

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object ImageBorderGradient {

    @Composable
    fun borderGradient() : List<Color> {
        val darkTheme = isSystemInDarkTheme()
        return listOf(
            if (darkTheme) Color.White else MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.secondary
        )
    }
}