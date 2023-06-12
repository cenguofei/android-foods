package com.example.start

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun MeetMeal() {
    StartBodyText(id = R.string.discover_your_next)
    StartBodyText(id = R.string.favorite_meal)
}

@Composable
private fun StartBodyText(
    @StringRes id:Int
) {
    Text(
        modifier = Modifier,
        text = stringResource(id),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
        textAlign = TextAlign.Center
    )
}
