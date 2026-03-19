package com.rophim.player.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class PlayerGestureState {
    //ẩn /hiển thị thanh volume/brightness
    var isVolumeSliderVisible by mutableStateOf(false)
        private set
    var isBrightnessSliderVisible by mutableStateOf(false)
        private set
    //tua tap
    var isDoubleTapSeekingForward by mutableStateOf(false)
        private set
    var isDoubleTapSeekingBackward by mutableStateOf(false)
        private set
    var seekSeconds by mutableIntStateOf(0)
        private set
    val isDoubleTapping: Boolean
        get() = isDoubleTapSeekingForward || isDoubleTapSeekingBackward
    //tua nhanh
    var isSpeedBoosting by mutableStateOf(false)
        private set
    fun showVolumeSlider() {
        isVolumeSliderVisible = true
        isBrightnessSliderVisible = false
    }
    fun showBrightnessSlider() {
        isBrightnessSliderVisible = true
        isVolumeSliderVisible = false
    }
    fun hideSliders() {
        isVolumeSliderVisible = false
        isBrightnessSliderVisible = false
    }
    fun onDoubleTap(isForward: Boolean) {
        if (isForward) {
            isDoubleTapSeekingForward = true
            isDoubleTapSeekingBackward = false
        } else {
            isDoubleTapSeekingBackward = true
            isDoubleTapSeekingForward = false
        }
        seekSeconds += 10
    }
    fun hideSeekOverlay() {
        isDoubleTapSeekingForward = false
        isDoubleTapSeekingBackward = false
        seekSeconds = 0
    }
    fun startSpeedBoost() { isSpeedBoosting = true }
    fun stopSpeedBoost()  { isSpeedBoosting = false }
    companion object {
        @Composable
        fun rememberPlayerGestureState() = remember { PlayerGestureState() }
    }
}