package com.example.favorite

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.remoteModel.Favorite
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.NetworkResult
import com.example.model.remoteModel.User
import com.example.network.remote.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    companion object {
        private const val FAVORITES_KEY = "_my_favorites_key"
    }


    private var _myFavorites: MutableStateFlow<NetworkResult<List<Favorite>>> =
        MutableStateFlow(NetworkResult.Loading())
    val myFavorites: StateFlow<NetworkResult<List<Favorite>>> = _myFavorites

    /**
     * 存储food的id
     */
    val favoriteFoodIds: SnapshotStateList<Long> = mutableStateListOf()
    private val favorites: SnapshotStateList<Favorite> = mutableStateListOf()

    fun foodInFavorites(food: Food) : Boolean =
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
            } catch (e:Exception) {
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

        Log.v("myFavorites","移除喜欢：$food")
        Log.v("myFavorites","当前喜欢：${favorites.toList()}")
        if (myFavorites.value is NetworkResult.Success) {
            Log.v("myFavorites","myFavorites.value is NetworkResult.Success")
            val favorite = favorites.firstOrNull { it.foodId == food.id } ?: return
            Log.v("myFavorites","开始移除喜欢：$favorite")
            viewModelScope.launch {
                val result = favoriteRepository.deleteFavorite(currentUser.username,food.id)
                Log.v("myFavorites","删除结果：$result")
                val isSuccess = result["isSuccess"] as Boolean
                if (isSuccess) {
                    onSuccess()
                } else {
                    onError(result["msg"] as String)
                }

                favorites.removeIf { it.foodId == food.id }
            }
        } else {
            Log.v("myFavorites","删除喜欢，$food 失败")
        }
    }

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
                foodType = seller.foodType
            ).also { favorites.add(it) }
            val result = favoriteRepository.addFavorite(favorite)
            val isSuccess = result["isSuccess"] as Boolean
            if (isSuccess) {
                onSuccess()
            } else {
                onError(result["msg"] as String)
            }
        }
    }
}

typealias onSuccess = () -> Unit
typealias onError = (msg: String) -> Unit