package com.manutd.ronaldo.designsystem.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun VerticalSlideAnimation(
    visible: Boolean,
    slideDown: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { (it / 4) * (if (slideDown) 1 else -1) },
        exit = fadeOut() + slideOutVertically { (it / 6) * (if (slideDown) 1 else -1) },
        content = content,
        modifier = modifier,
    )
}

@Composable
private fun AnimatedPanel(
    visible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { it / 4 },
        exit = fadeOut() + slideOutVertically { it / 6 },
        modifier = modifier,
        content = content
    )
}