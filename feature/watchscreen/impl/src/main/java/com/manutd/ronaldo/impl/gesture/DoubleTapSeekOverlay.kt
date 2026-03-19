package com.manutd.ronaldo.impl.gesture

import android.R.attr.contentDescription
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manutd.ronaldo.designsystem.component.RoIcon
import com.manutd.ronaldo.designsystem.icon.IconType
import com.manutd.ronaldo.designsystem.theme.RoTheme
import com.manutd.ronaldo.impl.R

@Composable
internal fun DoubleTapSeekOverlay(
    isVisible: Boolean,
    isForward: Boolean,
    seekSeconds: Int,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(
                initialOffsetX = { it * if (!isForward) 1 else -1 },
                animationSpec = tween(durationMillis = 300)
            ) + fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300)) + slideOutHorizontally(
                targetOffsetX = { it * if (!isForward) -1 else 1 },
                animationSpec = tween(durationMillis = 300)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
            ) {
                AnimatedSeekArrows(isForward)
                if (seekSeconds > 0) {
                    Text(
                        text = "${if (isForward) "+" else "-"}$seekSeconds",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        ),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedSeekArrows(isForward: Boolean) {
    val spacing = 12.dp
    val iconSize = 40.dp

    val infiniteTransition = rememberInfiniteTransition(label = "seek_arrows")

    val alpha1 = infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha1"
    )

    val alpha2 = infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, delayMillis = 150, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha2"
    )

    val alpha3 = infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, delayMillis = 300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha3"
    )

    val iconRes = if (isForward) {
        R.drawable.ic_chevron_right
    } else {
        R.drawable.ic_chevron_left
    }

    Box(
        contentAlignment = Alignment.Center,
    ) {
        if (isForward) {
            RoIcon(
                icon = IconType.Drawable(iconRes),
                contentDescription = null,
                tint = Color.White,
                dp = iconSize,
                modifier = Modifier
                    .offset(x = spacing)
                    .graphicsLayer {
                        alpha = alpha1.value
                    }
            )

            RoIcon(
                icon = IconType.Drawable(iconRes),
                contentDescription = null,
                tint = Color.White,
                dp = iconSize,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = alpha2.value
                    }
            )

            RoIcon(
                icon = IconType.Drawable(iconRes),
                contentDescription = null,
                tint = Color.White,
                dp = iconSize,
                modifier = Modifier
                    .offset(x = -spacing)
                    .graphicsLayer {
                        alpha = alpha3.value
                    }
            )
        } else {
            RoIcon(
                icon = IconType.Drawable(iconRes),
                contentDescription = null,
                tint = Color.White,
                dp = iconSize,
                modifier = Modifier
                    .offset(x = -spacing)
                    .graphicsLayer {
                        alpha = alpha3.value
                    }
            )

            RoIcon(
                icon = IconType.Drawable(iconRes),
                contentDescription = null,
                tint = Color.White,
                dp = iconSize,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = alpha2.value
                    }
            )

            RoIcon(
                icon = IconType.Drawable(iconRes),
                contentDescription = null,
                tint = Color.White,
                dp = iconSize,
                modifier = Modifier
                    .offset(x = spacing)
                    .graphicsLayer {
                        alpha = alpha1.value
                    }
            )
        }
    }
}

@Preview(
    device = "spec:parent=pixel_5,orientation=landscape",
    showSystemUi = true,
    showBackground = true
)
@Composable
private fun DoubleTapSeekOverlayPreview() {
    RoTheme() {
        DoubleTapSeekOverlay(
            isVisible = true,
            isForward = true,
            seekSeconds = 10
        )
    }
}