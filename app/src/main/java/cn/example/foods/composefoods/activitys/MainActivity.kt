package cn.example.foods.composefoods.activitys

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import cn.example.foods.composefoods.FoodsApp
import cn.example.foods.composefoods.MainActivityUiState
import cn.example.foods.composefoods.MainActivityViewModel
import cn.example.foods.composefoods.datasource.SourceContainer
import cn.example.foods.logv
import com.example.designsystem.theme.FoodsTheme
import com.example.model.DarkThemeConfig
import com.example.model.ThemeBrand
import com.example.network.netstate.NetworkMonitor
import com.example.start.StartScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var sourceContainer: SourceContainer

    private val viewModel: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val uiState: MainActivityUiState by remember {
                mutableStateOf(MainActivityUiState.Loading)
            }

            val systemUiController = rememberSystemUiController()
            val darkTheme = shouldUseDarkTheme(uiState)

            LaunchedEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                systemUiController.setSystemBarsColor(Color.Transparent,darkIcons = !darkTheme)
            }

            CompositionLocalProvider {
                FoodsTheme(
                    darkTheme = darkTheme,
                    androidTheme = shouldUseAndroidTheme(uiState),
                    disableDynamicTheming = shouldDisableDynamicTheming(uiState),
                ) {
                    val isFirstUse = sourceContainer.userSettings.isFirstUse.collectAsState()

                    "${isFirstUse.value}".logv("start_screen")

                    Crossfade(isFirstUse.value) {
                        if (it) {
                            val updateUserUseState = {
                                sourceContainer.userSettings.updateUseState(false)
                            }
                            StartScreen(
                                onSignUpClick = {
                                    updateUserUseState()
                                },
                                onBeginClick = {
                                    updateUserUseState()
                                }
                            )
                        } else {
                            FoodsApp(
                                networkMonitor = networkMonitor,
                                windowSizeClass = calculateWindowSizeClass(this),
                                sourceContainer = sourceContainer,
                            )
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
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> when (uiState.userData.themeBrand) {
        ThemeBrand.DEFAULT -> false
        ThemeBrand.ANDROID -> true
    }
}

/**
 * Returns `true` if the dynamic color is disabled, as a function of the [uiState].
 */
@Composable
private fun shouldDisableDynamicTheming(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> !uiState.userData.useDynamicColor
}

/**
 * Returns `true` if dark theme should be used, as a function of the [uiState] and the
 * current system context.
 */
@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> when (uiState.userData.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}