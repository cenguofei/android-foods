package com.example.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.designsystem.component.FoodsCenterRow
import com.example.model.remoteModel.Favorite


@Composable
fun FavoriteItem(favorite: Favorite) {
    FoodsCenterRow(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(top = 16.dp)) {

        AsyncImage(
            model = favorite.sellerPic,
            contentDescription = null,
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(RoundedCornerShape(12.dp))
                .height(150.dp)
                .weight(1f)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(2f)
                .clip(RoundedCornerShape(12))
                .background(MaterialTheme.colorScheme.primary),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            ProvideTextStyle(
                LocalTextStyle.provides(
                    MaterialTheme.typography.bodyMedium
                    .copy(color = MaterialTheme.colorScheme.surface)).value
            ) {
                Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Text(
                        text = favorite.canteenName,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
                FoodsCenterRow(modifier = Modifier.padding(horizontal = 12.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color.Yellow
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = favorite.score.toString())
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(text = favorite.foodType)
                }
            }
        }
    }
}
