package com.example.designsystem.common

import android.annotation.SuppressLint
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.designsystem.DataProvider
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User

@SuppressLint("AutoboxingStateCreation")
@Composable
fun StarButton(
    seller: User = DataProvider.user,
    food: Food = DataProvider.Food,
    saveFavorite: (food: Food, seller: User) -> Unit,
    deleteFavorite: (food: Food, seller: User) -> Unit,
    isFavoriteFood: Boolean
) {
    IconButton(
        onClick = {
            if (isFavoriteFood) {
                deleteFavorite(food, seller)
            } else {
                saveFavorite(food, seller)
            }
        },
        modifier = Modifier
    ) {
        if (isFavoriteFood) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}