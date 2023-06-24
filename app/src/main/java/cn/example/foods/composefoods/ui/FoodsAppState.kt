package cn.example.foods.composefoods.ui

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.PopUpToBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.common.di.ShoppingCardViewModel
import cn.example.foods.composefoods.navigation.Screens
import cn.example.foods.composefoods.navigation.TopLevelDestination
import com.example.datastore.SettingsViewModel
import com.example.home.HomeViewModel
import com.example.network.netstate.NetworkMonitor
import com.example.model.remoteModel.User
import com.google.accompanist.systemuicontroller.SystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberFoodsAppState(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
    settingsViewModel: SettingsViewModel,
    mainViewModel: ShoppingCardViewModel,
    homeViewModel: HomeViewModel,
    systemUiController: SystemUiController
): FoodsAppState {
    NavigationTrackingSideEffect(navController)
    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
        networkMonitor,
        settingsViewModel
    ) {
        FoodsAppState(
            navController,
            coroutineScope,
            windowSizeClass,
            networkMonitor,
            settingsViewModel,
            mainViewModel,
            homeViewModel,
            systemUiController = systemUiController
        )
    }
}

@Stable
class FoodsAppState(
    val navController: NavHostController,
    coroutineScope: CoroutineScope,
    private val windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    val settingsViewModel: SettingsViewModel,
    val shoppingCardViewModel: ShoppingCardViewModel,
    val homeViewModel: HomeViewModel,
    val systemUiController: SystemUiController
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val shouldStatusBarContentDark:MutableState<Boolean> = mutableStateOf(false)

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            Screens.Home.route -> TopLevelDestination.HOME
            else -> null
        }

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = !shouldShowBottomBar

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()


    /**
     * 导航到HOME
     * 判断是否是从from导航，如果是，只popUpTo from 一次
     */
    fun navigateToTopLevelDestination(
        topLevelDestination: TopLevelDestination = TopLevelDestination.HOME,
    ) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHome(navOptions)
            else -> {

            }
        }
    }

    fun navigateToSearch() {
        navController.navigate(Screens.Search.route)
    }

    fun navigateToSellerDetail(shouldShowDialog: Boolean = false, fromHome: Boolean = false) {
        val navOptions = if (!fromHome) {
            navOptions {
                popUpTo(Screens.FoodDetail.route) {
                    /**
                     * Whether the `popUpTo` destination should be popped from the back stack.
                     */
                    inclusive = true

                    /**
                     * Whether the back stack and the state of all destinations between the
                     * current destination and the [NavOptionsBuilder.popUpTo] ID should be saved for later
                     * restoration via [NavOptionsBuilder.restoreState] or the `restoreState` attribute using
                     * the same [NavOptionsBuilder.popUpTo] ID (note: this matching ID is true whether
                     * [inclusive] is true or false).
                     *
                     * 当使用 NavOptionsBuilder.popUpTo ID 属性来指定要弹出到的目标 ID 时，
                     * 是否应该保存当前目标和弹出目标之间所有目标的状态和后退栈，
                     * 以便稍后通过 NavOptionsBuilder.restoreState 或 restoreState 属性进行恢复。
                     * 这个 ID 参数需要匹配，无论 inclusive 是否为 true。
                     */
                    saveState = false
                }
                /**
                 * Whether this navigation action should launch as single-top (i.e., there will be at most
                 * one copy of a given destination on the top of the back stack).
                 */
                launchSingleTop = true
                /**
                 * Whether this navigation action should restore any state previously saved
                 * by [PopUpToBuilder.saveState] or the `popUpToSaveState` attribute. If no state was
                 * previously saved with the destination ID being navigated to, this has no effect.
                 */
                restoreState = false
            }
        } else {
            navOptions { }
        }
        navController.navigate(Screens.SellerDetail.route + "/$shouldShowDialog", navOptions)
    }

    fun navigateToLoginOrSignUp(isRegister: Boolean = false) {
        navController.navigate(Screens.Login.route + "/$isRegister")
    }

    private var _currentUser: MutableState<User> = mutableStateOf(User.NONE)
    val currentUser = _currentUser
    fun setCurrentUser(user: User) {
        Log.v("cgf", "setCurrentUser:$user")
        _currentUser.value = user
    }

    fun navigateToMyOrder() {
        navController.navigate(Screens.MyOrder.route)
    }

    fun navigateToMyFavorite() {
        navController.navigate(Screens.Favorite.route)
    }

    fun navigateToFoodDetail() {
        navController.navigate(Screens.FoodDetail.route)
    }

    fun navigateToShoppingCart() {
        navController.navigate(Screens.ShoppingCartScreen.route)
    }
}

private fun NavHostController.navigateToHome(topLevelNavOptions: NavOptions) {
    Log.v("navigation_test", "NavHostController.navigateToHome(topLevelNavOptions: NavOptions)")
    navigate(route = Screens.Home.route, navOptions = topLevelNavOptions)
}

/**
 * Stores information about navigation events.
 */
@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, arguments ->
            Log.v(
                "navigation_events",
                "Foods Destination:${destination.route.toString()},arguments:$arguments"
            )
            val entries = navController.visibleEntries.value.toString()
            Log.v("navigation_events", "visibleEntries=$entries")
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}