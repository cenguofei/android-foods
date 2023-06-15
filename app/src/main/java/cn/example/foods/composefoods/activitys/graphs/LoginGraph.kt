package cn.example.foods.composefoods.activitys.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import cn.example.foods.composefoods.navigation.Screens
import cn.example.foods.composefoods.ui.FoodsAppState
import com.example.login.ui.LoginScreenRoute
import com.example.model.remoteModel.User
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.loginNavigation(
    appState: FoodsAppState,
    onShowError: (String) -> Unit,
    onSuccess: (user: User) -> Unit
) {
    composable(
        Screens.LOGIN.route,
        enterTransition = {
            when (initialState.destination.route) {
                Screens.HOME.route -> fadeIn()

                else -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        },
        exitTransition = {
            when (targetState.destination.route) {
                Screens.HOME.route -> fadeOut()
                else -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        },
        popEnterTransition = {
            when (initialState.destination.route) {
                Screens.HOME.route -> fadeIn()
                else ->
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
            }
        },
        popExitTransition = {
            when (targetState.destination.route) {
                Screens.LOGIN.route -> fadeOut()
                else -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }
        }) {
        LoginScreenRoute(
            onSuccess = onSuccess,
            onError = onShowError
        )
    }
}