package com.example.start.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.LocalBackgroundTheme
import com.example.start.R

@Composable
fun FoodsSplashScreen() {
    Surface(color = LocalBackgroundTheme.current.color) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Image(modifier = Modifier.fillMaxSize().padding(
                horizontal = 45.dp,
                vertical = 100.dp
            ),painter = painterResource(id = R.drawable.flower), contentDescription = null)
        }
    }
}