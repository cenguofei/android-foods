/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.example.foods.composefoods.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cn.example.foods.composefoods.activitys.MyBackHandler
import cn.example.foods.composefoods.ui.FoodsAppState
import com.example.datastore.SettingsUiState
import com.example.home.HomeScreen
import com.example.home.HomeViewModel
import com.example.login.ui.LoginScreenRoute
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import com.example.myorder.MyOrderScreen
import com.example.sellerdetail.SellerDetailRoute
import com.example.start.StartScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FoodsNavHost(
    appState: FoodsAppState,
    modifier: Modifier = Modifier,
//   TODO BUG when use: onShowSnackbar: suspend (String, String?) -> Boolean,
    drawerState: DrawerState,
    startScreen: Screens,
) {
    val coroutineScope = rememberCoroutineScope()
    val navController = appState.navController
    val homeViewModel:HomeViewModel = viewModel()

    MyBackHandler(appState = appState, drawerState = drawerState,coroutineScope)
    NavHost(
        navController = navController,
        startDestination = startScreen.route,
        modifier = modifier,
    ) {
        composable(route = Screens.Start.route) {
            StartScreen(
                onSignUpClick = {
                    appState.settingsViewModel.updateUserInitialThemeBehavior()
                    appState.navigateToLoginOrSignUp(true)
                },
                onBeginClick = {
                    appState.settingsViewModel.updateUserInitialThemeBehavior()
                    appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
                }
            )
        }
        composable(Screens.HOME.route) {
            val settingsUiStateState = appState.settingsViewModel.settingsUiState.collectAsState()
            if (settingsUiStateState.value is SettingsUiState.Success) {
                if ((settingsUiStateState.value as SettingsUiState.Success).settings.isFirstUse) {
                    appState.settingsViewModel.updateUseState(false)
                }
            }
            HomeScreen(
                homeViewModel = homeViewModel,
                onShowError = {
                              //TODO BUG when use onShowSnackbar
//                    coroutineScope.launch {
//                        onShowSnackbar(it, null)
//                    }
                },
                onFoodClick = { foods: List<Food>, seller: User ->
                    HomeViewModel.foods = foods
                    HomeViewModel.seller = seller
                    appState.navigateToSellerDetail()
                }
            )
        }
        composable(Screens.SellerDetail.route) {
            SellerDetailRoute(
                seller = HomeViewModel.seller,
                foods = HomeViewModel.foods,
                onBackClick = {
                    appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
                },
                currentLoginUser = appState.currentUser
            )
        }
        composable(
            Screens.LOGIN.route + "/{isRegister}",
            arguments = listOf(
                navArgument("isRegister") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { navBackEntry ->
            val isRegister = navBackEntry.arguments?.getBoolean("isRegister") ?: false
            Log.v("重复加载测试","Login屏幕,isRegister=$isRegister")
            LoginScreenRoute(
                onSuccess = {
                    Log.v(
                        "navigation_test",
                        "loginNavigation navigateToTopLevelDestination(TopLevelDestination.HOME)"
                    )
                    appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
                    appState.setCurrentUser(it)
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
        composable(Screens.MyORDER.route) {
            MyOrderScreen(
                onBack = {
                    appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
                },
                currentLoginUser = appState.currentUser.value
            )
        }
    }
}




