package cn.example.foods.composefoods.ui

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import cn.example.foods.composefoods.navigation.TopLevelDestination
import com.example.network.netstate.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Composable
fun rememberFoodsAppState(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
//    userNewsResourceRepository: UserNewsResourceRepository,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): FoodsAppState {
    NavigationTrackingSideEffect(navController)
    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
        networkMonitor,
//        userNewsResourceRepository,
    ) {
        FoodsAppState(
            navController,
            coroutineScope,
            windowSizeClass,
            networkMonitor,
//            userNewsResourceRepository,
        )
    }
}

@Stable
class FoodsAppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
//    userNewsResourceRepository: UserNewsResourceRepository,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

//    val currentTopLevelDestination: TopLevelDestination?
//        @Composable get() = when (currentDestination?.route) {
//            forYouNavigationRoute -> FOR_YOU
//            bookmarksRoute -> BOOKMARKS
//            interestsRoute -> INTERESTS
//            else -> null
//        }

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
//    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    /**
     * The top level destinations that have unread news resources.
     */
//    val topLevelDestinationsWithUnreadResources: StateFlow<Set<TopLevelDestination>> =
//        userNewsResourceRepository.observeAllForFollowedTopics()
//            .combine(userNewsResourceRepository.observeAllBookmarked()) { forYouNewsResources, bookmarkedNewsResources ->
//                setOfNotNull(
//                    FOR_YOU.takeIf { forYouNewsResources.any { !it.hasBeenViewed } },
//                    BOOKMARKS.takeIf { bookmarkedNewsResources.any { !it.hasBeenViewed } },
//                )
//            }.stateIn(
//                coroutineScope,
//                SharingStarted.WhileSubscribed(5_000),
//                initialValue = emptySet(),
//            )

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }

//            when (topLevelDestination) {
//                TopLevelDestination.FOR_YOU -> navController.navigateToForYou(topLevelNavOptions)
//                TopLevelDestination.BOOKMARKS -> navController.navigateToBookmarks(topLevelNavOptions)
//                TopLevelDestination.INTERESTS -> navController.navigateToInterestsGraph(topLevelNavOptions)
//            }
    }

//    fun navigateToSearch() {
//        navController.navigateToSearch()
//    }
}

/**
 * Stores information about navigation events to be used with JankStats
 */
//@Composable
//private fun NavigationTrackingSideEffect(navController: NavHostController) {
//    TrackDisposableJank(navController) { metricsHolder ->
//        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
//            metricsHolder.state?.putState("Navigation", destination.route.toString())
//        }
//
//        navController.addOnDestinationChangedListener(listener)
//
//        onDispose {
//            navController.removeOnDestinationChangedListener(listener)
//        }
//    }
//}


/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            Log.v("Navigation",destination.route.toString())
        }

        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}

