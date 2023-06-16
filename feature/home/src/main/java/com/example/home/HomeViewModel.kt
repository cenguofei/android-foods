package com.example.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.di.Dispatcher
import com.example.common.di.FoodsDispatchers
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.NetworkResult
import com.example.network.remote.repository.RemoteApi
import com.example.network.remote.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    @Dispatcher(FoodsDispatchers.IO) private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    init {
        getAllFoods()
    }

    private var _foods: MutableStateFlow<NetworkResult<Map<String, List<Food>>>> =
        MutableStateFlow(NetworkResult.Loading())
    val foods = _foods

    private fun getAllFoods() {
        viewModelScope.launch(dispatcher) {
            remoteRepository.getAllFood()
                .distinctUntilChangedBy {
                    it.map { food -> food.id }
                }
                .catch {
                    _foods.emit(NetworkResult.Error(it.cause))
                }
                .collect {
                    val foodList =
                        it.map { food -> food.copy(foodPic = RemoteApi.IMAGE_BASE_URL + food.foodPic) }
                    Log.v("FoodImage_test","foods:$foodList")
                    val netResult = it.groupBy { food ->
                        food.foodType
                    }
                    _foods.emit(NetworkResult.Success(netResult))
                }
        }
    }
}

