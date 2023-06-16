package com.example.sellerdetail

import androidx.lifecycle.ViewModel
import com.example.network.remote.repository.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SellerDetailViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {

}