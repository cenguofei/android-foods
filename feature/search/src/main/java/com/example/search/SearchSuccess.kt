package com.example.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.page.PageList
import com.example.model.remoteModel.Food

@Composable
fun SearchSuccess(pageList: PageList<Food>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        pageList.rows.forEach {
            item {
                SearchItem(food = it)
            }
        }
    }
}

@Composable
fun SearchItem(food: Food) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, start = 8.dp, end = 8.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            FoodImage(
                food = food,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )
            Column(
                modifier = Modifier
                    .weight(2.5f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = food.foodName,
                    modifier = Modifier,
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 18.sp
                )
                val primary = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                OutlinedCard(
                    colors = CardDefaults.outlinedCardColors(contentColor = primary),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = food.foodCategory,
                        modifier = Modifier
                            .padding(vertical = 1.dp)
                            .padding(horizontal = 4.dp)
                            .align(Alignment.CenterHorizontally),
                        color = primary,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Text(
                    text = food.taste,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 5
                )
                PriceBar(
                    price = food.price
                )
            }
        }
    }
}

@Composable
private fun FoodImage(food: Food, modifier: Modifier) {
    AsyncImage(
        model = food.foodPic,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(65.dp)
            .clip(RoundedCornerShape(50)),
        alignment = Alignment.Center
    )
}

@Composable
private fun PriceBar(
    price: Double
) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val priceAnno = buildAnnotatedString {
            pushStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold
                )
            )
            pushStyle(SpanStyle(fontSize = 14.sp))
            append("￥")
            pop()
            pushStyle(SpanStyle(fontSize = 18.sp))
            append("$price")
        }
        Text(
            text = priceAnno, modifier = Modifier
                .weight(1f)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "抢",
                style = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(3.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}