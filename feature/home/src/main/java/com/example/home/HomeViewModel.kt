package com.example.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.di.Dispatcher
import com.example.common.di.FoodsDispatchers
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.NetworkResult
import com.example.model.remoteModel.User
import com.example.network.remote.repository.ApiParam
import com.example.network.remote.repository.FoodRepository
import com.example.network.remote.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

data class SellerHolder(
    val foods:List<Food>,
    val seller:User,
    val clickedFood: Food = Food.NONE
)
class HomeViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val userRepository: UserRepository,
    @Dispatcher(FoodsDispatchers.IO) private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    /**
     * 客户点击首页后跳转至商家详情页面应该显示的商家信息
     */
    private val sellerHolder:MutableState<SellerHolder> = mutableStateOf(SellerHolder(listOf(),User.NONE))
    //点击首页后传递给商家详情页的参数
    val foods: List<Food> get() =  sellerHolder.value.foods
    val seller: User get() =  sellerHolder.value.seller
    val clickedFood:Food get() =  sellerHolder.value.clickedFood
    fun setNewSellerHolder(foods: List<Food>,seller:User) {
        sellerHolder.value = SellerHolder(foods,seller)
    }

    /**
     * 导航到商品详情页
     */
    fun updateClickedFood(clickedFood: Food) {
        sellerHolder.value = sellerHolder.value.copy(clickedFood = clickedFood)
    }

    val homeUiState: MutableStateFlow<NetworkResult<Any>> = MutableStateFlow(NetworkResult.Loading())

    /**
     * 根据商家获取该商家的 foods
     */
    var userToFoodsMap: SnapshotStateMap<User, List<Food>> = mutableStateMapOf()
        private set

    /**
     * 根据商家获取对应的foods
     * 从首页店家商家card导航到商家详情页使用
     */
    fun sellerFoods(seller: User) : List<Food> = userToFoodsMap[seller] ?: listOf()

    /**
     * HomeScreen展示的内容，
     * 只展示每一个商家的一个Food
     */
    var showFoods: SnapshotStateList<Pair<User, Food>> = mutableStateListOf()
        private set

    fun getAllFoods() {
        if (homeUiState.value is NetworkResult.Success) {
            return
        }
        viewModelScope.launch(dispatcher) {
            try {
                val allUser = async { userRepository.getAllUser() }
                foodRepository.getAllFood()
                    .catch {
                        Log.v("cgf", "错误：" + it.message + ", " + it.cause)
                        homeUiState.emit(NetworkResult.Error(it.cause))
                    }
                    .collect {
                        val foodList = it.map { m -> m.copy(count = 0) }

                        val users = allUser.await()
                        val map = users.map { user ->
                            user.headImg = ApiParam.IMAGE_USER_URL + user.headImg
                            user.score = Random.nextInt(0, 9).toDouble()
                            val filter = foodList.filter { food: Food ->
                                food.createUserId == user.id
                            }
                            user to filter
                        }.filter { pair ->
                            pair.second.isNotEmpty()
                        }.toMap().toMutableMap()

                        userToFoodsMap.clear()
                        showFoods.clear()

                        userToFoodsMap.putAll(map)
                        userToFoodsMap.forEach { entry ->
                            if (entry.value.isNotEmpty()) {
                                showFoods.add(entry.key to entry.value.random())
                            } else {
                                userToFoodsMap -= entry.key
                            }
                        }
                        showFoods.sortBy { p -> p.first.canteenName }
                        val find = showFoods.find { pair -> pair.first.canteenName == "麦当劳" }
                        val find2 = showFoods.find { pair -> pair.first.canteenName == "星巴克" }
                        val find3 = showFoods.find { pair -> pair.first.canteenName == "大董茶饮" }
                        val find4 = showFoods.find { pair -> pair.first.canteenName == "坦克" }
                        if (find != null) {
                            showFoods.remove(find)
                            showFoods.add(0,find)
                        }
                        if (find2 != null) {
                            showFoods.remove(find2)
                            showFoods.add(0,find2)
                        }
                        if (find3 != null) {
                            showFoods.remove(find3)
                            showFoods.add(0,find3)
                        }
                        if (find4 != null) {
                            showFoods.remove(find4)
                            showFoods.add(0,find4)
                        }

                        homeUiState.emit(NetworkResult.Success(Any()))
                    }
            } catch (e: Exception) {
                homeUiState.emit(NetworkResult.Error(e))
                e.printStackTrace()
            }
        }
    }
}