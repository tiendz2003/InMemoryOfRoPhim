package com.manutd.ronaldo.impl.gesture

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.manutd.ronaldo.designsystem.utils.noIndicationClickable
import com.manutd.ronaldo.feature.watchscreen.impl.R
import com.rophim.player.manager.BrightnessManager
import com.rophim.player.manager.VolumeManager
import com.rophim.player.state.PlayerGestureState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DRAG_MULTIPLIER = 2f
private const val HIDE_DELAY_MS = 1_000L
private const val SEEK_ANIM_DELAY_MS = 600L
private const val LONG_PRESS_SPEED = 2f

@Composable
fun PlayerGestureHandler(
    gestureState: PlayerGestureState,
    brightnessManager: BrightnessManager,
    volumeManager: VolumeManager,
    areControlsVisible: Boolean,
    onSeekForward: () -> Unit,
    onSeekBackward: () -> Unit,
    onSingleTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val windowWidthInPx = LocalWindowInfo.current.containerSize.width
    val screenWidth = with(density) { windowWidthInPx.toDp() }

    var screenHeight by remember { mutableIntStateOf(0) }
    var sliderHideJob: Job? by remember { mutableStateOf(null) }
    var seekAnimJob: Job? by remember { mutableStateOf(null) }

    val leftInteractionSource = remember { MutableInteractionSource() }
    val rightInteractionSource = remember { MutableInteractionSource() }
    //khi control hiện lên thì ẩn slider để không conflict
    LaunchedEffect(areControlsVisible) {
        if (areControlsVisible) {
            gestureState.hideSliders()
            sliderHideJob?.cancel()
        }
    }
    // Auto-hide seek overlay sau khi double-tap
    LaunchedEffect(gestureState.seekSeconds, gestureState.isDoubleTapping) {
        if (gestureState.isDoubleTapping && gestureState.seekSeconds > 0) {
            delay(SEEK_ANIM_DELAY_MS)
            gestureState.hideSeekOverlay()
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged {
                screenHeight = it.height
            }
            .pointerInput(Unit) {
                awaitEachGesture {
                    val longPressTimeout = viewConfiguration.longPressTimeoutMillis
                    val touchSlop = viewConfiguration.touchSlop
                    val down = awaitFirstDown(requireUnconsumed = false)

                    val trigger = withTimeoutOrNull(longPressTimeout) {
                        while (true) {
                            val event = awaitPointerEvent(PointerEventPass.Initial)
                            if (event.changes.all { !it.pressed }) break
                            val moved = event.changes.any {
                                (it.position - down.position).getDistance() > touchSlop
                            }
                            if (moved) break
                        }
                    } == null// null == timeout = bấm lâu kích hoạt
                    if (trigger) {
                        gestureState.startSpeedBoost()
                        //Giuwx speed đến khi nhả ngón tay
                        while (true) {
                            val event = awaitPointerEvent(PointerEventPass.Initial)
                            event.changes.forEach { it.consume() }
                            if (event.changes.all { !it.pressed }) break
                        }
                        gestureState.stopSpeedBoost()

                    }
                }
            }.noIndicationClickable { onSingleTap() }
            .onSizeChanged { size ->
                screenHeight = size.height
            }
    ) {
        var dragStartBrightness by remember { mutableFloatStateOf(0f) }
        //Brightness
        GestureBox(
            interactionSource = leftInteractionSource,
            screenWidth = screenWidth,
            onSingleTap = onSingleTap,
            onDoubleTap = { offset ->
                scope.launch {
                    seekAnimJob?.cancel()
                    seekAnimJob = launch {
                        val press = PressInteraction.Press(offset)
                        leftInteractionSource.emit(press)
                        gestureState.onDoubleTap(isForward = false)
                        onSeekBackward()
                        leftInteractionSource.emit(PressInteraction.Release(press))
                    }
                }
            },
            onDragStart = {
                // Lưu brightness hiện tại làm baseline mỗi lần drag mới
                dragStartBrightness = brightnessManager.currentBrightness
                gestureState.showBrightnessSlider()
            },
            onDragEnd = {
                sliderHideJob?.cancel()
                sliderHideJob = scope.launch {
                    delay(HIDE_DELAY_MS)
                    gestureState.hideSliders()
                }
            },
            onVerticalDrag = { dragAmount ->
                sliderHideJob?.cancel()
                // dragAmount âm = kéo lên = tăng brightness
                val delta =
                    dragAmount * (DRAG_MULTIPLIER * brightnessManager.maxBrightness) / screenHeight
                val newBrightness = dragStartBrightness - delta
                brightnessManager.setBrightness(newBrightness)
                // Update baseline để lần drag tiếp theo tính từ đây
                dragStartBrightness = brightnessManager.currentBrightness
            },
            modifier = Modifier.align(Alignment.CenterStart)
        )

        // Volume
        var dragStartVolume by remember { mutableFloatStateOf(0f) }
        GestureBox(
            interactionSource = rightInteractionSource,
            screenWidth = screenWidth,
            onSingleTap = onSingleTap,
            onDoubleTap = { offset ->
                scope.launch {
                    seekAnimJob?.cancel()
                    seekAnimJob = launch {
                        val press = PressInteraction.Press(offset)
                        rightInteractionSource.emit(press)
                        gestureState.onDoubleTap(isForward = true)
                        onSeekForward()
                        rightInteractionSource.emit(PressInteraction.Release(press))
                    }
                }
            },
            onDragStart = {
                dragStartVolume = volumeManager.currentVolume
                gestureState.showVolumeSlider()
            },
            onDragEnd = {
                sliderHideJob?.cancel()
                sliderHideJob = scope.launch {
                    delay(HIDE_DELAY_MS)
                    gestureState.hideSliders()
                }
            },
            onVerticalDrag = { dragAmount ->
                sliderHideJob?.cancel()
                val delta = dragAmount * (DRAG_MULTIPLIER * volumeManager.maxVolume) / screenHeight
                val newVolume = dragStartVolume - delta
                volumeManager.setVolume(newVolume)
                dragStartVolume = volumeManager.currentVolume
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        )
        //seek overlay
        DoubleTapSeekOverlay(
            isVisible = gestureState.isDoubleTapSeekingBackward,
            isForward = false,
            seekSeconds = gestureState.seekSeconds,
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .fillMaxHeight()
                .align(Alignment.CenterStart)
        )
        DoubleTapSeekOverlay(
            isVisible = gestureState.isDoubleTapSeekingForward,
            isForward = true,
            seekSeconds = gestureState.seekSeconds,
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
        )
        PlayerVerticalSlider(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight()
                .width(64.dp)
                .background(
                    Brush.horizontalGradient(
                        0f to Color.Black.copy(alpha = 0.6f),
                        1f to Color.Transparent
                    )
                ),
            isVisible = gestureState.isBrightnessSliderVisible,
            value = brightnessManager.currentBrightnessPercentage,
            onValueChange = { brightnessManager.setBrightness(it) },
            iconPainter = {
                val currentBrightnessPercentage = brightnessManager.currentBrightnessPercentage
                val icon = when {
                    currentBrightnessPercentage > 0.8F -> R.drawable.brightness_full
                    currentBrightnessPercentage < 0.5F && currentBrightnessPercentage > 0F -> R.drawable.brightness_half
                    currentBrightnessPercentage <= 0F -> R.drawable.brightness_empty
                    else -> R.drawable.brightness_full
                }

                painterResource(icon)
            },
            valueRange = 0f..1f
        )
        PlayerVerticalSlider(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .background(
                    Brush.horizontalGradient(
                        0f to Color.Black.copy(0.6f),
                        1f to Color.Transparent,
                    )
                ),
            isVisible = gestureState.isVolumeSliderVisible,
            iconPainter = {
                val currentVolumePercentage = volumeManager.currentVolumePercentage
                val icon = when {
                    currentVolumePercentage > 0.8F -> R.drawable.volume_up_black_24dp
                    currentVolumePercentage < 0.5F && currentVolumePercentage > 0F -> R.drawable.volume_down_black_24dp
                    currentVolumePercentage <= 0F -> R.drawable.volume_off_black_24dp
                    else -> R.drawable.volume_up_black_24dp
                }

                painterResource(icon)
            },
            value = volumeManager.currentVolumePercentage,
            onValueChange = { volumeManager.setVolume(it * volumeManager.maxVolume) },
            valueRange = 0f..1f
        )
    }
}

@Composable
private fun GestureBox(
    interactionSource: MutableInteractionSource,
    screenWidth: Dp,
    onSingleTap: () -> Unit,
    onDoubleTap: (Offset) -> Unit,
    onDragStart: () -> Unit,
    onDragEnd: () -> Unit,
    onVerticalDrag: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.45f)
            .fillMaxHeight()
            .indication(
                interactionSource,
                ripple(
                    bounded = true,
                    radius = screenWidth / 2f,
                )
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onSingleTap() },
                    onDoubleTap = { offset -> onDoubleTap(offset) }
                )
            }
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = { onDragStart() },
                    onDragEnd = { onDragEnd() },
                    onDragCancel = { onDragEnd() },
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        onVerticalDrag(dragAmount)
                    }
                )
            }
    )
}