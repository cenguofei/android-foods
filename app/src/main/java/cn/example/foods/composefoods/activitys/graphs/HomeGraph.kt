package cn.example.foods.composefoods.activitys.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import cn.example.foods.composefoods.navigation.Screens
import com.example.home.HomeScreen
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.User
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeNavigation(
    onFoodClick:(food: Food, seller: User) -> Unit = { _, _ ->},
    onShowError: (String) -> Unit
) {
    composable(
        Screens.HOME.route,
        enterTransition = {
            when (initialState.destination.route) {
                "Blue" ->
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )

                "Green" ->
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(700)
                    )

                else -> null
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                "Blue" ->
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )

                "Green" ->
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(700)
                    )

                else -> null
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                "Blue" ->
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )

                "Green" ->
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(700)
                    )

                else -> null
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                "Blue" ->
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )

                "Green" ->
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(700)
                    )

                else -> null
            }
        }) {
        HomeScreen(
            onShowError = onShowError,
            onFoodClick = onFoodClick
        )
    }
}

