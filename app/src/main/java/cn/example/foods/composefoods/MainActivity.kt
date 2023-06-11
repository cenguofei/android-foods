package cn.example.foods.composefoods

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.designsystem.theme.FoodsTheme
import com.example.model.DarkThemeConfig
import com.example.model.ThemeBrand
import com.example.network.netstate.NetworkMonitor
import com.example.network.remote.model.User
import com.example.network.remote.repository.RemoteService
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var networkMonitor: NetworkMonitor

    @Inject lateinit var remoteService: RemoteService

    private val viewModel: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it
                    }
                    .collect { }
            }
        }

        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
//        splashScreen.setKeepOnScreenCondition {
//            when (uiState) {
//                Loading -> true
//                is Success -> false
//            }
//        }

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent {

            val systemUiController = rememberSystemUiController()
            val darkTheme = shouldUseDarkTheme(uiState)

            // Update the dark content of the system bars to match the theme
            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose {}
            }

            CompositionLocalProvider {
                FoodsTheme(
                    darkTheme = darkTheme,
                    androidTheme = shouldUseAndroidTheme(uiState),
                    disableDynamicTheming = shouldDisableDynamicTheming(uiState),
                ) {
//                    App(
//                        networkMonitor = networkMonitor,
//                        windowSizeClass = calculateWindowSizeClass(this),
////                        userNewsResourceRepository = userNewsResourceRepository,
//                    )
                    TestRemoteService(remoteService)
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


@Composable
fun TestRemoteService(remoteService: RemoteService) {

    val coroutineScope = rememberCoroutineScope()
    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TestButton("login") {
                coroutineScope.launch {
                    val hashMap = remoteService.login(User(username = "admin", password = "abc"))
                    hashMap.string()
                }
            }

            TestButton("register exist") {
                coroutineScope.launch {
                    val hashMap =
                        remoteService.register(User(username = "admin", password = "abc"))
                    hashMap.string()
                }
            }

            TestButton("register") {
                coroutineScope.launch {
                    val hashMap = remoteService.register(
                        User(
                            username = "aaa",
                            password = "aaa",
                            email = "aaa",
                            tel = "aaa"
                        )
                    )
                    hashMap.string()
                }
            }

            TestButton("getAllFood") {
                coroutineScope.launch {
                    val allFood = remoteService.getAllFood()
                    Log.v("http_test",allFood.toString())
                }
            }

            TestButton("getUserOrders") {
                coroutineScope.launch {
                    val allFood = remoteService.getUserOrders("admin")
                    Log.v("http_test",allFood.toString())
                }
            }
        }
    }
}

fun <K,V>HashMap<K,V>.string() {
    val builder = StringBuilder()
    builder.append("{")
    keys.forEach {
        val item = get(it)
        builder.append(it).append(":").append(item).append(",")
    }
    builder.append("}")
    Log.v("http_test",builder.toString())
}

@Composable
fun TestButton(text:String,onClick:()-> Unit) {
    OutlinedButton(onClick = onClick) {
        Text(text = text)
    }
}