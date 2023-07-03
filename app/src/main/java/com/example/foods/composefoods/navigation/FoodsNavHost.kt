package com.example.foods.composefoods.navigation

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.datastore.SettingsUiState
import com.example.favorite.FavoriteScreen
import com.example.favorite.FavoriteViewModel
import com.example.fooddetail.FoodDetailScreen
import com.example.foods.composefoods.activitys.MyBackHandler
import com.example.foods.composefoods.activitys.shouldUseDarkTheme
import com.example.foods.composefoods.ui.FoodsAppState
import com.example.home.HomeScreen
import com.example.home.HomeViewModel
import com.example.login.ui.LoginScreenRoute
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import com.example.myorder.MyOrderScreen
import com.example.search.SearchScreen
import com.example.sellerdetail.SellerDetailScreen
import com.example.shoppingcart.ShoppingCartScreen
import com.example.start.StartScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodsNavHost(
    appState: FoodsAppState,
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    drawerState: DrawerState,
    startScreen: Screens,
) {
    val coroutineScope = rememberCoroutineScope()
    val navController = appState.navController
    val favoriteViewModel: FavoriteViewModel = viewModel()

    LaunchedEffect(key1 = appState.currentUser.value, block = {
        favoriteViewModel.getFavorites(appState.currentUser.value.username)
    })

    val currentBackStackEntry = appState.navController.currentBackStackEntryAsState().value
    Log.v(
        "navigation_test",
        "currentBackStackEntry{\nid=${currentBackStackEntry?.id}\n,toString=${currentBackStackEntry.toString()},\nroute=${currentBackStackEntry?.destination?.route}"
    )

    val backToHome: () -> Unit = remember {
        {
            appState.navController.navigate(Screens.Home.route) {
                popUpTo(Screens.Home.route) {
                    inclusive = true
                }
            }
        }
    }
    /**
     * 在首页，点击返回：
     *  如果抽屉打开：先关闭抽屉
     */
    MyBackHandler(appState = appState, drawerState = drawerState, coroutineScope)
    NavHost(
        navController = navController,
        startDestination = startScreen.route,
        modifier = modifier,
    ) {
        startScreen(appState = appState)
        homeScreen(
            appState = appState,
            favoriteViewModel = favoriteViewModel,
            onSearchClick = {
                appState.navigateToSearch()
            },
            onShowSnackbar = onShowSnackbar
        )
        searchScreen(
            onBack = backToHome
        )
        sellerDetailScreen(
            appState = appState,
            homeViewModel = appState.homeViewModel,
            onShowSnackbar = onShowSnackbar,
            onBack = backToHome
        )
        loginScreen(appState = appState)
        myOrderScreen(
            appState = appState,
            onBack = backToHome
        )
        favoriteScreen(
            favoriteViewModel = favoriteViewModel,
            onBack = backToHome
        )
        foodDetailScreen(
            favoriteViewModel = favoriteViewModel,
            homeViewModel = appState.homeViewModel,
            appState = appState,
            onBack = {
                appState.navigateToSellerDetail()
            }
        )
        shoppingCartScreen(
            appState = appState,
            onShowSnackbar = onShowSnackbar,
            onBack = backToHome
        )
    }
}

private fun NavGraphBuilder.homeScreen(
    appState: FoodsAppState,
    favoriteViewModel: FavoriteViewModel,
    onSearchClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable(Screens.Home.route) {
        val settingsUiStateState = appState.settingsViewModel.settingsUiState.collectAsState()
        if (settingsUiStateState.value is SettingsUiState.Success) {
            if ((settingsUiStateState.value as SettingsUiState.Success).settings.isFirstUse) {
                appState.settingsViewModel.updateUseState(false)
            }
        }
        val homeViewModel = appState.homeViewModel

        val shoppingCart =
            appState.shoppingCardViewModel.getAllShoppingCartFood(appState.currentUser.value).collectAsState(listOf())
        Log.v("ShoppingCardViewModel","collect:${shoppingCart.value}")

        HomeScreen(
            homeViewModel = homeViewModel,
            onSellerFoodClick = { foods: List<Food>, seller: User ->
                homeViewModel.setNewSellerHolder(foods, seller)
                Log.v("BottomScrollableContent", "HomeScreen:homeViewModel=$homeViewModel")
                appState.navigateToSellerDetail(fromHome = true)
            },
            saveFavorite = { food, seller ->
                favoriteViewModel.addFavorite(
                    currentUser = appState.currentUser.value,
                    seller = seller,
                    food = food,
                    onError = {
                        // TODO while save favorite failed.
                    },
                    onSuccess = { }
                )
            },
            deleteFavorite = { food, seller ->
                favoriteViewModel.deleteFavorite(
                    food = food,
                    onSuccess = {},
                    onError = {
                        // TODO while delete favorite failed.
                    },
                    currentUser = appState.currentUser.value
                )
            },
            favoriteFoodIds = favoriteViewModel.favoriteFoodIds,
            onSearchClick = onSearchClick,
            shoppingCard = shoppingCart.value.map { it.toFood() },
            onUsersLoaded = {
                appState.shoppingCardViewModel.setUsers(it)
            },
            onShoppingCartClick = {
                appState.navigateToShoppingCart()
            },
            onNotificationClick = {

            },
            onShowSnackbar = onShowSnackbar
        )
    }
}

