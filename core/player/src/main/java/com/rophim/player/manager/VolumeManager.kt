package com.rophim.player.manager

import android.content.Context
import android.media.AudioManager
import android.media.audiofx.LoudnessEnhancer
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.rophim.player.utils.RoPlayer

@Stable
class VolumeManager(
    context: Context
) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val currentStreamVolume
        get() = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    private val maxStreamVolume
        get() = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val maxVolume: Float
        get() = maxStreamVolume
            .times(if (loudnessEnhancer != null) 2 else 1)
            .toFloat()

    private val currentLoudnessGain: Float
        get() = (currentVolume - maxStreamVolume) * (MAX_LOUDNESS_BOOST / maxStreamVolume)
    var currentVolume by mutableFloatStateOf(currentStreamVolume.toFloat())
        private set
    val currentVolumePercentage by derivedStateOf {
        (currentVolume / maxStreamVolume.toFloat())
            .coerceIn(0F, 1F)
    }

    //loudness enhance chỉ hoạt động khi volume > max volume
    private var loudnessEnhancer: LoudnessEnhancer? = null
        set(value) {
            if (currentVolume > maxStreamVolume) {
                runCatching {
                    value?.enabled = true
                    value?.setTargetGain(currentLoudnessGain.toInt())
                }
            }
        }

    fun setVolume(volume: Float) {
        currentVolume = volume.coerceIn(0f, maxVolume)
        if (currentVolume <= maxStreamVolume) {
            loudnessEnhancer?.enabled = false
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                currentVolume.toInt(),
                0// Không show system volume UI
            )
        } else {
            // Vượt max: dùng LoudnessEnhancer boost thêm
            runCatching {
                loudnessEnhancer?.enabled = true
                loudnessEnhancer?.setTargetGain(currentLoudnessGain.toInt())
            }
        }
    }

    fun increaseVolume(step: Float = 1f) = setVolume(currentVolume + step)
    fun decreaseVolume(step: Float = 1f) = setVolume(currentVolume - step)

    companion object {
        private const val MAX_LOUDNESS_BOOST = 2000f

        @OptIn(UnstableApi::class)
        @Composable
        fun rememberVolumeManager(player: RoPlayer): VolumeManager {
            val context = LocalContext.current
            val manager = remember { VolumeManager(context) }
            LaunchedEffect(player) {
                player.addListener(object : Player.Listener {
                    override fun onAudioSessionIdChanged(audioSessionId: Int) {
                        manager.loudnessEnhancer?.release()
                        runCatching {
                            manager.loudnessEnhancer = LoudnessEnhancer(audioSessionId)
                        }
                    }
                })
            }
            DisposableEffect(LocalLifecycleOwner.current) {
                onDispose { manager.loudnessEnhancer?.release() }
            }
            return manager
        }
    }
}