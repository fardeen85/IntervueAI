package com.fardeen.intervueai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

class NetworkStatusViewModel(application: Application) : AndroidViewModel(application) {

    private val networkConnectivityObserver = NetworkConnectivityObserver(application)

    fun observeNetworkStatus(): Flow<NetworkStatus> {
        return networkConnectivityObserver.networkStatus
    }



}