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
import androidx.compose.material3.MaterialTheme
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
import com.example.datastore.SettingsUiState
import com.example.datastore.SettingsViewModel
import com.example.designsystem.component.FoodsBackground
import com.example.designsystem.component.FoodsGradientBackground
import com.example.designsystem.component.SettingsClickBarExpandable
import com.example.designsystem.component.UserHeader
import com.example.designsystem.theme.LocalBackgroundTheme
import com.example.designsystem.theme.LocalGradientColors
import com.example.model.remoteModel.User
import com.example.model.storagemodel.DarkThemeConfig
import com.example.model.storagemodel.ThemeBrand

@Composable
fun DrawerContent(
    settingsViewModel: SettingsViewModel,
    user: User,
    onLogin: () -> Unit,
    onSeeMyOrder: () -> Unit,
    onSeeMyFavorites: () -> Unit,
) {
    FoodsBackground(
        modifier = Modifier
            .fillMaxHeight()
            .width(LocalConfiguration.current.screenWidthDp.dp - 100.dp),
        shape = RoundedCornerShape(topEnd = 50f, bottomEnd = 50f)
    ) {
        FoodsGradientBackground(gradientColors = LocalGradientColors.current) {
//            Surface(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(LocalConfiguration.current.screenWidthDp.dp - 100.dp),
//                shape = RoundedCornerShape(topEnd = 50f, bottomEnd = 50f),
//                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
//                else LocalBackgroundTheme.current.color,
            //TODO error when set tonalElevation:because it is Unspecified default
//        tonalElevation = LocalBackgroundTheme.current.tonalElevation
//            ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier
                        .systemBarsPadding()
                        .padding(8.dp)
                        .clickable(onClick = onLogin),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UserHeader(model = user.headImg)
                    Text(text = user.username, modifier = Modifier.padding(start = 8.dp))
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(
                            if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.5f
                            )
                            else LocalBackgroundTheme.current.color
                        )
                ) {
                    Column {
                        val settingsUiStateState =
                            settingsViewModel.settingsUiState.collectAsState()

                        val shouldShowThemeSelections = remember { mutableStateOf(false) }
                        SettingsClickBarExpandable(
                            shouldShow = shouldShowThemeSelections,
                            text = "Choose Theme",
                            startIcon = Icons.Default.DarkMode,
                            endIcon = Icons.Default.ArrowDownward
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        VisibilityThemeSelections(
                            shouldShowThemeSelections = shouldShowThemeSelections,
                            onThemeSelected = {
                                settingsViewModel.updateThemeBrand(it)
                            },
                            onIsDynamicSelected = {
                                settingsViewModel.updateDynamicColorPreference(it)
                            },
                            onModeSelected = {
                                settingsViewModel.updateDarkThemeConfig(it)
                            },
                            settingsUiStateState = settingsUiStateState
                        )

                        SettingsClickBarExpandable(
                            text = "我的邮箱",
                            startIcon = Icons.Default.Mail
                        )
                        SettingsClickBarExpandable(
                            text = "我的电话",
                            startIcon = Icons.Default.Call
                        )
                        SettingsClickBarExpandable(
                            text = "我的订单",
                            startIcon = Icons.Default.ListAlt,
                            onClick = onSeeMyOrder
                        )
                        SettingsClickBarExpandable(
                            text = "我的喜欢",
                            startIcon = Icons.Default.StarBorder,
                            onClick = onSeeMyFavorites
                        )
                        SettingsClickBarExpandable(
                            text = "我的邮箱",
                            startIcon = Icons.Default.Logout
                        )
                    }
                }
            }
        }
//        }
    }
}

@Composable
private fun VisibilityThemeSelections(
    shouldShowThemeSelections: MutableState<Boolean>,
    onThemeSelected: (ThemeBrand) -> Unit,
    onIsDynamicSelected: (Boolean) -> Unit,
    onModeSelected: (DarkThemeConfig) -> Unit,
    settingsUiStateState: State<SettingsUiState>,
) {
    /**
     * Theme:
     *      Default
     *      Android
     *
     * Use Dynamic Color: [enabled = Theme == Default]
     *      Yes
     *      No
     *
     * Dark mode preference:
     *      System default
     *      Light
     *      Dark
     *
     * Choices:
     * 1. Theme.Android + {System default, Light, Dark}
     *
     * 2.1 Theme.Default + No + {System default, Light, Dark}
     * 2.2 Theme.Default + Yes + {System default, Light, Dark}
     *
     * 共 3x3=9 种情况，实际主题组合只有 9-3=6 种
     */
    /**
     * Theme:
     *      Default
     *      Android
     *
     * Use Dynamic Color: [enabled = Theme == Default]
     *      Yes
     *      No
     *
     * Dark mode preference:
     *      System default
     *      Light
     *      Dark
     *
     * Choices:
     * 1. Theme.Android + {System default, Light, Dark}
     *
     * 2.1 Theme.Default + No + {System default, Light, Dark}
     * 2.2 Theme.Default + Yes + {System default, Light, Dark}
     *
     * 共 3x3=9 种情况，实际主题组合只有 9-3=6 种
     */
    AnimatedVisibility(
        visible = shouldShowThemeSelections.value,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(LocalBackgroundTheme.current.color)
        ) {
            val selected =
                remember {
                    mutableStateOf(
                        if (settingsUiStateState.value is SettingsUiState.Loading) ThemeBrand.DEFAULT
                        else (settingsUiStateState.value as SettingsUiState.Success).settings.brand
                    )
                }
            ThemeSelectionChipText(
                onThemeSelected = onThemeSelected,
                selected = selected
            )

            UseDynamicColorSelectionChipText(
                onIsDynamicSelected = onIsDynamicSelected,
                enabled = selected.value == ThemeBrand.DEFAULT,
                userPreSelectedYes = if (settingsUiStateState.value is SettingsUiState.Loading) true
                else (settingsUiStateState.value as SettingsUiState.Success).settings.useDynamicColor
            )

            val preMode =
                if (settingsUiStateState.value is SettingsUiState.Loading) DarkThemeConfig.FOLLOW_SYSTEM
                else (settingsUiStateState.value as SettingsUiState.Success).settings.darkThemeConfig
            DarkModePreferenceSelectionChipText(onModeSelected = onModeSelected, preMode = preMode)
        }
    }
}

