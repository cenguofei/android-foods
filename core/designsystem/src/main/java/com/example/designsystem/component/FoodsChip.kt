package com.example.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.theme.FoodsTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FoodsChip(
    modifier:Modifier = Modifier,
    onSelectedChanged:(Boolean) -> Unit = {},
    selected:Boolean
) {
    Surface(
        modifier = modifier
            .size(15.dp),
        shape = RoundedCornerShape(50),
        onClick = {
            onSelectedChanged(!selected)
        },
        color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    BorderStroke(
                        1.dp,
                        color = if (selected) MaterialTheme.colorScheme.primary else Color.Gray.copy(
                            alpha = 0.8f
                        )
                    ), shape = RoundedCornerShape(50)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = Color.Black
                )
            }
        }
    }
}

@Preview
@Composable
private fun ChipSelectedPreview() {
    FoodsTheme {
        Box(Modifier.size(100.dp)) {
            FoodsChip(selected = true)

        }
    }
}

@Preview
@Composable
private fun ChipUnselectedPreview() {
    FoodsTheme {
        Box(Modifier.size(100.dp)) {
            FoodsChip(selected = false)

        }
    }
}