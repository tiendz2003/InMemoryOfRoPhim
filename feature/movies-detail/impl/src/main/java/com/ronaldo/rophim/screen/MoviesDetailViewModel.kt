package com.ronaldo.rophim.screen

import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.manutd.rophim.ExoPlayerManager
import dagger.Binds
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesDetailViewModel @AssistedInject constructor(
    @Assisted state: DetailState,
    private val exoPlayerManager: ExoPlayerManager
) : MavericksViewModel<DetailState>(state) {
    val player: ExoPlayer get() = exoPlayerManager.player

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            val newUiState = when (playbackState) {
                Player.STATE_BUFFERING -> TrailerUiState.Buffering
                Player.STATE_READY -> TrailerUiState.Playing
                Player.STATE_ENDED -> TrailerUiState.Ended
                else -> TrailerUiState.Idle
            }
            setState { copy(uiState = newUiState) }
        }

        override fun onPlayerError(error: PlaybackException) {
            val message = when (error.errorCode) {
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT ->
                    "Không có kết nối mạng"

                PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND ->
                    "Video không tồn tại"

                else -> "Không thể phát video"
            }
            setState { copy(uiState = TrailerUiState.Error(message)) }
        }
    }

    init {
        player.addListener(playerListener)
    }

    fun loadTrailer(url: String) {
        withState { s ->
            if (url == s.currentUrl && s.uiState !is TrailerUiState.Error) {
                return@withState
            }
            setState {
                copy(
                    currentUrl = url,
                    uiState = TrailerUiState.Buffering
                )
            }
            viewModelScope.launch(Dispatchers.Main) {
                exoPlayerManager.prepareMedia(url)
                player.play()
            }
        }
    }

    fun retry() = withState { state ->
        if (state.currentUrl.isNotEmpty()) loadTrailer(state.currentUrl)
    }

    fun onVisibilityChanged(isVisible: Boolean) {
        withState { state ->
            viewModelScope.launch(Dispatchers.Main) {
                when {
                    !isVisible && player.isPlaying -> player.pause()

                    isVisible &&
                            !player.isPlaying &&
                            (state.uiState is TrailerUiState.Playing || state.uiState is TrailerUiState.Buffering) -> player.play()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.removeListener(playerListener)
        player.pause()
        player.clearMediaItems()
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<MoviesDetailViewModel, DetailState> {
        override fun create(state: DetailState): MoviesDetailViewModel
    }

    companion object : MavericksViewModelFactory<MoviesDetailViewModel, DetailState>
    by hiltMavericksViewModelFactory()
}

data class DetailState(
    val uiState: TrailerUiState = TrailerUiState.Idle,
    val currentUrl: String = ""
) : MavericksState

sealed class TrailerUiState {
    object Idle : TrailerUiState()
    object Buffering : TrailerUiState()
    object Playing : TrailerUiState()
    object Ended : TrailerUiState()
    data class Error(val message: String) : TrailerUiState()
}

@Module
@InstallIn(MavericksViewModelComponent::class) // 1. Cài đặt vào Component riêng của Mavericks
interface DetailViewModelModule {

    @Binds
    @IntoMap // 2. Đưa vào Map (để sửa cái lỗi MissingBinding Map kia)
    @ViewModelKey(MoviesDetailViewModel::class) // 3. Định danh Key là class ViewModel của bạn
    fun bindHomeViewModelFactory(factory: MoviesDetailViewModel.Factory): AssistedViewModelFactory<*, *>
}