package cn.example.foods.composefoods.activitys

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import cn.example.foods.composefoods.navigation.TopLevelDestination
import cn.example.foods.composefoods.ui.FoodsAppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBackHandler(
    appState: FoodsAppState,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope
)  {
    var enabled by remember { mutableStateOf(true) }
    // Safely update the current `onBack` lambda when a new one is provided
    enabled = appState.currentTopLevelDestination == TopLevelDestination.HOME

    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher

    val onBack: (OnBackPressedCallback) -> Unit = { callback ->
        if (drawerState.isOpen) {
            coroutineScope.launch {
                drawerState.close()
            }
        } else {
            callback.remove()
            backDispatcher.onBackPressed()
            backDispatcher.addCallback(callback)
        }
    }

    val currentOnBack by rememberUpdatedState(onBack)
    // Remember in Composition a back callback that calls the `onBack` lambda
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack(this)
            }
        }
    }

    // On every successful composition, update the callback with the `enabled` value
    SideEffect {
        backCallback.isEnabled = enabled
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, backDispatcher) {
        // Add callback to the backDispatcher
        Log.v("navigation_events","添加OnBackPressedCallback")
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        // When the effect leaves the Composition, remove the callback
        onDispose {
            backCallback.remove()
        }
    }
}