@Composable
fun DarkModePreferenceSelectionChipText(
    onModeSelected: (DarkThemeConfig) -> Unit,
    preMode: DarkThemeConfig
) {
    var selected by remember {
        mutableStateOf(
            preMode
        )
    }
    Text(
        text = stringResource(R.string.dark_mode_preference),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 8.dp)
    )
    Row(modifier = Modifier.padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected == DarkThemeConfig.FOLLOW_SYSTEM,
            onClick = {
                selected = DarkThemeConfig.FOLLOW_SYSTEM
                onModeSelected(DarkThemeConfig.FOLLOW_SYSTEM)
            }
        )
        Text(
            text = "Follow System",
            modifier = Modifier
                .clickable(onClick = {
                    selected = DarkThemeConfig.FOLLOW_SYSTEM
                    onModeSelected(DarkThemeConfig.FOLLOW_SYSTEM)
                })
                .padding(start = 4.dp)
        )
    }
    Spacer(modifier = Modifier.size(4.dp))

    Row(modifier = Modifier.padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected == DarkThemeConfig.LIGHT,
            onClick = {
                selected = DarkThemeConfig.LIGHT
                onModeSelected(DarkThemeConfig.LIGHT)
            }
        )
        Text(
            text = "Light",
            modifier = Modifier
                .clickable(onClick = {
                    selected = DarkThemeConfig.LIGHT
                    onModeSelected(DarkThemeConfig.LIGHT)
                })
                .padding(start = 4.dp)
        )
    }
    Spacer(modifier = Modifier.size(4.dp))


    Row(modifier = Modifier.padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected == DarkThemeConfig.DARK,
            onClick = {
                selected = DarkThemeConfig.DARK
                onModeSelected(DarkThemeConfig.DARK)
            }
        )
        Text(
            text = "Dark",
            modifier = Modifier
                .clickable(onClick = {
                    selected = DarkThemeConfig.DARK
                    onModeSelected(DarkThemeConfig.DARK)
                })
                .padding(start = 4.dp)
        )
    }
    Spacer(modifier = Modifier.size(4.dp))
}

@Composable
fun UseDynamicColorSelectionChipText(
    onIsDynamicSelected: (Boolean) -> Unit,
    enabled: Boolean,
    userPreSelectedYes: Boolean
) {
    var selectedYes by remember { mutableStateOf(userPreSelectedYes) }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (enabled) Color.Transparent else Color.Gray.copy(0.8f))
    ) {
        val onClick = {
            selectedYes = !selectedYes
            onIsDynamicSelected(selectedYes)
        }
        Column {
            Text(
                text = stringResource(R.string.use_dynamic_color),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
            Row(
                modifier = Modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    enabled = enabled,
                    selected = selectedYes,
                    onClick = onClick
                )
                TextButton(
                    onClick = onClick,
                    enabled = enabled,
                    contentPadding = PaddingValues(start = 8.dp)
                ) {
                    Text(text = "Yes")
                }
            }
            Spacer(modifier = Modifier.size(4.dp))

            Row(
                modifier = Modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    enabled = enabled,
                    selected = !selectedYes,
                    onClick = onClick
                )
                TextButton(
                    onClick = onClick,
                    enabled = enabled,
                    contentPadding = PaddingValues(start = 8.dp)
                ) {
                    Text(text = "No")
                }
            }
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}

@Composable
fun ThemeSelectionChipText(
    onThemeSelected: (ThemeBrand) -> Unit,
    selected: MutableState<ThemeBrand>,
) {
    Text(
        text = "Theme",
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 8.dp)
    )
    Row(modifier = Modifier.padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected.value == ThemeBrand.ANDROID,
            onClick = {
                selected.value = ThemeBrand.ANDROID
                onThemeSelected(ThemeBrand.ANDROID)
            }
        )
        Text(
            text = "Android",
            fontSize = 16.sp,
            modifier = Modifier
                .clickable(onClick = {
                    selected.value = ThemeBrand.ANDROID
                    onThemeSelected(ThemeBrand.ANDROID)
                })
                .padding(start = 4.dp)
        )
    }
    Spacer(modifier = Modifier.size(4.dp))
    Row(modifier = Modifier.padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = selected.value == ThemeBrand.DEFAULT,
            onClick = {
                selected.value = ThemeBrand.DEFAULT
                onThemeSelected(ThemeBrand.DEFAULT)
            }
        )
        Text(
            text = "Default",
            fontSize = 16.sp,
            modifier = Modifier
                .clickable(onClick = {
                    selected.value = ThemeBrand.DEFAULT
                    onThemeSelected(ThemeBrand.DEFAULT)
                })
                .padding(start = 4.dp)
        )
    }
    Spacer(modifier = Modifier.size(4.dp))
}

@Preview
@Composable
private fun PreviewSelectionBar1() {
    SettingsClickBarExpandable(text = "Choose Theme")
}

@Preview
@Composable
private fun PreviewSelectionBar2() {
    SettingsClickBarExpandable(text = "Choose Theme")
}