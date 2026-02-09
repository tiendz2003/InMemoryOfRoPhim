package com.manutd.ronaldo.impl

import android.util.Log
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.manutd.ronaldo.domain.GetHomeResourceUseCase
import com.manutd.ronaldo.impl.utils.HomeSection
import com.manutd.ronaldo.impl.utils.toHomeSections
import dagger.Binds
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

class HomeViewModel @AssistedInject constructor(
    @Assisted initialState: MoviesState,
    private val getHomeResourceUseCase: GetHomeResourceUseCase
) : MavericksViewModel<MoviesState>(initialState) {

    //gọi trong launchEffect
    fun fetchMovies(forceRefresh: Boolean = true) {
        withState { state ->
            val shouldFetch = state.sections is Uninitialized || forceRefresh
            if (shouldFetch) {
                if (forceRefresh) {
                    setState { copy(isRefreshing = true) }
                }
                suspend {
                    try {
                        getHomeResourceUseCase.invoke(
                            onError = { throw Exception(it) }
                        ).groups.toHomeSections()
                    } catch (e: Exception) {
                        throw e
                    }
                }.execute { asyncSections ->
                    // Copy state với Async result mới
                    copy(
                        sections = asyncSections,
                        isRefreshing = false
                    )
                }
            }
        }

    }

    /**
     * Tải lại
     */
    fun refresh() {
        setState { copy(isRefreshing = true) }
        fetchMovies()
    }

    /**
     * Select a channel
     */
    fun selectChannel(channelId: String) {
        setState {
            copy(selectedChannelId = channelId)
        }
    }

    /**
     * Clear selection
     */
    fun clearSelection() {
        setState {
            copy(selectedChannelId = null)
        }
    }

    /**
     * Retry loading after error
     */
    fun retry() {
        fetchMovies()
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<HomeViewModel, MoviesState> {
        override fun create(state: MoviesState): HomeViewModel
    }

    companion object : MavericksViewModelFactory<HomeViewModel, MoviesState>
    by hiltMavericksViewModelFactory()
}

data class MoviesState(
    val sections: Async<List<HomeSection>> = Uninitialized,
    val selectedChannelId: String? = null,
    val isRefreshing: Boolean = false
) : MavericksState {

    val isLoading: Boolean
        get() = sections is Loading

    val hasError: Boolean
        get() = sections is Fail

    val errorMessage: String?
        get() = (sections as? Fail)?.error?.message
    val isUninitialized: Boolean
        get() = sections is Uninitialized


    val homeSections: List<HomeSection>
        get() = sections() ?: emptyList()
}

@Module
@InstallIn(MavericksViewModelComponent::class) // 1. Cài đặt vào Component riêng của Mavericks
interface HomeViewModelModule {

    @Binds
    @IntoMap // 2. Đưa vào Map (để sửa cái lỗi MissingBinding Map kia)
    @ViewModelKey(HomeViewModel::class) // 3. Định danh Key là class ViewModel của bạn
    fun bindHomeViewModelFactory(factory: HomeViewModel.Factory): AssistedViewModelFactory<*, *>
}