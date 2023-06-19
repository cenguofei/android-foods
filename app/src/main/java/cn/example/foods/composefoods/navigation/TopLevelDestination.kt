/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.example.foods.composefoods.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import cn.example.foods.R
import com.example.designsystem.icon.FoodsIcons

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int? = null,
    @StringRes val titleTextId: Int? = null,
) {
    HOME(
        unselectedIcon = FoodsIcons.Home,
        selectedIcon = FoodsIcons.HomeFilled
    ),
    NONE(
        unselectedIcon = FoodsIcons.Grid3x3,
        selectedIcon = FoodsIcons.Grid3x3
    )
}

