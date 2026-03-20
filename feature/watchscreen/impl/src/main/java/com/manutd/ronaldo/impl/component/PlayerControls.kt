package com.manutd.ronaldo.impl.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsStateWithLifecycle
import com.manutd.ronaldo.designsystem.animation.VerticalSlideAnimation
import com.manutd.ronaldo.designsystem.component.RoIcon
import com.manutd.ronaldo.designsystem.icon.IconType
import com.manutd.ronaldo.impl.R
import com.manutd.ronaldo.impl.gesture.PlayerGestureHandler
import com.manutd.ronaldo.impl.screen.WatchScreenViewModel
import com.manutd.ronaldo.impl.utils.UiMode
import com.manutd.rophim.noIndicationClickable
import com.rophim.player.manager.BrightnessManager
import com.rophim.player.state.ControlsVisibilityState
import com.rophim.player.manager.VolumeManager
import com.rophim.player.state.PlayerGestureState
import com.rophim.player.state.SeekButtonState
import com.rophim.player.utils.RoPlayer

@Composable
internal fun PlayerControls(
    player: RoPlayer,
    viewModel: WatchScreenViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.collectAsStateWithLifecycle()
    val volumeManager = VolumeManager.rememberVolumeManager(player = player)
    val brightnessManager = BrightnessManager.rememberBrightnessManager()
    val seekButtonState = SeekButtonState.rememberSeekButtonState(player = player)
    val gestureState =
        PlayerGestureState.rememberPlayerGestureState(seekButtonState.seekForwardAmountMs)
    val controlsVisibilityState = ControlsVisibilityState.rememberControlsVisibilityState(
        player = player,
        isScrubbing = { state.isDraggingSlider }
    )
    var isLocked by remember { mutableStateOf(false) }
    var uiMode by remember { mutableStateOf(UiMode.None) }
    var bottomControlsHeightPx by remember { mutableIntStateOf(0) }

    // queueControlVisibility: cờ để restore controls sau khi gesture kết thúc
    // queuePlay: cờ để resume player sau khi gesture kết thúc

    var queueControlVisibility by remember { mutableStateOf(false) }
    var queuePlay by remember { mutableStateOf(false) }

    // Center controls chỉ hiện khi không có gì đang active

    val areCenterControlsVisible by remember {
        derivedStateOf {
            controlsVisibilityState.isVisible
                    && uiMode.isNone
                    && !gestureState.isDoubleTapping
                    && !gestureState.isSliding
                    && !gestureState.isSpeedBoosting
                    && !state.isDraggingSlider
        }
    }
    BackHandler(enabled = isLocked) {
        controlsVisibilityState.show()
    }
    LaunchedEffect(
        gestureState.isDoubleTapping,
        gestureState.isSliding,
        gestureState.isSpeedBoosting,
        uiMode,
        state.isDraggingSlider
    ) {
        val gestureActive = gestureState.isDoubleTapping
                || gestureState.isSliding
                || gestureState.isSpeedBoosting
                || !uiMode.isNone
                || state.isDraggingSlider

        if (controlsVisibilityState.isVisible && gestureActive) {
            // Gesture bắt đầu: ẩn controls, queue để restore sau
            queueControlVisibility = true
            controlsVisibilityState.hide()
            //paausse player khi đang gesture(trừ subtitle panel)
            if (player.isPlaying && !uiMode.isSubs) {
                player.pause()
            }
        } else if (queueControlVisibility && !gestureActive) {
            // Gesture kết thúc: restore controls và player
            queueControlVisibility = false
            controlsVisibilityState.show()
            if (queuePlay && !player.isPlaying) {
                queuePlay = false
                player.play()
            }
        }
    }
    //scrubbing → controls luôn hiện
    LaunchedEffect(state.isDraggingSlider) {
        if (state.isDraggingSlider) {
            controlsVisibilityState.show(true)
        }
    }
    // Effect: speed boost → 2x playback
    LaunchedEffect(gestureState.isSpeedBoosting) {
        val speed = if (gestureState.isSpeedBoosting) 2f else 1f
        player.setPlaybackSpeed(speed)
    }
    Box(modifier = modifier.fillMaxSize()) {
        //Gradient scrim overlay (top + bottom)
        ControlsBlackOverlay(controlsVisibilityState.isVisible && !isLocked, Modifier.fillMaxSize())
        // AnimatedContent: Locked vs Unlocked UI tree
        // Khi lock, gesture handler bị remove hoàn toàn — không chỉ ẩn
        AnimatedContent(
            targetState = isLocked,
            transitionSpec = {
                ContentTransform(
                    targetContentEnter = fadeIn(),
                    initialContentExit = fadeOut()
                )
            },
            label = "locked_state"
        ) { locked ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (locked) {
                    //nếu locked :chỉ show unlock button
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .noIndicationClickable { controlsVisibilityState.toggle() }
                    )
                    AnimatedVisibility(
                        visible = controlsVisibilityState.isVisible,
                        enter = fadeIn(),
                        exit = fadeOut(tween(400))
                    ) {
                        LockControls(
                            unlock = { isLocked = false },
                            showControls = { controlsVisibilityState.show() })
                    }
                } else {
                    //unlocked:full control
                    PlayerGestureHandler(
                        gestureState = gestureState,
                        brightnessManager = brightnessManager,
                        volumeManager = volumeManager,
                        areControlsVisible = controlsVisibilityState.isVisible,
                        onSeekForward = viewModel::seekForward,
                        onSeekBackward = viewModel::seekBackward,
                        onSingleTap = {
                            if (!uiMode.isNone) uiMode = UiMode.None
                            else controlsVisibilityState.toggle()
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    VerticalSlideAnimation(
                        visible = controlsVisibilityState.isVisible,
                        slideDown = false,
                        modifier = Modifier.align(Alignment.TopCenter)
                    ) {
                        PlayerTopBar(
                            title = state.movieId,    // TODO: replace với movie title
                            onBack = onBack,
                            episode = state.episode
                        )
                    }

                    VerticalSlideAnimation(
                        visible = gestureState.isSpeedBoosting,
                        slideDown = false,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 72.dp) // dưới top bar
                    ) {
                        SpeedBoostIndicator()
                    }

                    AnimatedVisibility(
                        visible = areCenterControlsVisible,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        CenterControls(
                            isPlaying = state.isPlaying,
                            isBuffering = state.isBuffering,
                            onPlayPauseClick = {
                                viewModel.togglePlayPause()
                                controlsVisibilityState.show()
                            },
                            onSeekForward = {
                                viewModel.seekForward()
                                controlsVisibilityState.show()
                            },
                            onSeekBackward = {
                                viewModel.seekBackward()
                                controlsVisibilityState.show()
                            }
                        )
                    }

                    VerticalSlideAnimation(
                        visible = controlsVisibilityState.isVisible,
                        slideDown = true,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .onSizeChanged {
                                if (it.height > 0) bottomControlsHeightPx = it.height
                            }
                    ) {
                        BottomControls(
                            currentPositionMs = { state.displayPositionMs },
                            durationMs = { state.durationMs },
                            bufferedPositionMs = { state.bufferedPositionMs },
                            progress = { state.progress },
                            isPlaying = state.isPlaying,
                            isDraggingSlider = state.isDraggingSlider,
                            uiMode = uiMode,
                            onSliderDragStart = viewModel::onSliderDragStart,
                            onSliderDrag = viewModel::onSliderDrag,
                            onSliderDragEnd = viewModel::onSliderDragEnd,
                            onToggleUiPanel = { uiMode = it },
                            onInteraction = { controlsVisibilityState.show() }
                        )
                    }
                    //todo:triển khai thêm các panel cho episode,serves,playback
                }
            }

        }
    }
}

@Composable
fun LockControls(
    unlock: () -> Unit,
    showControls: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(Color.Black.copy(0.3F))
            }
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 45.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            onClick = {
                unlock()
                showControls()
            }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RoIcon(
                    icon = IconType.Drawable(R.drawable.round_lock_open_24),
                    contentDescription = stringResource(R.string.unlock),
                    tint = Color.White,
                    dp = 40.dp,
                )

                Text(
                    text = stringResource(R.string.unlock),
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Composable
fun SpeedBoostIndicator() {

}

@Composable
fun CenterControls() {

}

@Composable
fun BottomControls() {

}
