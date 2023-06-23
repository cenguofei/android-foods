package com.example.sellerdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.common.di.ShoppingCardViewModel
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FoodsDetailContent(
    seller: User,
    scaffoldState: BottomSheetScaffoldState,
    selectedFood: List<Food>,
    onBackClick: () -> Unit,
    currentLoginUser: User,
    sellerDetailViewModel: SellerDetailViewModel,
    mainViewModel: ShoppingCardViewModel,
    onSellerSingleFoodClick: (Food) -> Unit = {},
    categories: List<String>,
    categoryFoodsList: List<List<Food>>,
    shouldShowDialogForNav: MutableState<Boolean>,
    shouldStatusBarContentDark:(Boolean) -> Unit
) {
    val scrollState = remember { mutableStateOf(ScrollState(initial = 0)) }
    val coroutineScope = rememberCoroutineScope()
    var shouldShowDialog by remember { mutableStateOf(false) }
    val address = remember { mutableStateOf(TextFieldValue("")) }
    val tel = remember { mutableStateOf(TextFieldValue("")) }

    val commitOrder = {
        sellerDetailViewModel.commitOrder(
            selectedFood = mainViewModel.getSellerFoods(seller),
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
    if (shouldShowDialog || shouldShowDialogForNav.value) {
        if (shouldShowDialogForNav.value) {
            shouldShowDialogForNav.value = false
        }
        FoodsMaterial3AlertDialog(
            onDismissRequest = {
                shouldShowDialog = !shouldShowDialog
            },
            confirmButton = {
                Surface(
                    onClick = {
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
            FoodsSheetBillContent(
                selectedFood = selectedFood,
                mainViewModel = mainViewModel,
                onSellerSingleFoodClick = onSellerSingleFoodClick,
                seller = seller,
                drawerState = scaffoldState.drawerState,
                onCloseDrawer = {
//                    if (scaffoldState.bottomSheetState.isExpanded) {
//                        coroutineScope.launch {
//                            scaffoldState.bottomSheetState.collapse()
//                        }
//                    }
                },
                currentLoginUser = currentLoginUser
            )
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
                onBackClick = onBackClick,
                categories = categories,
                categoryFoodsList = categoryFoodsList,
                mainViewModel = mainViewModel,
                onSellerSingleFoodClick = onSellerSingleFoodClick,
                shouldStatusBarContentDark = shouldStatusBarContentDark,
                currentLoginUser = currentLoginUser
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
                selectedFood = mainViewModel.getSellerFoods(seller),
                sellerDetailViewModel = sellerDetailViewModel,
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


