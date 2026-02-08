package com.manutd.rophim.core.data.repository

import com.manutd.ronaldo.network.model.HomeData

interface HomeResourceRepository {
    //trả về danh sách dữ liệu của màn home
    suspend fun getHomeData(onError: (String?) -> Unit): HomeData
}
