package com.example.home.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.designsystem.component.FoodsTopAppBar
import com.example.home.Position
import com.example.model.remoteModel.Food

@Composable
fun FoodsHomeToolBar(
    position: Position,
    onSearchClick: () -> Unit,
    shoppingCard: SnapshotStateMap<Food, Int>
) {
    FoodsTopAppBar(
        modifier = Modifier
            .systemBarsPadding()
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        needNavigation = false,
        startContent = {
            StartContent(position)
        },
        endContent = {
            Row(modifier = Modifier, horizontalArrangement = Arrangement.End) {
                Box(modifier = Modifier.size(45.dp)) {
                    IconButton(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        onClick = {
                            // TODO 消息通知
                        }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Box(modifier = Modifier.size(45.dp)) {
                    IconButton(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center), onClick = {
                            // TODO 购物车
                        }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = shoppingCard.size.toString(),
                        modifier = Modifier.align(Alignment.TopEnd).padding(top = 8.dp, end = 8.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Box(modifier = Modifier.size(45.dp)) {
                    IconButton(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center), onClick = onSearchClick
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun StartContent(position: Position) {
    Row(modifier = Modifier, horizontalArrangement = Arrangement.Start) {
        Spacer(
            modifier = Modifier
                .size(width = 4.dp, height = 50.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
        Column(modifier = Modifier.padding(start = 12.dp)) {
            val color =
                if (isSystemInDarkTheme()) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface
            Text(text = "你的位置", style = MaterialTheme.typography.labelSmall, color = color)
            Text(
                text = position.city,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = position.zone,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = color
            )
        }
    }
}
