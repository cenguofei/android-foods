package cn.example.foods.composefoods.activitys

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import cn.example.foods.composefoods.datasource.SourceContainer
import cn.example.foods.composefoods.navigation.Screens
import cn.example.foods.composefoods.ui.FoodsApp
import cn.example.foods.composefoods.ui.FoodsAppState
import cn.example.foods.composefoods.ui.rememberFoodsAppState
import com.example.common.di.MainViewModel
import com.example.datastore.SettingsUiState
import com.example.datastore.SettingsViewModel
import com.example.model.storagemodel.DarkThemeConfig
import com.example.model.storagemodel.ThemeBrand
import com.example.designsystem.theme.FoodsTheme
import com.example.network.netstate.NetworkMonitor
import com.example.start.splash.FoodsSplashScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var sourceContainer: SourceContainer

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    @Inject
    lateinit var mainViewModel: MainViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val uiState: State<SettingsUiState> = settingsViewModel.settingsUiState.collectAsState()

            val systemUiController = rememberSystemUiController()
            val darkTheme = shouldUseDarkTheme(uiState.value)

            LaunchedEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = !darkTheme)
            }

            FoodsTheme(
                darkTheme = darkTheme,
                androidTheme = shouldUseAndroidTheme(uiState.value),
                disableDynamicTheming = shouldDisableDynamicTheming(uiState.value),
            ) {
                val navController = rememberNavController()
                val appState: FoodsAppState = rememberFoodsAppState(
                    networkMonitor = networkMonitor,
                    windowSizeClass = calculateWindowSizeClass(this),
                    sourceContainer = sourceContainer,
                    settingsViewModel = settingsViewModel,
                    navController = navController,
                    mainViewModel = mainViewModel
                )
                Crossfade(uiState) { settingsState ->
                    when (settingsState.value) {
                        is SettingsUiState.Loading -> {
                            FoodsSplashScreen()
                        }

                        is SettingsUiState.Success -> {
                            if ((settingsState.value as SettingsUiState.Success).settings.isFirstUse) {
                                FoodsApp(appState = appState, startScreen = Screens.Start)
                            } else {
                                FoodsApp(appState = appState, startScreen = Screens.Home)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Returns `true` if the Android theme should be used, as a function of the [uiState].
 */
@Composable
private fun shouldUseAndroidTheme(
    uiState: SettingsUiState,
): Boolean = when (uiState) {
    SettingsUiState.Loading -> false
    is SettingsUiState.Success -> when (uiState.settings.brand) {
        ThemeBrand.DEFAULT -> false
        ThemeBrand.ANDROID -> true
    }
}

/**
 * Returns `true` if the dynamic color is disabled, as a function of the [uiState].
 */
@Composable
private fun shouldDisableDynamicTheming(
    uiState: SettingsUiState,
): Boolean = when (uiState) {
    SettingsUiState.Loading -> false
    is SettingsUiState.Success -> !uiState.settings.useDynamicColor
}

/**
 * Returns `true` if dark theme should be used, as a function of the [uiState] and the
 * current system context.
 */
@Composable
private fun shouldUseDarkTheme(
    uiState: SettingsUiState,
): Boolean = when (uiState) {
    SettingsUiState.Loading -> isSystemInDarkTheme()
    is SettingsUiState.Success -> when (uiState.settings.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}