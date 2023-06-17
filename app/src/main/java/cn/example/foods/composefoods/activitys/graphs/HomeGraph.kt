//package cn.example.foods.composefoods.activitys.graphs
//
//import android.os.Build
//import android.util.Log
//import androidx.annotation.RequiresApi
//import androidx.compose.animation.AnimatedContentTransitionScope
//import androidx.compose.animation.ExperimentalAnimationApi
//import androidx.compose.animation.core.tween
//import androidx.navigation.NavGraphBuilder
//import androidx.navigation.NavType
//import androidx.navigation.navArgument
//import androidx.navigation.navigation
//import cn.example.foods.composefoods.navigation.NavigationRoutes
//import cn.example.foods.composefoods.navigation.Screens
//import cn.example.foods.composefoods.navigation.createParcelableNavType
//import cn.example.foods.composefoods.ui.FoodsAppState
//import com.example.home.HomeScreen
//import com.example.model.remoteModel.Food
//import com.example.model.remoteModel.User
//import com.example.sellerdetail.SellerDetailRoute
//import com.google.accompanist.navigation.animation.composable
//
//fun NavGraphBuilder.homeNavigation(
//    onShowError: (String) -> Unit,
//    appState: FoodsAppState
//) {
//
//    homeScreen(
//        onFoodClick = { foods: List<Food>, seller: User ->
//            Log.v("navigation_test", "点击了：seller=$seller, foods=$foods")
//            val arrayList = arrayListOf<Food>()
//            foods.toCollection(arrayList)
//            appState.navigateToSellerDetail(foods = arrayList, seller = seller)
//        },
//        onShowError = onShowError
//    )
//    sellerDetailScreen()
//
//}
//
//@Suppress("DEPRECATED")
//@OptIn(ExperimentalAnimationApi::class)
//fun NavGraphBuilder.sellerDetailScreen() {
//    composable(
//        Screens.SellerDetail.route + "?key1={seller}",
//        arguments = listOf(
//            navArgument("seller") {
//                type = createParcelableNavType<User>()
//            }
//        )
//    ) { backStackEntry ->
//        val seller:User? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            backStackEntry.arguments?.getParcelable("seller",User::class.java)!!
//        }else {
//            backStackEntry.arguments?.getParcelable("seller")
//        }
//        Log.v("seller_test", "seller:$seller")
//    }
//}
//
//@OptIn(ExperimentalAnimationApi::class)
//private fun NavGraphBuilder.homeScreen(
//    onFoodClick: (food: List<Food>, seller: User) -> Unit = { _, _ -> },
//    onShowError: (String) -> Unit
//) {
//    composable(
//        Screens.HOME.route,
//        enterTransition = {
//            when (initialState.destination.route) {
//                "Blue" ->
//                    slideIntoContainer(
//                        AnimatedContentTransitionScope.SlideDirection.Left,
//                        animationSpec = tween(700)
//                    )
//
//                "Green" ->
//                    slideIntoContainer(
//                        AnimatedContentTransitionScope.SlideDirection.Up,
//                        animationSpec = tween(700)
//                    )
//
//                else -> null
//            }
//        },
//        exitTransition = {
//            when (targetState.destination.route) {
//                "Blue" ->
//                    slideOutOfContainer(
//                        AnimatedContentTransitionScope.SlideDirection.Left,
//                        animationSpec = tween(700)
//                    )
//
//                "Green" ->
//                    slideOutOfContainer(
//                        AnimatedContentTransitionScope.SlideDirection.Up,
//                        animationSpec = tween(700)
//                    )
//
//                else -> null
//            }
//        },
//        popEnterTransition = {
//            when (initialState.destination.route) {
//                "Blue" ->
//                    slideIntoContainer(
//                        AnimatedContentTransitionScope.SlideDirection.Right,
//                        animationSpec = tween(700)
//                    )
//
//                "Green" ->
//                    slideIntoContainer(
//                        AnimatedContentTransitionScope.SlideDirection.Down,
//                        animationSpec = tween(700)
//                    )
//
//                else -> null
//            }
//        },
//        popExitTransition = {
//            when (targetState.destination.route) {
//                "Blue" ->
//                    slideOutOfContainer(
//                        AnimatedContentTransitionScope.SlideDirection.Right,
//                        animationSpec = tween(700)
//                    )
//
//                "Green" ->
//                    slideOutOfContainer(
//                        AnimatedContentTransitionScope.SlideDirection.Down,
//                        animationSpec = tween(700)
//                    )
//
//                else -> null
//            }
//        }) {
//        HomeScreen(
//            onShowError = onShowError,
//            onFoodClick = onFoodClick
//        )
//    }
//}
