package com.ronaldo.rophim.screen

import android.util.Log
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.manutd.ronaldo.domain.GetCastUseCase
import com.manutd.ronaldo.domain.GetMovieDetailUseCase
import com.manutd.ronaldo.domain.GetRecommendationsUseCase
import com.manutd.ronaldo.domain.ToggleFavoriteUseCase
import com.manutd.ronaldo.network.model.CastMember
import com.manutd.ronaldo.network.model.MovieDetail
import com.manutd.ronaldo.network.model.MovieRecommendation
import com.manutd.ronaldo.network.model.MovieType
import com.manutd.ronaldo.network.model.RatingSource
import com.manutd.rophim.ExoPlayerManager
import com.ronaldo.rophim.api.MoviesDetailKey
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
    private val exoPlayerManager: ExoPlayerManager,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getCastUseCase: GetCastUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
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
            setState { copy(trailerUiState = newUiState) }
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
            setState { copy(trailerUiState = TrailerUiState.Error(message)) }
        }
    }

    init {
        player.addListener(playerListener)
        withState { loadMovieDetail(it.movieId) }
    }

    private fun loadMovieDetail(movieId: String) {
        suspend {
            getMovieDetailUseCase(movieId)
        }.execute { copy(movieDetailAsync = it) }
        // Mavericks .execute{} tự set Loading → Success/Fail
        // Sau khi load xong → load trailer tự động
    }

    fun onTabSelected(index: Int) {
        setState { copy(selectedTabIndex = index) }
        withState { state ->
            when (state.availableTabs.getOrNull(index)) {
                is DetailTab.Cast -> loadCastIfNeeded(state)
                is DetailTab.Recommendations -> loadRecommendationsIfNeeded(state)
                else -> Unit
            }
        }
    }

    private fun loadCastIfNeeded(state: DetailState) {
        // Uninitialized = chưa load lần nào → load
        // Loading/Success/Fail → không load lại
        if (state.castAsync !is com.airbnb.mvrx.Uninitialized) return
        suspend {
            getCastUseCase(state.movieId)
        }.execute { copy(castAsync = it) }
    }

    private fun loadRecommendationsIfNeeded(state: DetailState) {
        if (state.recommendAsync !is com.airbnb.mvrx.Uninitialized) return
        suspend {
            getRecommendationsUseCase(state.movieId)
        }.execute { copy(recommendAsync = it) }
    }

    fun loadTrailer(url: String) {
        withState { s ->
            if (url == s.currentTrailerUrl && s.trailerUiState !is TrailerUiState.Error) return@withState
            setState { copy(currentTrailerUrl = url, trailerUiState = TrailerUiState.Buffering) }
            viewModelScope.launch(Dispatchers.Main) {
                exoPlayerManager.prepareMedia(url)
                player.play()
            }
        }
    }

    fun retryTrailer() =
        withState { if (it.currentTrailerUrl.isNotEmpty()) loadTrailer(it.currentTrailerUrl) }


    fun onTrailerVisibilityChanged(isVisible: Boolean) {
        withState { state ->
            viewModelScope.launch(Dispatchers.Main) {
                when {
                    !isVisible && player.isPlaying -> player.pause()
                    isVisible && !player.isPlaying &&
                            (state.trailerUiState is TrailerUiState.Playing ||
                                    state.trailerUiState is TrailerUiState.Buffering) -> player.play()
                }
            }
        }
    }

    fun toggleFavorite() {
        withState { state ->
            val detail = state.movieDetail ?: return@withState
            if (state.isFavoriteLoading) return@withState
            setState { copy(isFavoriteLoading = true) }
            viewModelScope.launch {
                runCatching {
                    toggleFavoriteUseCase(detail.id, detail.isFavorited)
                }.onSuccess { newValue ->
                    setState {
                        copy(
                            isFavoriteLoading = false,
                            movieDetailAsync = Success(
                                detail.copy(isFavorited = newValue)
                            )
                        )
                    }
                }.onFailure {
                    setState { copy(isFavoriteLoading = false) }
                }
            }
        }
    }

    fun onSeasonSelected(index: Int) = setState {
        copy(
            selectedSeasonIndex = index,
            selectedAudioTrackId = null  // Reset về default của season mới
        )
    }

    fun onAudioTrackSelected(trackId: String) = setState {
        copy(selectedAudioTrackId = trackId)
    }

    fun toggleSynopsisSheet() = setState {
        copy(showSynopsisSheet = !showSynopsisSheet)
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
    val movieId: String = "",

    // ── Async (dùng Mavericks Async thay TabContentState) ─────────
    val movieDetailAsync: Async<MovieDetail> = Uninitialized,
    val castAsync: Async<List<CastMember>> = Uninitialized,
    val recommendAsync: Async<List<MovieRecommendation>> = Uninitialized,

    // ── Trailer player ────────────────────────────────────────────
    val trailerUiState: TrailerUiState = TrailerUiState.Idle,
    val currentTrailerUrl: String = "",

    // ── Tab selection ─────────────────────────────────────────────
    val selectedTabIndex: Int = 0,

    // ── Season / Audio selection ──────────────────────────────────
    val selectedSeasonIndex: Int = 0,
    val selectedAudioTrackId: String? = null,

    // ── UI interaction ────────────────────────────────────────────
    val showSynopsisSheet: Boolean = false,
    val isFavoriteLoading: Boolean = false
) : MavericksState {
    constructor(args: MoviesDetailKey) : this(
        movieId = args.movieId
    )

    val movieDetail: MovieDetail? get() = movieDetailAsync()
    val availableTabs: List<DetailTab>
        get() = buildList {
            if (movieDetail?.type == MovieType.SERIES) add(DetailTab.Episodes)
            add(DetailTab.Cast)
            add(DetailTab.Recommendations)
        }
    val showEpisodesButton: Boolean
        get() = movieDetail?.type == MovieType.SERIES
    val currentSeason get() = movieDetail?.seasons?.getOrNull(selectedSeasonIndex)
    val currentAudioTrack
        get() = currentSeason?.let { season ->
            val trackId = selectedAudioTrackId ?: season.defaultAudioTrackId
            season.audioTracks.firstOrNull { it.id == trackId }
                ?: season.audioTracks.firstOrNull()
        }
    val metadataBadges: List<MetadataBadge>
        get() {
            val detail = movieDetail ?: return emptyList()
            return buildList {
                // IMDb badge
                detail.ratings.firstOrNull { it.source == RatingSource.IMDB }?.let {
                    add(MetadataBadge.Rating("IMDb", it.score))
                }
                add(MetadataBadge.Text(detail.classification))
                add(MetadataBadge.Text(detail.releaseYear.toString()))
                detail.currentSeason?.let { add(MetadataBadge.Text("Phần $it")) }
                detail.latestEpisode?.let { add(MetadataBadge.Text("Tập $it")) }
            }
        }
}

sealed class TrailerUiState {
    object Idle : TrailerUiState()
    object Buffering : TrailerUiState()
    object Playing : TrailerUiState()
    object Ended : TrailerUiState()
    data class Error(val message: String) : TrailerUiState()
}

sealed class DetailTab {
    abstract val title: String

    object Episodes : DetailTab() {
        override val title = "Tập phim"
    }

    object Cast : DetailTab() {
        override val title = "Diễn viên"
    }

    object Recommendations : DetailTab() {
        override val title = "Đề xuất"
    }
}

sealed class MetadataBadge {
    data class Rating(val label: String, val score: Float) : MetadataBadge()
    data class Text(val value: String) : MetadataBadge()
}
