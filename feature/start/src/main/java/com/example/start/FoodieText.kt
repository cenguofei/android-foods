package com.example.start

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp

@Composable
fun FoodieText(
    modifier: Modifier = Modifier
) {

    val annotatedString = buildAnnotatedString {
        pushStyle(SpanStyle(color = Color.Black))
        append("Food")
        pop()
        pushStyle(SpanStyle(color = MaterialTheme.colorScheme.primary))
        // append new text, this text will be rendered as green
        append("ie")
        toAnnotatedString()
    }
    Text(
        modifier = modifier,
        text = annotatedString,
        fontSize = 25.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = FontFamily.Monospace,
        textAlign = TextAlign.Center
    )
}






