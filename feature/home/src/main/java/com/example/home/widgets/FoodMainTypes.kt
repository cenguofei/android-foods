package com.example.home.widgets

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.outlined.ChangeHistory
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.CropSquare
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val foodsTypesLineHeight = 75.dp
val foodsTypesDescHeight = 80.dp

@Composable
fun FoodMainTypes(
    modifier: Modifier = Modifier,
    types: List<Pair<Int, String>>,
    coroutineScope: CoroutineScope,
    visible: MutableState<Boolean> = remember { mutableStateOf(false) },
    onDoubleLine: (Boolean) -> Unit = {},
    foodTypesHeight:Animatable<Float,AnimationVector1D>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.Circle,
                        contentDescription = null,
                        modifier = Modifier.size(10.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Row {
                        Icon(
                            imageVector = Icons.Outlined.CropSquare,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = Icons.Outlined.ChangeHistory,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = "热门类别",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            val rotation = remember { Animatable(0f) }
            LaunchedEffect(key1 = visible.value, block = {
                if (visible.value) {
                    onDoubleLine(true)
                    coroutineScope.launch {
                        rotation.animateTo(-180f)
//                        foodTypesHeight.animateTo(foodsTypesLineHeight.value )
                    }
                } else {
                    onDoubleLine(false)
                    coroutineScope.launch {
                        rotation.animateTo(0f)
//                        foodTypesHeight.animateTo(0f)
                    }
                }
            })
            IconButton(
                onClick = { visible.value = !visible.value },
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotation.value
                }) {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            types.take(3).forEach {
                item { FoodsTypeImage(it) }
            }
        }
//        AnimatedVisibility(visible = visible.value, enter = fadeIn(), exit = fadeOut()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                types.reversed().take(3).forEach {
                    item { FoodsTypeImage(it) }
                }
            }
//        }
    }
}
