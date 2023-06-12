package cn.example.foods.composefoods.activitys

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import cn.example.foods.composefoods.MainActivityUiState
import cn.example.foods.composefoods.MainActivityViewModel
import com.example.designsystem.theme.FoodsTheme
import com.example.model.DarkThemeConfig
import com.example.model.ThemeBrand
import com.example.network.remote.model.Order
import com.example.network.remote.model.OrderDetail
import com.example.network.remote.model.User
import com.example.network.remote.repository.RemoteRepository
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : ComponentActivity() {
    private val remote = RemoteRepository()

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val uiState: MainActivityUiState by remember {
                mutableStateOf(MainActivityUiState.Loading)
            }

            val systemUiController = rememberSystemUiController()
            val darkTheme = shouldUseDarkTheme(uiState)

            // Update the dark content of the system bars to match the theme
            DisposableEffect(systemUiController, darkTheme) {
//                systemUiController.systemBarsDarkContentEnabled = !darkTheme
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
                    TestRemoteService(remote)
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

@Preview
@Composable
fun Fix() {
    TestRemoteService(remoteService = RemoteRepository())
}

@Composable
fun TestRemoteService(remoteService: RemoteRepository) {
    Log.v("http_test","TestRemoteService")
    val coroutineScope = rememberCoroutineScope()
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Log.v("http_test","Scaffold")
        Column(modifier = Modifier
            .padding(paddingValues)
            .background(Color.Blue.copy(alpha = 0.5f))) {
            TestButton("login") {
                coroutineScope.launch {
                    val hashMap = remoteService.login(username = "cgf", password = "abc")
                    hashMap.string()
                }
            }

            TestButton("register exist") {
                coroutineScope.launch {
                    val hashMap =
                        remoteService.register(
                            User(username = "cgf", password = "abc", email = "qq.com", tel = "111")
                        )
                    hashMap.string()
                }
            }

            TestButton("postOrder") {
                coroutineScope.launch {
                    val postOrder = remoteService.postOrder(
                        order
                    )
                    postOrder.string()
                }
            }

            TestButton("register") {
                coroutineScope.launch {
                    val hashMap = remoteService.register(
                        User(
                            username = "chenguofei",
                            password = "aaa",
                            email = "feifei@qq.com",
                            tel = "10086"
                        )
                    )
                    hashMap.string()
                }
            }

            TestButton("getAllFood") {
                coroutineScope.launch {
                    remoteService.getAllFood().collect {
                        Log.v("http_test",it.toString())
                    }
                }
            }

//            TestButton("postOrder") {
//                coroutineScope.launch {
//                    val allFood = remoteService.postOrder(Order(1000L,"111",""))
//                    Log.v("http_test",allFood.toString())
//                }
//            }

            TestButton("getUserOrders") {
                coroutineScope.launch {
                    remoteService.getUserOrders("admin").collect { v ->
                        v.string()
                    }
                }
            }
        }
    }
}
fun <K,V>Map<K,V>.string() {
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
    Button(onClick = onClick,modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        Text(text = text,color = Color.Black)
    }
}

val order = Order(
    999L, "999", "1", null, 50.0, "address", "cgf", "10086",
    listOf(
        OrderDetail(
            888L,"小菜",2.1,"2222222",1
        )
    )
)