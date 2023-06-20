package com.example.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import com.example.common.di.Dispatcher
import com.example.common.di.FoodsDispatchers
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.NetworkResult
import com.example.model.remoteModel.User
import com.example.network.remote.repository.ApiParam
import com.example.network.remote.repository.FoodRepository
import com.example.network.remote.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val userRepository: UserRepository,
    @Dispatcher(FoodsDispatchers.IO) private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    companion object {
        const val SELLER_TO_FOODS_KEY = "seller_to_foods"

        const val SELLERS_MAP_BY_ID = "sellers_map_by_id"

        //点击首页后传递给商家详情页的参数
        var foods: List<Food> = listOf()
        var seller: User = User.NONE
    }

    val sellerToFoods: MutableStateFlow<NetworkResult<Map<User, List<Food>>>> =
        MutableStateFlow(NetworkResult.Loading())

    init {
        Log.v("cgf","HomeViewModel RemoteRepository->$foodRepository")
        Log.v("test_home","1 savedStateHandle get  sellerToFoods.value=${sellerToFoods.value}")

//        val default = NetworkResult.Loading<Map<User, List<Food>>>()
//        val stateFlow = (savedStateHandle.getStateFlow(SELLER_TO_FOODS_KEY,default) as StateFlow<NetworkResult<Map<User,List<Food>>>>)
//        viewModelScope.launch {
//            sellerToFoods.emit(stateFlow.value)
//        }
        Log.v("test_home","2 savedStateHandle get sellerToFoods.value=${sellerToFoods.value}")
        getAllFoods()
    }

    @OptIn(SavedStateHandleSaveableApi::class)
    fun getAllFoods() {
        viewModelScope.launch(dispatcher) {
            try {
                val allUser = async { userRepository.getAllUser() }
                foodRepository.getAllFood()
                    .catch {
                        Log.v("cgf", "错误：" + it.message + ", " + it.cause)
                        sellerToFoods.emit(NetworkResult.Error(it.cause))
                    }
                    .collect {
                        val foodList = it
                        val users = allUser.await()
                        Log.v("cgf","wait users:$users")
                        Log.v("cgf", "getAllFoods foods:$foodList")
                        val map = users.map { user ->
                            user.headImg = ApiParam.IMAGE_USER_URL + user.headImg
                            val filter = foodList.filter { food: Food ->
                                food.createUserId == user.id
                            }
                            user to filter
                        }.toMap()
                        Log.v("cgf", "getAllUser users:$users")
                        val result = NetworkResult.Success(map)
                        sellerToFoods.emit(result)
//                        savedStateHandle[SELLER_TO_FOODS_KEY] = result
//
//                        viewModelScope.launch {
//                            while (true) {
//                                delay(1000)
//
//                                val default = NetworkResult.Loading<Map<User, List<Food>>>()
//                                val stateFlow = (savedStateHandle.getStateFlow(SELLER_TO_FOODS_KEY,default) as StateFlow<NetworkResult<Map<User,List<Food>>>>)
//
//                                Log.v("test_home",
//                                    "3 savedStateHandle get after save sellerToFoods.value=${stateFlow.value}")
//                            }
//                        }
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

