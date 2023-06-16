package com.example.sellerdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.designsystem.theme.FoodsTheme
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User


@Composable
fun SellerDetailRoute(seller: User?, foods: Array<Food>?) {
    Box {
        FoodsDetailScreen(
            Album(
                1,
                "Pop",
                "artist",
                "song",
                "desc",
                com.example.designsystem.R.drawable.food1,
                false
            )
        )
        Text(text = seller.toString() + foods.toString())
    }

}


@Preview(showBackground = true)
@Composable
fun PreviewSpotifyDetailActivity() {
    val album = Album(
        1,
        "Pop",
        "artist",
        "song",
        "desc",
        com.example.designsystem.R.drawable.food1,
        false
    )
    FoodsTheme {
        FoodsDetailScreen(album)
    }
}