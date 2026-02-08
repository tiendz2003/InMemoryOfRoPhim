package com.manutd.ronaldo.network

import com.manutd.ronaldo.network.model.HomeResponse

interface RoNetworkDataSource {
    suspend fun getHomeData(onError: (String?) -> Unit): HomeResponse
}