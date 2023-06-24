package com.example.model.page

import com.example.model.remoteModel.Food

sealed interface SearchResultUiState {
    object PendingSearch : SearchResultUiState

    /**
     * The state query is empty or too short. To distinguish the state between the
     * (initial state or when the search query is cleared) vs the state where no search
     * result is returned, explicitly define the empty query state.
     */
    object EmptyQuery : SearchResultUiState

    data class LoadFailed(val e:Throwable?) : SearchResultUiState

    data class Success(
        val pageList: PageList<Food> = emptyPageList(),
    ) : SearchResultUiState
}
