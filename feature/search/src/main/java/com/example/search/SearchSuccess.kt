package com.example.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.page.PageList
import com.example.model.remoteModel.Food

@Composable
fun SearchSuccess(pageList: PageList<Food>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
        pageList.rows.forEach {
            item { SearchItem(food = it) }
        }
    }
}

@Composable
fun SearchItem(food: Food) {
    Card(
        modifier = Modifier
            .height(135.dp)
            .fillMaxWidth()
            .padding(top = 12.dp, start = 8.dp, end = 8.dp),
        shape = RoundedCornerShape(12)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            SellerImage(
                food = food,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2.5f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = food.foodName,
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleLarge
                )
                val primary = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                OutlinedCard(
                    modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 5.dp),
                    colors = CardDefaults.outlinedCardColors(contentColor = primary),
                    border = BorderStroke(1.dp,MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = food.foodCategory,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .padding(horizontal = 8.dp),
                        color = primary
                    )
                }
                Text(
                    text = food.taste,
                    style = MaterialTheme.typography.bodyMedium
                )
                PriceBar(
                    modifier = Modifier,
                    price = food.price
                )
            }
        }
    }

//    ConstraintLayout(
//        modifier = Modifier
//            .height(128.dp)
//            .fillMaxWidth()
//            .padding(top = 12.dp, start = 8.dp, end = 8.dp)
//    ) {
//        val (foodImg, foodNameTxt, foodTasteTxt, foodCategoryTxt, priceTxt) = createRefs()
//        SellerImage(
//            food = food,
//            modifier = Modifier.constrainAs(foodImg) {
//                start.linkTo(parent.start, margin = 8.dp)
//                top.linkTo(parent.top, margin = 8.dp)
//                bottom.linkTo(parent.bottom, margin = 8.dp)
//            }
//        )
//        Text(
//            text = food.foodName,
//            modifier = Modifier.constrainAs(foodNameTxt) {
//                top.linkTo(parent.top, margin = 8.dp)
//                start.linkTo(foodImg.start, margin = 6.dp)
//            }
//        )
//        OutlinedCard(modifier = Modifier.constrainAs(foodCategoryTxt) {
//            start.linkTo(foodNameTxt.start)
//            top.linkTo(foodNameTxt.bottom, margin = 8.dp)
//        }) {
//            Text(
//                text = food.foodCategory,
//                modifier = Modifier
//                    .padding(vertical = 4.dp)
//                    .padding(horizontal = 8.dp)
//            )
//        }
//        Text(
//            text = food.taste,
//            modifier = Modifier.constrainAs(foodTasteTxt) {
//                top.linkTo(foodCategoryTxt.top, margin = 8.dp)
//                start.linkTo(foodCategoryTxt.start)
//            }
//        )
//        PriceBar(
//            modifier = Modifier.constrainAs(priceTxt) {
//                start.linkTo(foodTasteTxt.start)
//                end.linkTo(parent.end, margin = 2.dp)
//            },
//            price = food.price
//        )
//    }
}

@Composable
private fun SellerImage(food: Food, modifier: Modifier) {
    Surface(
        shape = RoundedCornerShape(16),
        modifier = modifier.width(118.dp)
    ) {
        AsyncImage(
            model = food.foodPic,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center
        )
    }
}

@Composable
private fun PriceBar(
    modifier: Modifier,
    price: Double
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(12)
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val priceAnno = buildAnnotatedString {
                pushStyle(SpanStyle(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    fontWeight = FontWeight.ExtraBold
                ))
                pushStyle(SpanStyle(fontSize = 18.sp))
                append("￥")
                pop()
                pushStyle(SpanStyle(fontSize = 26.sp))
                append("$price")
            }
            Text(text = priceAnno, modifier = Modifier.padding(horizontal = 8.dp).weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "抢",
                    style = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(3.dp))
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null,tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}