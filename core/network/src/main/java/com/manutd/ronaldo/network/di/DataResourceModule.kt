package com.manutd.ronaldo.network.di

import com.manutd.ronaldo.network.RoNetworkDataSource
import com.manutd.ronaldo.network.retrofit.RetrofitRophimNetwork
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataResourceModule {
    @Binds
    internal abstract fun bindsHomeDataSource(
        homeResource: RetrofitRophimNetwork,
    ): RoNetworkDataSource
}