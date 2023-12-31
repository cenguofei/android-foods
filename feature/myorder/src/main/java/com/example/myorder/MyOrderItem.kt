package com.example.myorder

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.model.remoteModel.Order

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyOrderItem(order: Order) {
    if (order.orderDetailList.isEmpty()) {
        return
    }
    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
        .clip(RoundedCornerShape(10))
        .height(190.dp), color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = order.canteenName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f), color = MaterialTheme.colorScheme.surface
            ) {
                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                    if (order.orderDetailList.size == 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                        FoodSmallImage(model = order.orderDetailList[0].foodPic)
                        Column(
                            modifier = Modifier.weight(1f).padding(start = 8.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = order.orderDetailList[0].foodName,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = order.createTime,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    } else {
                        Column(modifier = Modifier.weight(1f)) {
                            LazyRow {
                                Log.v("cgf","LazyRow order.orderDetailList="+order.orderDetailList)
                                order.orderDetailList.forEach {
                                    item(it.id) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.padding(start = 8.dp)
                                        ) {
                                            FoodSmallImage(model = it.foodPic)
                                            Text(
                                                text = it.foodName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            Text(
                                text = order.createTime,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    Text(
                        text = "￥ " + order.price,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .padding(bottom = 8.dp, end = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FoodSmallImage(model: String) {
    AsyncImage(
        model = model,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(12))
    )
}