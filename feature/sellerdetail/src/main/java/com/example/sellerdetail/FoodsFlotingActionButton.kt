package com.example.sellerdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.remoteModel.Food
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FoodsFAB(
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState,
    onCommitClick: () -> Unit,
    coroutineScope: CoroutineScope,
    sellerDetailViewModel: SellerDetailViewModel,
    selectedFood: SnapshotStateMap<Food, Int>,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(50)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(2f)
                    .background(MaterialTheme.colorScheme.onSurface)
                    .clickable(onClick = {
                        coroutineScope.launch {
                            if (scaffoldState.bottomSheetState.isCollapsed) {
                                if (selectedFood.size > 0) {
                                    scaffoldState.bottomSheetState.expand()
                                }
                            } else {
                                scaffoldState.bottomSheetState.collapse()
                            }
                        }
                    }),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "￥ "+sellerDetailViewModel.calculatePrice(selectedFood),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 20.sp
                )
                Text(
                    text = "预估另需配送费 ￥"+(selectedFood.size * 1 + 0.5),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.surface
                )
            }

            Row(modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary)
                .padding(start = 4.dp)
                .clickable(onClick = onCommitClick), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surface
                )
                Text(
                    text = "提交订单",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    color = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}