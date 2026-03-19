package com.manutd.ronaldo.impl.screen

import android.media.AudioTrack
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.exoplayer.ExoPlayer
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.manutd.ronaldo.network.model.Episode
import com.manutd.rophim.ExoPlayerFactory
import com.manutd.rophim.core.data.utils.FakeDataProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WatchScreenViewModel @AssistedInject constructor(
    @Assisted state: WatchState,
    private val exoPlayerFactory: ExoPlayerFactory
) : MavericksViewModel<WatchState>(state) {
    val player: ExoPlayer get() = exoPlayerFactory.player
    private var progressJob: Job? = null
    private var hideControlsJob: Job? = null
    private var seekFeedbackJob: Job? = null

    private val playerListener = object : Player.Listener {

        override fun onPlaybackStateChanged(state: Int) {
            val playerState = when (state) {
                Player.STATE_BUFFERING -> WatchPlayerState.Buffering
                Player.STATE_READY -> WatchPlayerState.Ready
                Player.STATE_ENDED -> WatchPlayerState.Ended
                else -> WatchPlayerState.Idle
            }
            setState { copy(playerState = playerState) }

            // Chỉ polling progress khi READY
            if (state == Player.STATE_READY) startProgressPolling()
            else stopProgressPolling()
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            setState { copy(isPlaying = isPlaying) }
            if (isPlaying) startProgressPolling() else stopProgressPolling()
        }

        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            // Duration có thể chỉ có sau khi timeline ready
            setState { copy(durationMs = player.duration.coerceAtLeast(0L)) }
        }

        override fun onPlayerError(error: PlaybackException) {
            stopProgressPolling()
            val message = when (error.errorCode) {
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT ->
                    "Mất kết nối mạng. Vui lòng thử lại."

                PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND ->
                    "Không tìm thấy video."

                PlaybackException.ERROR_CODE_DECODER_INIT_FAILED ->
                    "Thiết bị không hỗ trợ định dạng này."

                else -> "Lỗi phát video. Thử lại sau."
            }
            setState { copy(playerState = WatchPlayerState.Error(message)) }
        }
    }

    init {
        player.addListener(playerListener)
        withState { loadMedia(it) }
    }

    private fun loadMedia(state: WatchState) {
        // Lấy URL từ episodeId hoặc fallback fake data
        val videoUrl = state.episodeId?.let { id ->
            FakeDataProvider.seriesOnAir.seasons
                .flatMap { it.episodes }
                .firstOrNull { it.id == id }
                ?.videoUrl
        } ?: FakeDataProvider.seriesOnAir.seasons.firstOrNull()?.episodes?.firstOrNull()?.videoUrl
        ?: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

        exoPlayerFactory.prepareMedia(videoUrl)
        player.playWhenReady = true
    }

    // ── Progress polling ──────────────────────────────────────────
    // Poll mỗi 500ms thay vì mỗi frame — đủ mượt cho Seekbar, không tốn CPU
    private fun startProgressPolling() {
        if (progressJob?.isActive == true) return
        progressJob = viewModelScope.launch {
            while (coroutineContext.isActive) {
                withState { s ->
                    // Không update nếu user đang drag — tránh state conflict
                    if (!s.isDraggingSlider) {
                        setState {
                            copy(
                                currentPositionMs = player.currentPosition.coerceAtLeast(0L),
                                bufferedPositionMs = player.bufferedPosition.coerceAtLeast(0L),
                                durationMs = player.duration.coerceAtLeast(0L)
                            )
                        }
                    }
                }
                delay(500L)
            }
        }
    }

    fun onScreenTap() {
        withState { s ->
            if (s.showControls) {
                // Đang hiện → ẩn ngay
                setState { copy(showControls = false) }
                hideControlsJob?.cancel()
            } else {
                // Đang ẩn → hiện và schedule auto-hide
                setState { copy(showControls = true) }
                scheduleHideControls()
            }
        }
    }

    fun onControlsInteraction() {
        withState { if (it.showControls) scheduleHideControls() }
    }

    private fun scheduleHideControls() {
        hideControlsJob?.cancel()
        hideControlsJob = viewModelScope.launch {
            delay(3_000L)
            setState { copy(showControls = false) }
        }
    }

    fun togglePlayPause() {
        if (player.isPlaying) player.pause() else player.play()
        onControlsInteraction()
    }

    fun seekTo(positionMs: Long) {
        player.seekTo(positionMs.coerceIn(0L, player.duration.coerceAtLeast(0L)))
        onControlsInteraction()
    }

    fun seekForward() {
        seekTo(player.currentPosition + 10_000L)
        showSeekFeedback(isForward = true)
        onControlsInteraction()
    }

    fun seekBackward() {
        seekTo(player.currentPosition - 10_000L)
        showSeekFeedback(isForward = false)
        onControlsInteraction()
    }

    private fun showSeekFeedback(isForward: Boolean) {
        setState { copy(seekFeedback = SeekFeedback(isForward)) }
        seekFeedbackJob?.cancel()
        seekFeedbackJob = viewModelScope.launch {
            delay(800L)
            setState { copy(seekFeedback = null) }
        }
    }

    // Tách onDragStart / onDrag / onDragEnd để kiểm soát chính xác
    fun onSliderDragStart(positionMs: Long) {
        setState { copy(isDraggingSlider = true, dragPositionMs = positionMs) }
        stopProgressPolling()   // Dừng update từ player
        onControlsInteraction()
    }

    fun onSliderDrag(positionMs: Long) {
        // Chỉ update dragPositionMs — Slider đọc state.dragPositionMs qua lambda
        // → chỉ Slider recompose, không phải toàn màn hình
        setState { copy(dragPositionMs = positionMs) }
    }

    fun onSliderDragEnd(positionMs: Long) {
        seekTo(positionMs)
        setState { copy(isDraggingSlider = false) }
        startProgressPolling()  // Resume polling
    }
    // ViewModel chỉ lưu level hiển thị UI
    // Logic thực tế (AudioManager, WindowAttributes) xử lý ở GestureOverlay
    // vì cần Context và Window — không nên đưa vào ViewModel

    fun onVolumeChanged(level: Float) {
        setState { copy(volumeLevel = level, showVolumeFeedback = true) }
        viewModelScope.launch {
            delay(1_500L)
            setState { copy(showVolumeFeedback = false) }
        }
    }

    fun onBrightnessChanged(level: Float) {
        setState { copy(brightnessLevel = level, showBrightnessFeedback = true) }
        viewModelScope.launch {
            delay(1_500L)
            setState { copy(showBrightnessFeedback = false) }
        }
    }

    fun toggleTrackSheet() = setState { copy(showTrackSheet = !showTrackSheet) }

    fun onLifecycleStop() {
        player.pause()
        stopProgressPolling()
    }

    fun onLifecycleStart() {
        withState { s ->
            if (s.playerState is WatchPlayerState.Ready) {
                player.play()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopProgressPolling()
        hideControlsJob?.cancel()
        seekFeedbackJob?.cancel()
        player.removeListener(playerListener)
        player.release()
    }

    private fun stopProgressPolling() {
        progressJob?.cancel()
        progressJob = null
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<WatchScreenViewModel, WatchState> {
        override fun create(state: WatchState): WatchScreenViewModel
    }

    companion object : MavericksViewModelFactory<WatchScreenViewModel, WatchState>
    by hiltMavericksViewModelFactory()
}

sealed class WatchPlayerState {
    object Idle : WatchPlayerState()
    object Buffering : WatchPlayerState()
    object Ready : WatchPlayerState()
    object Ended : WatchPlayerState()
    data class Error(val message: String) : WatchPlayerState()
}

data class TrackSelectionState(
    val availableAudioTracks: List<AudioTrack> = emptyList(),
    val selectedAudioTrackId: String? = null,
    val availableSubtitleTracks: List<AudioTrack> = emptyList(),
    val selectedSubtitleTrackId: String? = null
)

data class WatchState(
    val movieId: String = "",
    val episodeId: String? = null,
    val playerState: WatchPlayerState = WatchPlayerState.Idle,
    val isPlaying: Boolean = false,
    // Slider đọc qua lambda để tránh recompose toàn màn hình
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val bufferedPositionMs: Long = 0L,
    // Khi user đang drag: dừng update từ player, tránh conflict
    val isDraggingSlider: Boolean = false,
    val dragPositionMs: Long = 0L,
    val showControls: Boolean = true,
    // Hiện icon volume/brightness khi đang drag
    val volumeLevel: Float = 0f,         // 0f - 1f
    val brightnessLevel: Float = -1f,    // -1f = dùng system, 0f-1f = override
    val showVolumeFeedback: Boolean = false,
    val showBrightnessFeedback: Boolean = false,
    val seekFeedback: SeekFeedback? = null,
    // ── Track selection
    val showTrackSheet: Boolean = false,
    val trackSelectionState: TrackSelectionState = TrackSelectionState(),

    // ── Episode data (nếu là phim bộ)
    val episodeAsync: Async<Episode> = Uninitialized,

    ) : MavericksState {

    constructor(args: WatchArgs) : this(
        movieId = args.movieId,
        episodeId = args.episodeId
    )


    /** Position hiển thị: dùng dragPosition khi đang drag, player position khi không */
    val displayPositionMs: Long
        get() = if (isDraggingSlider) dragPositionMs else currentPositionMs

    /** Progress 0f-1f cho Slider */
    val progress: Float
        get() = if (durationMs > 0L) displayPositionMs.toFloat() / durationMs else 0f

    val isBuffering: Boolean get() = playerState is WatchPlayerState.Buffering
    val hasError: Boolean get() = playerState is WatchPlayerState.Error
    val errorMessage: String? get() = (playerState as? WatchPlayerState.Error)?.message
}

data class SeekFeedback(
    val isForward: Boolean,
    val seconds: Int = 10
)