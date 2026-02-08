package com.manutd.ronaldo.network

import androidx.annotation.WorkerThread
import com.manutd.ronaldo.network.model.HomeData

interface RoNetworkDataSource {
    @WorkerThread
    suspend fun getMovies(onError: (String?) -> Unit): HomeData
}