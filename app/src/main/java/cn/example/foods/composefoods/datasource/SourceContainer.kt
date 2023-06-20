package cn.example.foods.composefoods.datasource

import com.example.network.remote.repository.FoodRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourceContainer @Inject constructor() {
    @Inject lateinit var remoteRepository: FoodRepository

}