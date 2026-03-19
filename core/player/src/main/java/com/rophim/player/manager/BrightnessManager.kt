package com.rophim.player.manager

import android.app.Activity
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner

@Stable
class BrightnessManager(private val activity: Activity) {
    val maxBrightness: Float
        get() = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_FULL  // = 1.0f

    var currentBrightness by mutableFloatStateOf(readWindowBrightness())
        private set

    val currentBrightnessPercentage by derivedStateOf {
        currentBrightness.coerceIn(0f, 1f)
    }

    /** Đọc brightness hiện tại từ window, fallback 0.5f nếu là system default (-1) */
    private fun readWindowBrightness(): Float {
        val lp = activity.window.attributes
        return if (lp.screenBrightness < 0f) 0.5f else lp.screenBrightness
    }

    fun setBrightness(brightness: Float) {
        currentBrightness = brightness.coerceIn(0f, maxBrightness)
        val lp = activity.window.attributes
        lp.screenBrightness = currentBrightness
        activity.window.attributes = lp
    }

    /** Reset về system brightness khi thoát màn Watch */
    fun resetToSystemBrightness() {
        val lp = activity.window.attributes
        lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
        activity.window.attributes = lp
    }

    companion object {
        @Composable
        fun rememberBrightnessManager(): BrightnessManager {
            val activity = LocalContext.current as Activity
            val manager = remember { BrightnessManager(activity) }

            // Reset brightness khi rời WatchScreen — tránh toàn app bị tối/sáng
            DisposableEffect(LocalLifecycleOwner.current) {
                onDispose { manager.resetToSystemBrightness() }
            }

            return manager
        }
    }
}