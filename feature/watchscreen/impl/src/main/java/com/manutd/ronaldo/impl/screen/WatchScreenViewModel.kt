package com.manutd.ronaldo.impl.screen

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.manutd.ronaldo.network.model.Episode
import com.rophim.player.utils.RoPlayer
import com.manutd.rophim.core.data.utils.FakeDataProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WatchScreenViewModel @AssistedInject constructor(
    @Assisted state: WatchState,
    private val roPlayer: RoPlayer,
    // TODO: inject các UseCase khi có repository thật
    // private val getEpisodesUseCase: GetEpisodesUseCase,
    // private val updateWatchProgressUseCase: UpdateWatchProgressUseCase,
    // private val getServersUseCase: GetServersUseCase,
) : MavericksViewModel<WatchState>(state) {

    val player: RoPlayer get() = roPlayer
    private var syncProgressJob: Job? = null


    init {
        withState { loadMedia(it) }
    }

    private fun resolveVideoUrl(state: WatchState): String {
        return state.episode?.id?.let { episodeId ->
            FakeDataProvider.seriesOnAir.seasons
                .flatMap { it.episodes }
                .firstOrNull { it.id == episodeId }
                ?.videoUrl
        }
            ?: FakeDataProvider.seriesOnAir.seasons
                .firstOrNull()?.episodes?.firstOrNull()?.videoUrl
            ?: FALLBACK_URL
    }

    private fun loadMedia(state: WatchState) {
       viewModelScope.launch {
           val videoUrl = resolveVideoUrl(state)
           roPlayer.prepareMedia(videoUrl)
           roPlayer.playWhenReady = true
       }
    }

    fun onEpisodeSelected(episode: Episode) {
        setState { copy(episode = episode, lastSyncedPositionMs = 0L) }
        // Sync progress tập cũ trước khi switch
        withState { syncWatchProgress(it.lastSyncedPositionMs, force = true) }
        // Load media mới
        val videoUrl = FakeDataProvider.seriesOnAir.seasons
            .flatMap { it.episodes }
            .firstOrNull { it.id == episode.id }
            ?.videoUrl ?: FALLBACK_URL
        roPlayer.prepareMedia(videoUrl)
        roPlayer.playWhenReady = true
    }


    fun syncWatchProgress(positionMs: Long, force: Boolean = false) {
        if (force) {
            syncProgressJob?.cancel()
            doSyncProgress(positionMs)
            return
        }
        // Throttle: chỉ schedule nếu chưa có job đang chờ
        if (syncProgressJob?.isActive == true) return
        syncProgressJob = viewModelScope.launch {
            delay(SYNC_INTERVAL_MS)
            doSyncProgress(positionMs)
        }
    }

    private fun doSyncProgress(positionMs: Long) {
        setState { copy(lastSyncedPositionMs = positionMs) }
        viewModelScope.launch {
            // TODO: gọi updateWatchProgressUseCase(movieId, episodeId, positionMs)
            // runCatching { updateWatchProgressUseCase(...) }
            //     .onFailure { /* log lỗi, không show UI error vì không critical */ }
        }
    }

    fun loadEpisodesIfNeeded() {
        withState { state ->
            // Uninitialized = chưa load lần nào → load
            // Loading/Success/Fail → không load lại
            if (state.episodesAsync !is Uninitialized) return@withState
            suspend {
                // TODO: getEpisodesUseCase(state.movieId)
                // Fake data tạm thời
                FakeDataProvider.seriesOnAir.seasons.flatMap { it.episodes }
            }.execute { copy(episodesAsync = it) }
        }
    }

    fun clearNetworkError() = setState { copy(networkError = null) }

    fun retryMedia() {
        withState { loadMedia(it) }
    }


    fun onLifecycleStop() {
        roPlayer.pause()
        // Sync progress khi app vào background
        syncProgressJob?.cancel()
        withState { syncWatchProgress(it.lastSyncedPositionMs, force = true) }
    }


    override fun onCleared() {
        super.onCleared()
        syncProgressJob?.cancel()
        // Sync lần cuối khi rời màn hình
        withState { syncWatchProgress(it.lastSyncedPositionMs, force = true) }
        // Không release RoPlayer vì là @Singleton
        // Chỉ clear media để giải phóng bộ nhớ
        roPlayer.clearMediaItems()
        roPlayer.stop()
    }


    @AssistedFactory
    interface Factory : AssistedViewModelFactory<WatchScreenViewModel, WatchState> {
        override fun create(state: WatchState): WatchScreenViewModel
    }

    companion object : MavericksViewModelFactory<WatchScreenViewModel, WatchState>
    by hiltMavericksViewModelFactory() {
        private const val FALLBACK_URL =
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        private const val SYNC_INTERVAL_MS = 10_000L
    }
}


data class WatchState(
    val movieId: String = "",
    val episode: Episode? = null,

    // ── Async data từ network
    val episodesAsync: Async<List<Episode>> = Uninitialized,

    // ── Watch progress — sync lên server theo interval
    // Không phải currentPositionMs (đã về ScrubState)
    val lastSyncedPositionMs: Long = 0L,

    // ── Network error (tách biệt với player error)
    // Player error được handle bởi Player.Listener trong State class
    val networkError: String? = null,

    //  Server/Source selection (placeholder cho sau)
    // val serversAsync: Async<List<Server>> = Uninitialized,
    // val selectedServerId: String? = null,

) : MavericksState {

    constructor(
        movieId: String,
        episodeArgs: Episode?
    ) : this(
        movieId = movieId,
        episode = episodeArgs
    )

}