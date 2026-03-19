package com.manutd.ronaldo.impl.gesture

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlayerVerticalSlider(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    iconPainter: @Composable () -> Painter,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
) {
    val sliderColors = SliderDefaults.colors(
        thumbColor = Color.White,
        activeTrackColor = Color.White,
        inactiveTrackColor = Color.White.copy(alpha = 0.4F)
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
            .fillMaxHeight(),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                8.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 30.dp),
        ) {
            Icon(
                painter = iconPainter(),
                contentDescription = null,
                tint = Color.White
            )

            Slider(
                modifier = Modifier
                    .graphicsLayer {
                        rotationZ = 270f
                        transformOrigin = TransformOrigin(0f, 0f)
                    }
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(
                            Constraints(
                                minWidth = constraints.minHeight,
                                maxWidth = constraints.maxHeight,
                                minHeight = constraints.minWidth,
                                maxHeight = constraints.maxHeight,
                            )
                        )

                        layout(placeable.height, placeable.width) {
                            placeable.place(-placeable.width, 0)
                        }
                    }
                    .width(120.dp),
                valueRange = valueRange,
                value = value,
                onValueChange = onValueChange,
                colors = sliderColors,
                track = {
                    SliderDefaults.Track(
                        sliderState = it,
                        drawStopIndicator = {},
                        drawTick = { _, _ -> },
                        thumbTrackGapSize = 0.dp,
                        modifier = Modifier
                            .height(5.dp)
                    )
                },
                thumb = {}
            )
        }
    }
}