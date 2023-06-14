package com.example.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.designsystem.R

@Composable
fun UserHeader(model:Any?) {
    AsyncImage(
        modifier = Modifier.size(60.dp),
        alignment = Alignment.Center,
        model = model,
        contentScale = ContentScale.Crop,
        contentDescription = stringResource(R.string.your_header),
        placeholder = painterResource(id = R.drawable.default_head_img),
        error = painterResource(id = R.drawable.default_head_img),
    )
}

@Composable
fun UserHeaderWithBorder(
    model:Any?,
    colors:List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.background,
        MaterialTheme.colorScheme.surface
    )
) {
    val brush = Brush.linearGradient(colors)
    AsyncImage(
        modifier = Modifier
            .size(60.dp)
            .border(width = 1.dp, brush = brush, shape = RoundedCornerShape(50)),
        alignment = Alignment.Center,
        model = model,
        contentScale = ContentScale.Crop,
        contentDescription = stringResource(R.string.your_header),
        placeholder = painterResource(id = R.drawable.default_head_img),
        error = painterResource(id = R.drawable.default_head_img),
    )
}