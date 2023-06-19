package cn.example.foods.composefoods.activitys.graphs

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import cn.example.foods.composefoods.navigation.Screens
import cn.example.foods.composefoods.ui.FoodsAppState
import com.example.model.remoteModel.User

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.loginNavigation(
    appState: FoodsAppState,
    onShowError: (String) -> Unit,
    onSuccess: (user: User) -> Unit
) {
    composable(Screens.Login.route) {
//        LoginScreenRoute(
//            onSuccess = onSuccess,
//            onError = onShowError,
//            isRegister = isRegister
//        )
    }
}