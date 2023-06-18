package com.example.designsystem.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsClickBarExpandable(
    shouldShow: MutableState<Boolean> = remember { mutableStateOf(false) },
    text: String,
    startIcon: ImageVector? = null,
    endIcon: ImageVector? = null,
    onClick:() -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(onClick = {
                if (endIcon != null) {
                    shouldShow.value = !shouldShow.value
                }
                onClick()
            }),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier.padding(start = 8.dp)) {
            startIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Text(text = text, style = MaterialTheme.typography.labelLarge)
        }
        endIcon?.let {
            val rotation = remember { Animatable(0f) }
            LaunchedEffect(key1 = shouldShow.value) {
                if (shouldShow.value) {
                    rotation.animateTo(180f)
                } else {
                    rotation.animateTo(0f)
                }
            }
            Icon(
                imageVector = it, //Icons.Default.ArrowDownward
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .graphicsLayer {
                        rotationZ = rotation.value
                    },
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}