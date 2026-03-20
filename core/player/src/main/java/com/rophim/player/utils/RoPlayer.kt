package com.rophim.player.utils

import android.content.Context
import android.os.Looper
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView
import androidx.annotation.OptIn
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.DeviceInfo
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.text.CueGroup
import androidx.media3.common.util.Size
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.rophim.player.model.track.CueWithTiming
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/*Đây là lớp quản lý player ,mọi logic khởi tạo,cache... sẽ được quản lý ở đây*/
@Stable
@Singleton
@OptIn(UnstableApi::class)
class RoPlayer
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val playerCache: SimpleCache,
    private val httpFactory: OkHttpDataSource.Factory
) : CuesProvider, Player {
    private var _player: ExoPlayer? = null
    val player: ExoPlayer get() = _player ?: createPlayer().also { _player = it }
    override var offset by mutableLongStateOf(0L)
        private set

    private fun createPlayer(): ExoPlayer {
        return ExoPlayer.Builder(context)
            .setMediaSourceFactory(buildMediaSourceFactory())
            .setLoadControl(buildLoadControl())
            .setRenderersFactory(buildRenderersFactory())
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                    .build(),
                /* handleAudioFocus= */ true
            )
            .setHandleAudioBecomingNoisy(true)
            .setSeekForwardIncrementMs(10_000)
            .setSeekBackIncrementMs(10_000)
            .build()
    }

    fun prepareMedia(uri: String, startPosition: Long = 0L) {
        player.apply {
            setMediaItem(MediaItem.fromUri(uri), startPosition)
            prepare()
        }
    }

    private fun buildMediaSourceFactory(): DefaultMediaSourceFactory {
        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(playerCache)
            .setUpstreamDataSourceFactory(httpFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
        return DefaultMediaSourceFactory(context).setDataSourceFactory(cacheDataSourceFactory)

    }

    private fun buildLoadControl(): DefaultLoadControl {
        return DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                /* minBufferMs     = */ 15_000,   // Buffer tối thiểu trước khi play
                /* maxBufferMs     = */ 50_000,   // Buffer tối đa giữ trong RAM
                /* bufferForPlaybackMs             = */ 2_500,
                /* bufferForPlaybackAfterRebufferMs = */ 5_000
            )
            .setPrioritizeTimeOverSizeThresholds(true)
            .build()
    }

    private fun buildRenderersFactory(): DefaultRenderersFactory {
        return DefaultRenderersFactory(context).apply {
            setExtensionRendererMode(
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
            )
        }
    }

    override fun play() {
        _player?.play()
    }

    override fun pause() {
        _player?.pause()
    }

    override fun setPlayWhenReady(playWhenReady: Boolean) {
    }

    override fun seekTo(position: Long) {
        _player?.seekTo(position)
    }

    override fun seekBack() {
        _player?.seekBack()
    }

    override fun seekForward() {
        _player?.seekForward()
    }

    override fun prepare() {
        _player?.prepare()
    }

    override fun seekTo(
        mediaItemIndex: Int,
        positionMs: Long,
    ) {
        _player?.seekTo(mediaItemIndex, positionMs)
    }

    override fun getApplicationLooper(): Looper {
        return _player?.applicationLooper ?: Looper.getMainLooper()
    }

    override fun addListener(listener: Player.Listener) {
        _player?.addListener(listener)
    }

    override fun removeListener(listener: Player.Listener) {
        _player?.removeListener(listener)
    }

    override fun setMediaItems(mediaItems: List<MediaItem>) {
        _player?.setMediaItems(mediaItems)
    }

    override fun setMediaItems(
        mediaItems: List<MediaItem>,
        resetPosition: Boolean,
    ) {
        _player?.setMediaItems(mediaItems, resetPosition)
    }

    override fun setMediaItems(
        mediaItems: List<MediaItem>,
        startIndex: Int,
        startPositionMs: Long,
    ) {
        _player?.setMediaItems(mediaItems, startIndex, startPositionMs)
    }

    override fun setMediaItem(mediaItem: MediaItem) {
        _player?.setMediaItem(mediaItem)
    }

    override fun setMediaItem(
        mediaItem: MediaItem,
        startPositionMs: Long,
    ) {
        _player?.setMediaItem(mediaItem, startPositionMs)
    }

    override fun setMediaItem(
        mediaItem: MediaItem,
        resetPosition: Boolean,
    ) {
        _player?.setMediaItem(mediaItem, resetPosition)
    }

    override fun addMediaItem(mediaItem: MediaItem) {
        _player?.addMediaItem(mediaItem)
    }

    override fun addMediaItem(
        index: Int,
        mediaItem: MediaItem,
    ) {
        _player?.addMediaItem(index, mediaItem)
    }

    override fun addMediaItems(mediaItems: List<MediaItem>) {
        _player?.addMediaItems(mediaItems)
    }

    override fun addMediaItems(
        index: Int,
        mediaItems: List<MediaItem>,
    ) {
        _player?.addMediaItems(index, mediaItems)
    }

    override fun moveMediaItem(
        currentIndex: Int,
        newIndex: Int,
    ) {
        _player?.moveMediaItem(currentIndex, newIndex)
    }

    override fun moveMediaItems(
        fromIndex: Int,
        toIndex: Int,
        newIndex: Int,
    ) {
        _player?.moveMediaItems(fromIndex, toIndex, newIndex)
    }

    override fun replaceMediaItem(
        index: Int,
        mediaItem: MediaItem,
    ) {
        _player?.replaceMediaItem(index, mediaItem)
    }

    override fun replaceMediaItems(
        fromIndex: Int,
        toIndex: Int,
        mediaItems: List<MediaItem>,
    ) {
        _player?.replaceMediaItems(fromIndex, toIndex, mediaItems)
    }

    override fun removeMediaItem(index: Int) {
        _player?.removeMediaItem(index)
    }

    override fun removeMediaItems(
        fromIndex: Int,
        toIndex: Int,
    ) {
        _player?.removeMediaItems(fromIndex, toIndex)
    }

    override fun clearMediaItems() {
        _player?.clearMediaItems()
    }

    override fun isCommandAvailable(command: Int): Boolean {
        return _player?.isCommandAvailable(command) ?: false
    }

    override fun canAdvertiseSession(): Boolean {
        return _player?.canAdvertiseSession() ?: false
    }

    override fun getAvailableCommands(): Player.Commands {
        return _player?.availableCommands ?: Player.Commands.EMPTY
    }

    override fun getPlaybackState(): Int {
        return _player?.playbackState ?: Player.STATE_IDLE
    }

    override fun getPlaybackSuppressionReason(): Int {
        return _player?.playbackSuppressionReason ?: Player.PLAYBACK_SUPPRESSION_REASON_NONE
    }

    override fun isPlaying(): Boolean {
        return _player?.isPlaying ?: false
    }

    override fun getPlayerError(): PlaybackException? {
        return _player?.playerError
    }

    override fun getPlayWhenReady(): Boolean {
        return _player?.playWhenReady ?: false
    }

    override fun setRepeatMode(repeatMode: Int) {
        _player?.repeatMode = repeatMode
    }

    override fun getRepeatMode(): Int {
        return _player?.repeatMode ?: Player.REPEAT_MODE_OFF
    }

    override fun setShuffleModeEnabled(shuffleModeEnabled: Boolean) {
        _player?.shuffleModeEnabled = shuffleModeEnabled
    }

    override fun getShuffleModeEnabled(): Boolean {
        return _player?.shuffleModeEnabled ?: false
    }

    override fun isLoading(): Boolean {
        return _player?.isLoading ?: false
    }

    override fun seekToDefaultPosition() {
        _player?.seekToDefaultPosition()
    }

    override fun seekToDefaultPosition(mediaItemIndex: Int) {
        _player?.seekToDefaultPosition(mediaItemIndex)
    }

    override fun getSeekBackIncrement(): Long {
        return _player?.seekBackIncrement ?: 0L
    }

    override fun getSeekForwardIncrement(): Long {
        return _player?.seekForwardIncrement ?: 0L
    }

    override fun hasPreviousMediaItem(): Boolean {
        return _player?.hasPreviousMediaItem() ?: false
    }

    override fun seekToPreviousMediaItem() {
        _player?.seekToPreviousMediaItem()
    }

    override fun getMaxSeekToPreviousPosition(): Long {
        return _player?.maxSeekToPreviousPosition ?: 0L
    }

    override fun seekToPrevious() {
        _player?.seekToPrevious()
    }

    override fun hasNextMediaItem(): Boolean {
        return _player?.hasNextMediaItem() ?: false
    }

    override fun seekToNextMediaItem() {
        _player?.seekToNextMediaItem()
    }

    override fun seekToNext() {
        _player?.seekToNext()
    }

    override fun setPlaybackParameters(playbackParameters: PlaybackParameters) {
        _player?.playbackParameters = playbackParameters
    }

    override fun setPlaybackSpeed(speed: Float) {
        _player?.setPlaybackSpeed(speed)
    }

    override fun getPlaybackParameters(): PlaybackParameters {
        return _player?.playbackParameters ?: PlaybackParameters.DEFAULT
    }

    override fun stop() {
        _player?.stop()
    }

    override fun release() {
        //removeListener(listener)
        _player?.release()
        _player = null
    }

    override fun getCurrentTracks(): Tracks {
        return _player?.currentTracks ?: Tracks.EMPTY
    }

    override fun getTrackSelectionParameters(): TrackSelectionParameters {
        return _player?.trackSelectionParameters ?: TrackSelectionParameters.DEFAULT_WITHOUT_CONTEXT
    }

    override fun setTrackSelectionParameters(parameters: TrackSelectionParameters) {
        _player?.trackSelectionParameters = parameters
    }

    override fun getMediaMetadata(): MediaMetadata {
        return _player?.mediaMetadata ?: MediaMetadata.EMPTY
    }

    override fun getPlaylistMetadata(): MediaMetadata {
        return _player?.playlistMetadata ?: MediaMetadata.EMPTY
    }

    override fun setPlaylistMetadata(mediaMetadata: MediaMetadata) {
        _player?.playlistMetadata = mediaMetadata
    }

    override fun getCurrentManifest(): Any? {
        return _player?.currentManifest
    }

    override fun getCurrentTimeline(): Timeline {
        return _player?.currentTimeline ?: Timeline.EMPTY
    }

    override fun getCurrentPeriodIndex(): Int {
        return _player?.currentPeriodIndex ?: 0
    }

    @Deprecated("Deprecated in Java")
    override fun getCurrentWindowIndex(): Int {
        return _player?.currentWindowIndex ?: 0
    }

    override fun getCurrentMediaItemIndex(): Int {
        return _player?.currentMediaItemIndex ?: 0
    }

    @Deprecated("Deprecated in Java")
    override fun getNextWindowIndex(): Int {
        return _player?.nextWindowIndex ?: C.INDEX_UNSET
    }

    override fun getNextMediaItemIndex(): Int {
        return _player?.nextMediaItemIndex ?: C.INDEX_UNSET
    }

    @Deprecated("Deprecated in Java")
    override fun getPreviousWindowIndex(): Int {
        return _player?.previousWindowIndex ?: C.INDEX_UNSET
    }

    override fun getPreviousMediaItemIndex(): Int {
        return _player?.previousMediaItemIndex ?: C.INDEX_UNSET
    }

    override fun getCurrentMediaItem(): MediaItem? {
        return _player?.currentMediaItem
    }

    override fun getMediaItemCount(): Int {
        return _player?.mediaItemCount ?: 0
    }

    override fun getMediaItemAt(index: Int): MediaItem {
        return _player?.getMediaItemAt(index) ?: MediaItem.EMPTY
    }

    override fun getDuration(): Long {
        return _player?.duration ?: C.TIME_UNSET
    }

    override fun getCurrentPosition(): Long {
        return _player?.currentPosition ?: 0L
    }

    override fun getBufferedPosition(): Long {
        return _player?.bufferedPosition ?: 0L
    }

    override fun getBufferedPercentage(): Int {
        return _player?.bufferedPercentage ?: 0
    }

    override fun getTotalBufferedDuration(): Long {
        return _player?.totalBufferedDuration ?: 0L
    }

    @Deprecated("Deprecated in Java")
    override fun isCurrentWindowDynamic(): Boolean {
        return _player?.isCurrentWindowDynamic ?: false
    }

    override fun isCurrentMediaItemDynamic(): Boolean {
        return _player?.isCurrentMediaItemDynamic ?: false
    }

    @Deprecated("Deprecated in Java")
    override fun isCurrentWindowLive(): Boolean {
        return _player?.isCurrentWindowLive ?: false
    }

    override fun isCurrentMediaItemLive(): Boolean {
        return _player?.isCurrentMediaItemLive ?: false
    }

    override fun getCurrentLiveOffset(): Long {
        return _player?.currentLiveOffset ?: C.TIME_UNSET
    }

    @Deprecated("Deprecated in Java")
    override fun isCurrentWindowSeekable(): Boolean {
        return _player?.isCurrentWindowSeekable ?: false
    }

    override fun isCurrentMediaItemSeekable(): Boolean {
        return _player?.isCurrentMediaItemSeekable ?: false
    }

    override fun isPlayingAd(): Boolean {
        return _player?.isPlayingAd ?: false
    }

    override fun getCurrentAdGroupIndex(): Int {
        return _player?.currentAdGroupIndex ?: C.INDEX_UNSET
    }

    override fun getCurrentAdIndexInAdGroup(): Int {
        return _player?.currentAdIndexInAdGroup ?: C.INDEX_UNSET
    }

    override fun getContentDuration(): Long {
        return _player?.contentDuration ?: C.TIME_UNSET
    }

    override fun getContentPosition(): Long {
        return _player?.contentPosition ?: 0L
    }

    override fun getContentBufferedPosition(): Long {
        return _player?.contentBufferedPosition ?: 0L
    }

    override fun getAudioAttributes(): AudioAttributes {
        return _player?.audioAttributes ?: AudioAttributes.DEFAULT
    }

    override fun setVolume(volume: Float) {
        _player?.volume = volume
    }

    override fun getVolume(): Float {
        return _player?.volume ?: 1f
    }

    override fun clearVideoSurface() {
        _player?.clearVideoSurface()
    }

    override fun clearVideoSurface(surface: Surface?) {
        _player?.clearVideoSurface(surface)
    }

    override fun setVideoSurface(surface: Surface?) {
        _player?.setVideoSurface(surface)
    }

    override fun setVideoSurfaceHolder(surfaceHolder: SurfaceHolder?) {
        _player?.setVideoSurfaceHolder(surfaceHolder)
    }

    override fun clearVideoSurfaceHolder(surfaceHolder: SurfaceHolder?) {
        _player?.clearVideoSurfaceHolder(surfaceHolder)
    }

    override fun setVideoSurfaceView(surfaceView: SurfaceView?) {
        _player?.setVideoSurfaceView(surfaceView)
    }

    override fun clearVideoSurfaceView(surfaceView: SurfaceView?) {
        _player?.clearVideoSurfaceView(surfaceView)
    }

    override fun setVideoTextureView(textureView: TextureView?) {
        _player?.setVideoTextureView(textureView)
    }

    override fun clearVideoTextureView(textureView: TextureView?) {
        _player?.clearVideoTextureView(textureView)
    }

    override fun getVideoSize(): VideoSize {
        return _player?.videoSize ?: VideoSize.UNKNOWN
    }

    override fun getSurfaceSize(): Size {
        return _player?.surfaceSize ?: Size.UNKNOWN
    }

    override fun getCurrentCues(): CueGroup {
        return _player?.currentCues ?: CueGroup.EMPTY_TIME_ZERO
    }

    override fun getDeviceInfo(): DeviceInfo {
        return _player?.deviceInfo ?: DeviceInfo.UNKNOWN
    }

    override fun getDeviceVolume(): Int {
        return _player?.deviceVolume ?: 0
    }

    override fun isDeviceMuted(): Boolean {
        return _player?.isDeviceMuted ?: false
    }

    @Deprecated("Deprecated in Java")
    override fun setDeviceVolume(volume: Int) {
        _player?.deviceVolume = volume
    }

    override fun setDeviceVolume(
        volume: Int,
        flags: Int,
    ) {
        _player?.setDeviceVolume(volume, flags)
    }

    @Deprecated("Deprecated in Java")
    override fun increaseDeviceVolume() {
        _player?.increaseDeviceVolume()
    }

    override fun increaseDeviceVolume(flags: Int) {
        _player?.increaseDeviceVolume(flags)
    }

    @Deprecated("Deprecated in Java")
    override fun decreaseDeviceVolume() {
        _player?.decreaseDeviceVolume()
    }

    override fun decreaseDeviceVolume(flags: Int) {
        _player?.decreaseDeviceVolume(flags)
    }

    @Deprecated("Deprecated in Java")
    override fun setDeviceMuted(muted: Boolean) {
        _player?.isDeviceMuted = muted
    }

    override fun setDeviceMuted(
        muted: Boolean,
        flags: Int,
    ) {
        _player?.setDeviceMuted(muted, flags)
    }

    override fun setAudioAttributes(
        audioAttributes: AudioAttributes,
        handleAudioFocus: Boolean,
    ) {
        _player?.setAudioAttributes(audioAttributes, handleAudioFocus)
    }

    override fun mute() {
        _player?.mute()
    }

    override fun unmute() {
        _player?.unmute()
    }


    override fun addCue(cue: CueWithTiming) {

    }

    override fun clearCues() {

    }
}

data class PlayerConfig(
    val autoPlay: Boolean = true,//TỰ ĐỘNG PHÁT
    val useController: Boolean = true,//SỬ DỤNG ĐIỀU KHIỂN
    val repeatMode: Int = Player.REPEAT_MODE_OFF,
    val enableGestures: Boolean = false,  //SỬ DỤNG GESTURE ĐỂ TĂNG ĐỘ SÁNG HOẶC ÂM THANH
    val showSubtitles: Boolean = false,//HIỂN THIJ PHỤ ĐỀ
    val keepScreenOn: Boolean = true//MÀN HÌNH SÁNG LIÊN TỤC
)

object PlayerConfigs {
    val Trailer = PlayerConfig(
        autoPlay = true,
        useController = false,
        keepScreenOn = false
    )

    val FullMovie = PlayerConfig(
        autoPlay = true,
        useController = true,
        enableGestures = true,
        showSubtitles = true,
        keepScreenOn = true
    )
}