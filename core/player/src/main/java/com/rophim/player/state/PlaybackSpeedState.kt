package com.rophim.player.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.media3.common.Player
import androidx.media3.common.listen
import com.rophim.player.utils.RoPlayer

@Stable
class PlaybackSpeedState(
    val player: RoPlayer
) {
    var isEnabled by mutableStateOf(arePlaybackParametersEnabled(player))
        private set

    var playbackSpeed by mutableFloatStateOf(player.playbackParameters.speed)
        private set

    fun updatePlaybackSpeed(speed: Float) {
        player.playbackParameters = player.playbackParameters.withSpeed(speed)
    }

    internal suspend fun observe(): Nothing {
        playbackSpeed = player.playbackParameters.speed
        isEnabled = arePlaybackParametersEnabled(player)
        player.listen { events ->
            if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED) && playbackState == Player.STATE_READY) {
                updatePlaybackSpeed(playbackSpeed)
            }

            if (
                events.containsAny(
                    Player.EVENT_PLAYBACK_PARAMETERS_CHANGED,
                    Player.EVENT_AVAILABLE_COMMANDS_CHANGED,
                )
            ) {
                playbackSpeed = playbackParameters.speed
                isEnabled = arePlaybackParametersEnabled(this)
            }
        }
    }

    private fun arePlaybackParametersEnabled(player: Player) =
        player.isCommandAvailable(Player.COMMAND_SET_SPEED_AND_PITCH)

    companion object {
        /**
         * Remember the value of [PlaybackSpeedState] created based on the passed [Player] and launch a
         * coroutine to listen to [Player's][Player] changes. If the [Player] instance changes between
         * compositions, produce and remember a new value.
         */
        @Composable
        fun rememberPlaybackSpeedState(player: RoPlayer): PlaybackSpeedState {
            val playbackSpeedState = remember(player) { PlaybackSpeedState(player) }
            LaunchedEffect(player) { playbackSpeedState.observe() }
            return playbackSpeedState
        }
    }
}