package cn.example.foods.composefoods.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.example.foods.R
import cn.example.foods.composefoods.navigation.TopLevelDestination
import com.example.datastore.SettingsUiState
import com.example.datastore.SettingsViewModel
import com.example.designsystem.component.FoodsBackground
import com.example.designsystem.component.FoodsGradientBackground
import com.example.designsystem.component.SettingsClickBarExpandable
import com.example.designsystem.component.UserHeader
import com.example.designsystem.theme.LocalBackgroundTheme
import com.example.designsystem.theme.LocalGradientColors
import com.example.model.storagemodel.DarkThemeConfig
import com.example.model.storagemodel.ThemeBrand
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
