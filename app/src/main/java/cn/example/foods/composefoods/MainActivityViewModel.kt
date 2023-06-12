package cn.example.foods.composefoods

import androidx.lifecycle.ViewModel
import com.example.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(

) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> =  MutableStateFlow(MainActivityUiState.Loading)
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}
