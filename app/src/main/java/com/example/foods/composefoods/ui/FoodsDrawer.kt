package com.example.foods.composefoods.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.datastore.SettingsViewModel
import com.example.foods.composefoods.navigation.TopLevelDestination
import com.example.model.remoteModel.User
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodsDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    settingsViewModel: SettingsViewModel,
    appState: FoodsAppState,
    user: User = appState.currentUser.value,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val closeDrawer = {
        coroutineScope.launch { drawerState.snapTo(DrawerValue.Closed) }
    }
    ModalNavigationDrawer(
        modifier = modifier,
        gesturesEnabled = appState.currentTopLevelDestination in TopLevelDestination.values(),
        drawerContent = {
            DrawerContent(
                settingsViewModel,
                user = user,
                onLogin = {
                    closeDrawer()
                    appState.navigateToLoginOrSignUp()
                },
                onSeeMyOrder = {
                    closeDrawer()
                    appState.navigateToMyOrder()
                },
                onSeeMyFavorites = {
                    closeDrawer()
                    appState.navigateToMyFavorite()
                },
                onLogout = {
                    appState.setCurrentUser(User.NONE)
                }
            )
        },
        drawerState = drawerState,
        content = content
    )
}
