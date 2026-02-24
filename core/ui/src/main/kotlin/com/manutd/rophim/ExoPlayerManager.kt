package com.manutd.rophim

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/*Đây là lớp quản lý player ,mọi logic khởi tạo,cache... sẽ được quản lý ở đây*/
@Singleton
@OptIn(UnstableApi::class)
class ExoPlayerManager
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val playerCache: SimpleCache,
    private val httpFactory: OkHttpDataSource.Factory
) {
    private var _player: ExoPlayer? = null
    val player: ExoPlayer get() = _player ?: createPlayer().also { _player = it }

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

    fun release() {
        _player?.release()
        _player = null
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