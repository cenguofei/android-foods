package com.example.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun IconWithTopEndMessage(
    onClick: () -> Unit,
    imageVector: ImageVector,
    message:String
) {

    Box(modifier = Modifier.size(60.dp)) {
        IconButton(onClick = onClick) {
            Icon(imageVector = imageVector, contentDescription = null)
        }

        Surface(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .align(Alignment.TopEnd)
                .offset {
                    IntOffset.Zero.copy(y = 8.dp.roundToPx())
                }
        ) {
            val contentColorFor = contentColorFor(backgroundColor = MaterialTheme.colors.primary)
            Text(text = message,color = contentColorFor, fontWeight = FontWeight.Medium)
        }
    }
}

