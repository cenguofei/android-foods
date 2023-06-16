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

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cn.example.foods.composefoods.activitys.MyBackHandler
import cn.example.foods.composefoods.activitys.graphs.homeNavigation
import cn.example.foods.composefoods.activitys.graphs.loginNavigation
import cn.example.foods.composefoods.ui.FoodsAppState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FoodsNavHost(
    appState: FoodsAppState,
    modifier: Modifier = Modifier,
    startDestination: String = Screens.HOME.route,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    drawerState: DrawerState,
) {
    val coroutineScope = rememberCoroutineScope()
    val navController = appState.navController
    val activity = LocalContext.current as Activity

    MyBackHandler(appState = appState, drawerState = drawerState,coroutineScope)
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeNavigation(
//            onFoodClick = { food, seller ->
//                val bundleOf = Bundle().apply {
//                    putParcelable("currentUser",appState.currentUser.value)
//                    putParcelable("food",food)
//                    putParcelable("seller",seller)
//                    putString("str","hello lwh")
//                }
//                val intent = Intent(activity, SellerDetailActivity::class.java)
//                intent.putExtras(bundleOf)
//                activity.startActivity(intent)
//            },
            onShowError = {
                coroutineScope.launch {
                    onShowSnackbar(it, null)
                }
            },
            appState = appState
        )
        loginNavigation(
            appState = appState,
            onSuccess = {
                Log.v(
                    "navigation_test",
                    "loginNavigation navigateToTopLevelDestination(TopLevelDestination.HOME)"
                )
                appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
                appState.setCurrentUser(it)
            },
            onShowError = {
                coroutineScope.launch {
                    onShowSnackbar(it, null)
                }
            }
        )
    }
}




