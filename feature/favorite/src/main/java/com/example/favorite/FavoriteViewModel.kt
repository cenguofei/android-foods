package com.example.favorite

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.remoteModel.Favorite
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.NetworkResult
import com.example.model.remoteModel.User
import com.example.network.remote.repository.FavoriteRepository
import com.example.network.remote.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    private var _myFavorites: MutableStateFlow<NetworkResult<List<Favorite>>> =
        MutableStateFlow(NetworkResult.Loading())
    val myFavorites: StateFlow<NetworkResult<List<Favorite>>> = _myFavorites

    /**
     * 存储food的id
     */
    val favoriteFoodIds: SnapshotStateList<Long> = mutableStateListOf()
    val favorites: SnapshotStateList<Favorite> = mutableStateListOf()

    fun foodInFavorites(food: Food): Boolean =
        favorites.firstOrNull { it.foodId == food.id } != null

    fun getFavorites(username: String) {
        viewModelScope.launch {
            try {
                favoriteRepository.getAllFavorites(username)
                    .catch {
                        Log.v("cgf", "获取用户喜欢出错：${it.message}")
                        it.printStackTrace()
                        _myFavorites.emit(
                            NetworkResult.Error(Error(it.message))
                        )
                    }
                    .collect {
                        Log.v("cgf", "用户：$username 的收藏：$it")
                        _myFavorites.emit(NetworkResult.Success(it))
                        favoriteFoodIds.clear()
                        favoriteFoodIds.addAll(it.map { fa -> fa.id })
                        favorites.clear()
                        favorites.addAll(it)
                    }
            } catch (e: Exception) {
                _myFavorites.emit(NetworkResult.Error(e))
                e.printStackTrace()
            }
        }
    }

    fun deleteFavorite(
        onError: onError = {},
        onSuccess: onSuccess = {},
        food: Food,
        currentUser: User
    ) {
        favoriteFoodIds.remove(food.id)
        Log.v("myFavorites", "移除喜欢：$food \n当前喜欢：${favorites.toList()}")
        if (myFavorites.value is NetworkResult.Success) {
//            val favorite = favorites.firstOrNull { it.foodId == food.id } ?: return
            viewModelScope.launch {
                try {
                    val result = favoriteRepository.deleteFavorite(currentUser.username, food.id)
                    Log.v("myFavorites", "删除结果：$result")
                    val isSuccess = result["isSuccess"] as Boolean
                    if (isSuccess) {
                        onSuccess()
                    } else {
                        onError(result["msg"] as String)
                    }
                    favorites.removeIf { it.foodId == food.id }
                } catch (e:Exception) {
                    onError(e.message ?: "未知错误...")
                }
            }
        }
    }

    var id = Long.MIN_VALUE

    fun addFavorite(
        currentUser: User,
        seller: User,
        food: Food,
        onError: onError = {},
        onSuccess: onSuccess = {}
    ) {
        favoriteFoodIds.add(food.id)
        viewModelScope.launch {
            val favorite = Favorite(
                foodId = food.id,
                username = currentUser.username,
                sellerId = seller.id,
                sellerPic = seller.headImg,
                canteenName = seller.canteenName,
                score = seller.score,
                foodType = seller.foodType,
                foodName = food.foodName,
                foodPic = food.foodPic
            ).also {
                //服务端已经自动自增id，客户端为了不在添加Favorite的时候每次都更新id，所以模拟生成不一样的id
                //防止Lazy布局的时候使用id出错
                favorites.add(it.copy(id = id++))
            }
            try {
                val result = favoriteRepository.addFavorite(favorite)
                val isSuccess = result["isSuccess"] as Boolean
                if (isSuccess) {
                    onSuccess()
                } else {
                    onError(result["msg"] as String)
                }
            } catch (e:Exception) {
                onError(e.message ?: "未知错误...")
            }
        }
    }
}

typealias onSuccess = () -> Unit
typealias onError = (msg: String) -> Unit