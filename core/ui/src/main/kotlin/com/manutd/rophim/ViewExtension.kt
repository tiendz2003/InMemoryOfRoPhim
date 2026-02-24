package com.manutd.rophim

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp

fun Modifier.customBlur(radius: Dp): Modifier {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        this.graphicsLayer {
            renderEffect = RenderEffect
                .createBlurEffect(
                    radius.toPx(),
                    radius.toPx(),
                    Shader.TileMode.MIRROR
                )
                .asComposeRenderEffect()
        }
    } else {
        this // Fallback cho Android cũ (không blur) hoặc dùng thư viện ngoài
    }
}