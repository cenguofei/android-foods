package com.example.search

/*
 * Copyright 2023 The Android Open Source Project
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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.designsystem.component.ErrorScreen
import com.example.designsystem.component.FoodsTopAppBar
import com.example.designsystem.component.SearchToolbar
import com.example.model.page.SearchResultUiState

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onInterestsClick: () -> Unit = {},
    onSearchTriggered: (String) -> Unit = {},
    searchViewModel: SearchViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val query = searchViewModel.searchQuery.collectAsState()
    val searchResultUiStateState = searchViewModel.searchResultUiState.collectAsState()

    SearchRoute(
        query = query,
        searchResultUiStateState = searchResultUiStateState,
        onSearchQueryChanged = searchViewModel::onSearchQueryChanged,
        onBack = onBack
    )
}

@Composable
private fun SearchRoute(
    query: State<String>,
    searchResultUiStateState: State<SearchResultUiState>,
    onSearchQueryChanged: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        FoodsTopAppBar(onBack = onBack)

        SearchToolbar(
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = { /*TODO*/ },
            searchQuery = query.value,
            onSearchQueryChanged = onSearchQueryChanged
        )

        when (searchResultUiStateState.value) {
            is SearchResultUiState.EmptyQuery -> {
                ShowEmpty(query = query.value)
            }

            is SearchResultUiState.LoadFailed -> {
                val message =
                    (searchResultUiStateState.value as SearchResultUiState.LoadFailed).e?.message
                        ?: "系统出错啦！"
                ErrorScreen(shouldShowAnim = false, description ="$message,\uD83E\uDD79")
            }

            is SearchResultUiState.Success -> {
                val pageList =
                    (searchResultUiStateState.value as SearchResultUiState.Success).pageList
                SearchSuccess(pageList = pageList)
            }

            is SearchResultUiState.PendingSearch -> { }
        }
    }
}


@Preview
@Composable
fun SearchPreview() {
    SearchScreen()
}