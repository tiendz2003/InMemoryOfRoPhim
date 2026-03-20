package com.manutd.ronaldo.impl.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.manutd.ronaldo.designsystem.animation.VerticalSlideAnimation

@Composable
internal fun ControlsBlackOverlay(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        //top gradient
        VerticalSlideAnimation(visible, false) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawRect(
                            Brush.verticalGradient(
                                0f to Color.Black.copy(alpha = 0.8f),
                                0.2f to Color.Transparent
                            )
                        )
                    }
            )
        }
        //bottom gradient
        VerticalSlideAnimation(visible, true) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawRect(
                            Brush.verticalGradient(
                                0.75f to Color.Transparent,
                                1f to Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    }
            )
        }
    }
}