package com.example.favorite

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.model.remoteModel.Favorite
import com.example.model.remoteModel.Food
import com.example.model.remoteModel.NetworkResult
import com.example.model.remoteModel.User
import com.example.network.remote.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val FAVORITES_KEY = "_my_favorites_key"
    }


    val favoriteFoodIds: SnapshotStateList<Long> = mutableStateListOf()

    private var _myFavorites: MutableStateFlow<NetworkResult<List<Favorite>>> =
        MutableStateFlow(NetworkResult.Loading())
    val myFavorites = _myFavorites

    init {
        _myFavorites.value = savedStateHandle.get<NetworkResult<List<Favorite>>>(FAVORITES_KEY)
            ?: NetworkResult.Loading()
    }

    @OptIn(SavedStateHandleSaveableApi::class)
    fun getFavorites(username: String) {
        viewModelScope.launch {
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
                    favoriteFoodIds.addAll(it.map { it.id })

                    savedStateHandle.saveable(FAVORITES_KEY) {
                        NetworkResult.Success(it)
                    }
                }
        }
    }

    fun deleteFavorite(
        id: Long,
        onError: onError,
        onSuccess: onSuccess
    ) {
        viewModelScope.launch {
            val result = favoriteRepository.deleteFavorite(id)
            val isSuccess = result["isSuccess"] as Boolean
            if (isSuccess) {
                onSuccess()
            } else {
                onError(result["msg"] as String)
            }
        }
    }

    fun addFavorite(
        currentUser: User,
        seller: User,
        food: Food,
        onError: onError,
        onSuccess: onSuccess
    ) {
        viewModelScope.launch {
            var score = Random.nextDouble(0.0,9.0).toInt().toDouble()
            if (Random.nextBoolean()) { score += 0.5 }
            val favorite = Favorite(
                foodId = food.id,
                username = currentUser.username,
                sellerId = seller.id,
                sellerPic = seller.headImg,
                canteenName = seller.canteenName,
                score = score,
                foodType = seller.foodType
            )
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