package com.manutd.ronaldo.impl

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.manutd.ronaldo.domain.GetHomeResourceUseCase
import com.manutd.ronaldo.network.model.ChannelType
import com.manutd.ronaldo.network.model.Group
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class HomeViewModel @AssistedInject constructor(
    @Assisted initialState: MoviesState,
    private val getHomeResourceUseCase: GetHomeResourceUseCase
) : MavericksViewModel<MoviesState>(initialState) {

    //gọi trong laucheEffect
    fun fetchMovies(forceRefresh: Boolean = true) {
        withState { state ->
            val shouldFetch = state.groups is Uninitialized || forceRefresh
            if (shouldFetch) {
                if (forceRefresh) {
                    setState { copy(isRefreshing = true) }
                }
                suspend {
                    getHomeResourceUseCase.invoke(
                        onError = { message ->
                            // Log error nhưng vẫn throw để Async catch
                            android.util.Log.e("HomeViewModel", "Error: $message")
                            throw Exception(message)
                        }
                    ).groups
                }.execute { asyncGroups ->
                    // Copy state với Async result mới
                    copy(
                        groups = asyncGroups,
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
    val groups: Async<List<Group>> = Uninitialized,
    val selectedChannelId: String? = null,
    val isRefreshing: Boolean = false
) : MavericksState {

    // Derived properties
    val sliderGroups: List<Group>
        get() = groups()?.filter { it.type == ChannelType.SLIDER } ?: emptyList()

    val horizontalGroups: List<Group>
        get() = groups()?.filter { it.type == ChannelType.HORIZONTAL } ?: emptyList()

    val topGroups: List<Group>
        get() = groups()?.filter { it.type == ChannelType.TOP } ?: emptyList()
}