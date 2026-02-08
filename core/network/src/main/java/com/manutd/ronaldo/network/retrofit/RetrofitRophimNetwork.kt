package com.manutd.ronaldo.network.retrofit

import com.manutd.ronaldo.network.RoNetworkDataSource
import com.manutd.ronaldo.network.model.HomeResponse
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

internal interface RetrofitRoNetworkApi {
    @GET(".")
    suspend fun getMovies(): ApiResponse<HomeResponse>
}

@Singleton
internal class RetrofitRophimNetwork @Inject constructor(
    private val retrofitRoNetworkApi: RetrofitRoNetworkApi
) : RoNetworkDataSource {
    override suspend fun getHomeData(onError: (String?) -> Unit): HomeResponse {
        var homeData: HomeResponse? = null
        val response = retrofitRoNetworkApi.getMovies()
        response.suspendOnSuccess {
            homeData = data
        }.onFailure {
            onError(message())
        }
        return homeData!!
    }
}