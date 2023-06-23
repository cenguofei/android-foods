package com.example.fooddetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.common.di.ShoppingCardViewModel
import com.example.designsystem.common.StarButton
import com.example.designsystem.component.FoodsTopAppBar
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User

@Composable
fun FoodDetailScreen(
    food: Food,
    onBack: () -> Unit,
    saveFavorite: (food: Food, seller: User) -> Unit,
    deleteFavorite: (food: Food, seller: User) -> Unit,
    isFavoriteFood: Boolean,
    seller: User,
    mainViewModel: ShoppingCardViewModel,
    onCommitOrder: () -> Unit,
    currentUser: User
) {
    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .height(380.dp)
                .fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStartPercent = 100, bottomEndPercent = 100))
                    .background(brush = with(Brush) {
                        verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondaryContainer,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                            ),
                        )
                    })
            )
            val imageHeight = 220.dp
            Surface(
                modifier = Modifier
                    .height(imageHeight)
                    .width(180.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 52.dp),
                shape = RoundedCornerShape(12)
            ) {
                AsyncImage(
                    model = food.foodPic,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        FoodDetailContent(
            food = food,
            mainViewModel = mainViewModel,
            onCommitOrder = onCommitOrder,
            currentUser = currentUser
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 380.dp - scoreSize / 2)
        ) {
            Surface(
                modifier = Modifier
                    .size(scoreSize)
                    .border(
                        width = 3.dp,
                        brush = Brush.linearGradient(
                            colors = ImageBorderGradient.borderGradient()
                        ),
                        shape = RoundedCornerShape(50)
                    ),
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = seller.score.toString(),
                        color = MaterialTheme.colorScheme.surface,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Text(
                text = "评分",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        FoodsTopAppBar(
            onBack = onBack,
            endContent = {
                StarButton(
                    saveFavorite = saveFavorite,
                    deleteFavorite = deleteFavorite,
                    isFavoriteFood = isFavoriteFood
                )
            }
        )
    }
}

val scoreSize = 50.dp