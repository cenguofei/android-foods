package com.example.fooddetail

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object ImageBorderGradient {

    @Composable
    fun borderGradient() : List<Color> {
        val darkTheme = isSystemInDarkTheme()
        return listOf(
            if (darkTheme) Color.White else Color.Black,
            Color.Cyan,
            Color.Magenta
        )
    }
}