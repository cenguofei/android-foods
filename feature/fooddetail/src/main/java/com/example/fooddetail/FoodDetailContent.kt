package com.example.fooddetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.di.ShoppingCardViewModel
import com.example.designsystem.common.AddAndRemoveFood
import com.example.designsystem.component.FoodsCenterRow
import com.example.designsystem.theme.LocalTintTheme
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User

@Composable
fun FoodDetailContent(
    food: Food,
    mainViewModel: ShoppingCardViewModel,
    onCommitOrder: () -> Unit,
    currentUser: User
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(350.dp + scoreSize + 32.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = food.foodName,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "￥${food.price}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            val foodDesc = buildAnnotatedString {
                pushStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface))

                pushStyle(
                    SpanStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                append("描述\n")
                pop()

                pushStyle(
                    ParagraphStyle(
                        textAlign = TextAlign.Center,
                        textIndent = TextIndent(22.sp),
                    )
                )
                append("主要成分：${food.taste}\n")
                pop()


                pushStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                append("数量")
            }
            Text(text = foodDesc)

            CompositionLocalProvider(
                LocalTintTheme provides
                        LocalTintTheme.current.copy(iconTint = MaterialTheme.colorScheme.primary)
            ) {
                AddAndRemoveFood(
                    modifier = Modifier
                        .padding(end = 4.dp, bottom = 4.dp),
                    num = mainViewModel.getFoodNumInShoppingCart(food.id,currentUser),
                    onAdd = {
                        mainViewModel.addFoodToShoppingCard(food,currentUser)
                    },
                    onRemove = {
                        mainViewModel.removeFoodFromShoppingCard(food,currentUser)
                    }
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp), shape = RoundedCornerShape(50)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                FoodsCenterRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onCommitOrder, modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.background)
                    ) {
                        Text(
                            text = "立即购买",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { mainViewModel.addFoodToShoppingCard(food,currentUser) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = "加入购物车",
                                color = MaterialTheme.colorScheme.surface,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = "￥${food.price}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}