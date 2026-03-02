package com.ronaldo.rophim.di

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.ronaldo.rophim.screen.MoviesDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@Module
@InstallIn(MavericksViewModelComponent::class) // 1. Cài đặt vào Component riêng của Mavericks
internal interface DetailViewModelModule {

    @Binds
    @IntoMap // 2. Đưa vào Map (để sửa cái lỗi MissingBinding Map kia)
    @ViewModelKey(MoviesDetailViewModel::class) // 3. Định danh Key là class ViewModel của bạn
    fun bindHomeViewModelFactory(factory: MoviesDetailViewModel.Factory): AssistedViewModelFactory<*, *>
}