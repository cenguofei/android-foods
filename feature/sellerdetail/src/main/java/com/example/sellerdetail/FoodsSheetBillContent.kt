package com.example.sellerdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.remoteModel.Food

@Composable
fun FoodsSheetBillContent(selectedFood: SnapshotStateMap<Food, Int>) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = buildAnnotatedString {
                pushStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp
                    )
                )
                append("已选商品")
                pop()
                (SpanStyle(color = MaterialTheme.colorScheme.primary, fontSize = 18.sp))
                append("打包费 ￥" + (selectedFood.size * 2))
            })
            TextButton(onClick = { selectedFood.clear() }) {
                Text(text = "清空购物车", style = MaterialTheme.typography.labelMedium)
            }
        }
        selectedFood.keys.forEach {
            FoodsListItem(food = it, modifier = Modifier, isSelectedFood = true,selectedFood = selectedFood)
        }
        Spacer(modifier = Modifier.height(120.dp))
    }
}