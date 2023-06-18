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
        const val SELLER_TO_FOODS = "seller_to_foods"

        const val SELLERS_MAP_BY_ID = "sellers_map_by_id"

        //点击首页后传递给商家详情页的参数
        var foods: List<Food> = listOf()
        var seller: User = User.NONE
    }

    val sellerToFoods: MutableStateFlow<NetworkResult<Map<User, List<Food>>>> =
        MutableStateFlow(NetworkResult.Loading())

    init {
        sellerToFoods.value =
            savedStateHandle.get<NetworkResult<Map<User, List<Food>>>>(SELLER_TO_FOODS)
                ?: NetworkResult.Loading()
        getAllFoods()
    }

    @OptIn(SavedStateHandleSaveableApi::class)
    fun getAllFoods() {
        viewModelScope.launch(dispatcher) {
            try {
                val allUser = async { remoteRepository.getAllUser() }
                remoteRepository.getAllFood()
                    .distinctUntilChangedBy {
                        it.map { food -> food.id }
                    }
                    .catch {
                        Log.v("cgf", "错误：" + it.message + ", " + it.cause)
                        sellerToFoods.emit(NetworkResult.Error(it.cause))
                    }
                    .collect {
                        val foodList = it
                        /*.map { food -> food.copy(foodPic = RemoteApi.IMAGE_BASE_URL + food.foodPic) }*/
                        val users = allUser.await()
                        Log.v("cgf","wait users:$users")
//                        val usersNotNull = users.filter {
//                            it != null && it.id != null && it.sex != null && it.foodType != null && it.username != null && it.password != null && it.tel != null && it.headImg != null && it.img != null && it.canteenName != null && it.foodType != null
//                        }
                        Log.v("cgf", "getAllFoods foods:$foodList")
                        val map = users.map { user ->
                            user.headImg = RemoteApi.IMAGE_USER_URL + user.headImg
                            val filter = foodList.filter { food: Food ->
                                food.createUserId == user.id
                            }
                            user to filter
                        }.toMap()
                        Log.v("cgf", "getAllUser users:$users")
                        val result = NetworkResult.Success(map)
                        sellerToFoods.emit(result)
                        savedStateHandle.saveable {
                            mutableStateOf(result)
                        }
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

