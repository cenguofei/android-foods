package com.example.shoppingcart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.designsystem.R
import com.example.designsystem.component.FoodsChip
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerFoodsItem(
    foods: List<Food>,
    seller: User,
    viewModel: CartViewModel,
    onSettleAccount:(seller:User) -> Unit
) {
    val foodIds = remember { foods.map { it.id } }
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(4),
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .padding(vertical = 6.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = seller.canteenName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .padding(bottom = 12.dp, top = 4.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            )
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                foods.forEach {
                    FoodItem(
                        food = it,
                        viewModel = viewModel
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 9.dp,start = 16.dp)) {
                Row(modifier = Modifier.weight(1f)) {
                    FoodsChip(
                        selected = viewModel.sellerWithSelectedFoodIds.containsAll(foodIds),
                        onSelectedChanged = {
                            if (it) {
                                viewModel.addIds(foodIds)
                            } else {
                                viewModel.clearIds(foodIds)
                            }
                        }
                    )
                    Text(
                        text = "全选",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(bottom = 4.dp, start = 8.dp)
                    )
                }
                TextButton(
                    onClick = { onSettleAccount(seller) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier
                        .height(35.dp)
                        .width(100.dp)
                        .padding(end = 4.dp, bottom = 4.dp)
                ) {
                    Text(text = "结算", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun FoodItem(
    food: Food,
    viewModel: CartViewModel
) {
    Row(modifier = Modifier
        .fillMaxWidth()) {
        FoodsChip(
            selected = viewModel.sellerWithSelectedFoodIds.contains(food.id),
            onSelectedChanged = {
                if (it) {
                    viewModel.addIds(listOf(food.id))
                } else {
                    viewModel.clearIds(listOf(food.id))
                }
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.dp)
        )
        AsyncImage(
            model = food.foodPic,
            contentDescription = null,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(12))
                .padding(start = 8.dp),
            placeholder = painterResource(id = R.drawable.food1),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = food.foodName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 5.dp)
            )

            Text(
                text = food.taste,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp
            )

            Text(
                text = food.foodCategory,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            )
            val priceAnno = buildAnnotatedString {
                pushStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold
                    )
                )
                pushStyle(SpanStyle(fontSize = 12.sp))
                append("￥")
                pop()
                pushStyle(SpanStyle(fontSize = 16.sp))
                append("${food.price}")
            }
            Text(text = priceAnno)

        }
        Text(
            text = "x${food.count}", modifier = Modifier
                .align(Alignment.Bottom)
                .padding(end = 8.dp, bottom = 8.dp)
        )
    }
}