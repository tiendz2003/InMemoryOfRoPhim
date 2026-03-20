package com.rophim.player.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.media3.common.Player
import androidx.media3.common.listen
import com.rophim.player.utils.RoPlayer
import kotlinx.coroutines.delay

class ControlsVisibilityState(
    val roPlayer: RoPlayer
) {
    private var controlTimeoutVisibility by mutableIntStateOf(PLAYER_CONTROL_VISIBILITY_TIMEOUT)
    var isVisible: Boolean by mutableStateOf(true)
        private set

    fun toggle() {
        if (isVisible) {
            hide()
        } else {
            show(indefinite = shouldShowIndefinitely())
        }
    }

    fun hide() {
        isVisible = false
        controlTimeoutVisibility = 0
    }

    fun show(indefinite: Boolean = false) {
        isVisible = true
        controlTimeoutVisibility =
            if (indefinite) Int.MAX_VALUE else PLAYER_CONTROL_VISIBILITY_TIMEOUT
    }

    private fun shouldShowIndefinitely(): Boolean {
        return !roPlayer.isPlaying ||
                roPlayer.playbackState == Player.STATE_ENDED ||
                roPlayer.playbackState == Player.STATE_BUFFERING
    }

    private suspend fun observe(isScrubbing: Boolean) {
        roPlayer.listen { events ->
            if (events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)) {
                if (isScrubbing) return@listen

                if (shouldShowIndefinitely()) {
                    controlTimeoutVisibility = Int.MAX_VALUE
                } else if (controlTimeoutVisibility > PLAYER_CONTROL_VISIBILITY_TIMEOUT) {
                    controlTimeoutVisibility =
                        PLAYER_CONTROL_VISIBILITY_TIMEOUT
                }
            }
        }
    }

    companion object {
        private const val PLAYER_CONTROL_VISIBILITY_TIMEOUT = 5

        @Composable
        fun rememberControlsVisibilityState(
            player: RoPlayer,
            isScrubbing: () -> Boolean,
        ): ControlsVisibilityState {
            val state = remember(player) { ControlsVisibilityState(player) }
            LaunchedEffect(state) {
                snapshotFlow { state.controlTimeoutVisibility }.collect {
                    if (it > 0) {
                        state.isVisible = true
                        delay(1000L)
                        state.controlTimeoutVisibility--
                    } else {
                        state.isVisible = false
                    }
                }

            }
            LaunchedEffect(player) {
                snapshotFlow(isScrubbing).collect {
                    state.observe(isScrubbing = it)
                }
            }
            return state
        }
    }
}