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

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.page.PageList
import com.example.model.page.SearchResultUiState
import com.example.network.remote.repository.ApiParam
import com.example.network.remote.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val foodRepository: FoodRepository
) : ViewModel() {

    val searchQuery = savedStateHandle.getStateFlow(SEARCH_QUERY, "")

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[SEARCH_QUERY] = query
    }

    val searchResultUiState: StateFlow<SearchResultUiState> = channelFlow {
        searchQuery.collectLatest { query ->
            if (query.isNotEmpty()) {
                foodRepository.queryLike(query)
                    .catch {
                        Log.v("cgf", "查询失败了:${it.message}")
                        it.printStackTrace()
                        trySend(SearchResultUiState.LoadFailed(it))
                    }
                    .collect {
                        Log.v("cgf", "查询成功：$it")
                        val map = it.rows.map { food ->
                            food.copy(foodPic = ApiParam.IMAGE_FOOD_URL + food.foodPic)
                        }
                        val pageList = PageList(total = it.total, rows = map)
                        if (it.isEmpty()) {
                            trySend(SearchResultUiState.EmptyQuery)
                        } else {
                            trySend(SearchResultUiState.Success(pageList))
                        }
                    }
            } else {
                trySend(SearchResultUiState.PendingSearch)
            }
        }
        awaitClose { close() }
    }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = SearchResultUiState.EmptyQuery
        )
}

private const val SEARCH_QUERY = "searchQuery"