private fun NavGraphBuilder.startScreen(appState: FoodsAppState) {
    composable(route = Screens.Start.route) {
        StartScreen(
            onSignUpClick = {
                appState.navigateToLoginOrSignUp(true)
            },
            onBeginClick = {
                appState.navigateToTopLevelDestination(from = Screens.Start)
            }
        )

        LaunchedEffect(key1 = Unit, block = {
            appState.settingsViewModel.updateUserInitialThemeBehavior()
        })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.sellerDetailScreen(
    appState: FoodsAppState,
    homeViewModel: HomeViewModel,
    onBack: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean
) {
    composable(
        Screens.SellerDetail.route + "/{shouldShowDialog}",
        arguments = listOf(
            navArgument("shouldShowDialog") {
                type = NavType.BoolType
                defaultValue = false
            }
        )
    ) { navBackEntry ->
        val shouldShowDialog = remember {
            mutableStateOf(navBackEntry.arguments?.getBoolean("shouldShowDialog") ?: false)
        }
        var navBack by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        SellerDetailScreen(
            seller = homeViewModel.seller,
            foods = homeViewModel.foods,
            onBackClick = {
                Log.v("onBackClick", "Seller Detail onBackClick")
                navBack = true
                onBack()
            },
            currentLoginUser = appState.currentUser.value,
            mainViewModel = appState.shoppingCardViewModel,
            onSellerSingleFoodClick = {
                homeViewModel.updateClickedFood(it)
                appState.navigateToFoodDetail()
            },
            shouldShowDialogForNav = shouldShowDialog,
            onShowSnackbar = onShowSnackbar,
            coroutineScope = coroutineScope
        ) {
            Log.v("darkTheme", "Seller Detail darkTheme=$it")
            appState.shouldStatusBarContentDark.value = it
        }
        val uiState: State<SettingsUiState> =
            appState.settingsViewModel.settingsUiState.collectAsState()
        val darkTheme = shouldUseDarkTheme(uiState = uiState.value)

        LaunchedEffect(navBack, appState.shouldStatusBarContentDark.value) {
            if (navBack) {
                appState.systemUiController.systemBarsDarkContentEnabled = !darkTheme
                appState.systemUiController.setSystemBarsColor(
                    Color.Transparent,
                    darkIcons = !darkTheme
                )
            } else {
                appState.systemUiController.systemBarsDarkContentEnabled =
                    appState.shouldStatusBarContentDark.value
                appState.systemUiController.setSystemBarsColor(
                    Color.Transparent,
                    darkIcons = appState.shouldStatusBarContentDark.value
                )
            }
        }

        BackHandler {
            appState.systemUiController.systemBarsDarkContentEnabled = !darkTheme
            appState.systemUiController.setSystemBarsColor(
                Color.Transparent,
                darkIcons = !darkTheme
            )
            appState.navController.popBackStack()
        }
    }
}

private fun NavGraphBuilder.loginScreen(appState: FoodsAppState) {
    composable(
        Screens.Login.route + "/{isRegister}",
        arguments = listOf(
            navArgument("isRegister") {
                type = NavType.BoolType
                defaultValue = false
            }
        )
    ) { navBackEntry ->
        val isRegister = navBackEntry.arguments?.getBoolean("isRegister") ?: false
        Log.v("重复加载测试", "Login屏幕,isRegister=$isRegister")
        LoginScreenRoute(
            onSuccess = {
                Log.v(
                    "navigation_test",
                    "loginNavigation navigateToTopLevelDestination(TopLevelDestination.HOME)"
                )
                appState.setCurrentUser(it)
                appState.navigateToTopLevelDestination(from = Screens.Login)
            },
            onError = {
                //TODO BUG
//                    coroutineScope.launch {
//                        onShowSnackbar(it, null)
//                    }
            },
            isRegister = isRegister
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.myOrderScreen(
    appState: FoodsAppState,
    onBack: () -> Unit
) {
    composable(Screens.MyOrder.route) {
        MyOrderScreen(
            onBack = onBack,
            currentLoginUser = appState.currentUser.value
        )
    }
}

private fun NavGraphBuilder.favoriteScreen(
    favoriteViewModel: FavoriteViewModel,
    onBack: () -> Unit
) {
    composable(Screens.Favorite.route) {
        FavoriteScreen(
            onBack = onBack,
            favoriteViewModel = favoriteViewModel
        )
    }
}

private fun NavGraphBuilder.searchScreen(
    onBack: () -> Unit
) {
    composable(Screens.Search.route) {
        SearchScreen(onBack = onBack)
    }
}


private fun NavGraphBuilder.foodDetailScreen(
    homeViewModel: HomeViewModel,
    favoriteViewModel: FavoriteViewModel,
    appState: FoodsAppState,
    onBack: () -> Unit
) {
    composable(Screens.FoodDetail.route) {
        FoodDetailScreen(
            food = homeViewModel.clickedFood,
            seller = homeViewModel.seller,
            onBack = onBack,
            saveFavorite = { _, _ ->
                favoriteViewModel.addFavorite(
                    appState.currentUser.value,
                    homeViewModel.seller,
                    homeViewModel.clickedFood
                )
            },
            deleteFavorite = { _, _ ->
                favoriteViewModel.deleteFavorite(
                    food = homeViewModel.clickedFood,
                    currentUser = appState.currentUser.value
                )
            },
            isFavoriteFood = favoriteViewModel.foodInFavorites(homeViewModel.clickedFood),
            mainViewModel = appState.shoppingCardViewModel,
            onCommitOrder = {
                appState.navigateToSellerDetail(true)
            },
            currentUser = appState.currentUser.value,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.shoppingCartScreen(
    appState: FoodsAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    onBack: () -> Unit
) {
    composable(Screens.ShoppingCartScreen.route) {
        ShoppingCartScreen(
            onBack = onBack,
            shoppingCardViewModel = appState.shoppingCardViewModel,
            currentLoginUser = appState.currentUser.value,
            onShowSnackbar = onShowSnackbar
        )
    }
}