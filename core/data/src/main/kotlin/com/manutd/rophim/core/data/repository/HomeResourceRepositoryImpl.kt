package com.manutd.rophim.core.data.repository

import com.manutd.ronaldo.network.RoNetworkDataSource
import com.manutd.ronaldo.network.model.HomeData
import com.manutd.rophim.core.data.utils.FakeDataProvider
import javax.inject.Inject

class HomeResourceRepositoryImpl @Inject constructor(
    private val roNetworkDataSource: RoNetworkDataSource,
) : HomeResourceRepository {
    override suspend fun getHomeData(onError: (String?) -> Unit): HomeData {
        val homeData = FakeDataProvider.getHomeData()
        return homeData
    }
}