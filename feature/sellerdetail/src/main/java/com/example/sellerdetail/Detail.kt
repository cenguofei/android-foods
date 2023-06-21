package com.example.sellerdetail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.common.di.MainViewModel
import com.example.designsystem.component.FoodsMaterial3AlertDialog
import com.example.designsystem.component.FoodsOutlinedTextField
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import kotlinx.coroutines.launch

object FoodsDataProvider {
    @Composable
    fun foodsSurfaceGradient(isDark: Boolean) =
        if (isDark) listOf(
            MaterialTheme.colorScheme.onSecondaryContainer,
            MaterialTheme.colorScheme.onBackground
        )
        else listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.background
        )
}

sealed class DominantState {
    object Loading : DominantState()
    data class Success(val bitmap: Bitmap) : DominantState()
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FoodsDetailScreen(
    seller: User,
    scaffoldState: BottomSheetScaffoldState,
    selectedFood: SnapshotStateMap<Food, Int>,
    onBackClick: () -> Unit,
    currentLoginUser: MutableState<User>,
    sellerDetailViewModel: SellerDetailViewModel,
    categoryFoods: Map<String, List<Food>>,
    mainViewModel: MainViewModel
) {
    val scrollState = remember { mutableStateOf(ScrollState(initial = 0)) }
    val coroutineScope = rememberCoroutineScope()

    var shouldShowDialog by remember { mutableStateOf(false) }
//    var commitButtonEnabled by remember { mutableStateOf(true) }
    val address = remember { mutableStateOf(TextFieldValue("")) }
    val tel = remember { mutableStateOf(TextFieldValue("")) }

    val commitOrder = {
        sellerDetailViewModel.commitOrder(
            selectedFood = selectedFood,
            currentLoginUser = currentLoginUser,
            address = address.value.text,
            tel = tel.value.text,
            seller = seller,
            onInputError = {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar(
                            "请输入正确的信息\uD83D\uDC95",
                            duration = SnackbarDuration.Long
                        )
                }
            },
            onCommitError = {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar(
                            "出错啦!错误信息：$it\uD83D\uDE4A",
                            duration = SnackbarDuration.Long
                        )
                }
            },
            onCommitSuccess = {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar("提交成功\uD83E\uDD70", duration = SnackbarDuration.Long)
                }
                shouldShowDialog = false
            }
        )
    }

    if (shouldShowDialog) {
        FoodsMaterial3AlertDialog(
            onDismissRequest = {
                shouldShowDialog = !shouldShowDialog
            },
            confirmButton = {
                Surface(
//                    enabled = commitButtonEnabled,
                    onClick = {
//                        commitButtonEnabled = false
                        commitOrder()
                    },
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(30)
                ) {
                    Text(
                        text = "确认",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.surface,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }
            },
            buttons = {
                FoodsOutlinedTextField(
                    value = address.value,
                    onValueChange = { address.value = it },
                    leadingIcon = Icons.Default.PinDrop,
                    backgroundEnabled = true,
                    shape = RoundedCornerShape(18)
                )
                FoodsOutlinedTextField(
                    value = tel.value,
                    onValueChange = { tel.value = it },
                    leadingIcon = Icons.Default.Call,
                    backgroundEnabled = true,
                    shape = RoundedCornerShape(18)
                )
            },
            titleText = "输入订单信息",
        )
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.Center,
        sheetContent = {
            FoodsSheetBillContent(selectedFood = selectedFood,mainViewModel = mainViewModel)
        },
        sheetElevation = 8.dp,
        sheetShape = RoundedCornerShape(topStartPercent = 10, topEndPercent = 10),
        drawerGesturesEnabled = false,
        sheetPeekHeight = 0.dp
    ) {
        Box {
            SellerDetailContent(
                seller = seller,
                scrollState = scrollState,
                selectedFood = selectedFood,
                onBackClick = onBackClick,
                categoryFoods = categoryFoods,
                mainViewModel = mainViewModel
            )

            FoodsFAB(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp),
                scaffoldState = scaffoldState,
                coroutineScope = coroutineScope,
                onCommitClick = {
                    shouldShowDialog = !shouldShowDialog
                },
                selectedFood = selectedFood,
                sellerDetailViewModel = sellerDetailViewModel
            )
            Image(
                painter = painterResource(id = R.drawable.bottom_kid),
                modifier = Modifier
                    .align(
                        Alignment.BottomStart
                    )
                    .size(height = 120.dp, width = 100.dp),
                contentDescription = null
            )
        }
    }
}


private fun String.log(s: String) {
    Log.v(s, this)
}

@SuppressLint("AutoboxingStateCreation")
@Composable
fun SideBar(
    modifier: Modifier,
    categoryFoods: Map<String, List<Food>>,
    onCategoryClick: (Int) -> Unit,
    textColor: Int
) {
    Box(
        modifier = modifier
            .fillMaxSize()
//            .verticalGradientBackground(
//                FoodsDataProvider.foodsSurfaceGradient(isDark = isSystemInDarkTheme())
//            ),
        , contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            categoryFoods.keys.forEachIndexed { index,category->
                Surface(
                    color = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable {
                        onCategoryClick(index)
                    }
                ) {
                    Text(
                        category,
                        Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(textColor),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedToolBar(
    scrollState: MutableState<ScrollState>,
    surfaceGradient: List<Color>,
    seller: User,
    onBackClick:() -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        }
        Text(
            text = seller.canteenName,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(16.dp)
                .alpha(((scrollState.value.value + 0.001f) / 1000).coerceIn(0f, 1f))
        )
        Icon(
            imageVector = Icons.Default.MoreVert, tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = null
        )
    }
}
