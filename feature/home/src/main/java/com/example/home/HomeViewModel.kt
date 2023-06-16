package com.example.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.common.di.Dispatcher
import com.example.common.di.FoodsDispatchers
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.NetworkResult
import com.example.model.remoteModel.User
import com.example.network.remote.repository.RemoteApi
import com.example.network.remote.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    @Dispatcher(FoodsDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val FOODS_WITH_SELLER = "foods_with_seller"

        const val SELLERS_MAP_BY_ID = "sellers_map_by_id"
    }



    private var _foods: MutableStateFlow<NetworkResult<Map<Int, List<Food>>>> =
        MutableStateFlow(NetworkResult.Loading())
    val foods = _foods

    var sellerWithFoodsMap = mapOf<Int,List<Food>>()
        private set
    private var sellers = listOf<User?>()

    init {
        getAllFoods()
        sellerWithFoodsMap = savedStateHandle.get<Map<Int, List<Food>>>(FOODS_WITH_SELLER) ?: mapOf()
        sellers = savedStateHandle.get<List<User?>>(SELLERS_MAP_BY_ID) ?: listOf()
    }

    @OptIn(SavedStateHandleSaveableApi::class)
    private fun getAllFoods() {
        viewModelScope.launch(dispatcher) {
            try {
                val users = async { remoteRepository.getAllUser() }
                remoteRepository.getAllFood()
                    .distinctUntilChangedBy {
                        it.map { food -> food.id }
                    }
                    .catch {
                        Log.v("cgf","错误："+it.message+", "+it.cause)
                        _foods.emit(NetworkResult.Error(it.cause))
                    }
                    .collect {
                        val foodList =
                            it/*.map { food -> food.copy(foodPic = RemoteApi.IMAGE_BASE_URL + food.foodPic) }*/
                        Log.v("FoodImage_test","foods:$foodList")
                        val netResult = it.groupBy { food ->
                            food.foodType
                        }
                        users.await().collect { m ->
                            sellers = m
                            savedStateHandle.saveable {
                                mutableStateOf(m)
                            }
                            _foods.emit(NetworkResult.Success(netResult))
                        }
                        sellerWithFoodsMap = foodList.groupBy { food -> food.createUserId }

                        savedStateHandle.saveable {
                            mutableStateOf(sellerWithFoodsMap)
                        }
                    }
            } catch (e:Exception) {
                e.printStackTrace()
            }

        }
    }

    fun getSeller(createUserId: Int): User? {
        try {
            val user = sellers[createUserId]
            if (user != null) {
                return user
            }
            return null
        } catch (e:Exception) {
            return null
        }
    }
}

