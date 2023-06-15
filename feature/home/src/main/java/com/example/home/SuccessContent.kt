package com.example.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.designsystem.component.FoodsBackground
import com.example.designsystem.component.FoodsTopAppBar
import com.example.designsystem.component.SearchToolbar
import com.example.designsystem.theme.FoodsTheme
import com.example.network.remote.remoteModel.Food

@Composable
fun SuccessContent(
    data: Map<String, List<Food>>?,
    onSearch: (String) -> Unit,
    searchQuery: String = "今天想吃点什么?",
    position: Position = Position.NONE,
    types:List<String> = listOf("中式快餐","西式快餐","饮品")
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        FoodsHomeToolBar(position)

        SearchToolbar(
            searchQuery = searchQuery,
            onLeadingClick = {
                onSearch(searchQuery)
            })


    }
}


@Composable
fun FoodsHomeToolBar(position: Position) {
    FoodsTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        startContent = {
            StartContent(position)
        },
        endContent = {
            Box(modifier = Modifier.size(60.dp)) {
                IconButton(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = {
                        // TODO 消息通知
                    }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                FoodsMessage()
            }

            Box(
                modifier = Modifier.size(60.dp)
            ) {
                IconButton(onClick = {
                    // TODO 购物车
                }) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    )
}

@Composable
fun StartContent(position: Position) {
    Spacer(
        modifier = Modifier
            .size(width = 4.dp, height = 50.dp)
            .background(MaterialTheme.colorScheme.primary)
    )
    Column(modifier = Modifier) {
        Text(text = "你的位置", style = MaterialTheme.typography.labelMedium)
        Text(text = position.city, style = MaterialTheme.typography.bodyMedium)
        Text(text = position.zone, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun FoodsMessage(
    num: Int = 0
) {

}

@Preview
@Composable
fun PreviewSuccess() {
    FoodsBackground {
        FoodsTheme {
            SuccessContent(data = mapOf(), onSearch = {})
        }
    }
}
