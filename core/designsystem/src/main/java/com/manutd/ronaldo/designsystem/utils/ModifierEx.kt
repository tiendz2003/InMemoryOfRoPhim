package com.manutd.ronaldo.designsystem.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

fun Modifier.noIndicationClickable(onClick: () -> Unit): Modifier {
    return clickable(
        indication = null,
        interactionSource = null,
        onClick = onClick,
    )
}

fun Modifier.noOpClickable() = noIndicationClickable { }

/**
 * Fills the maximum width of the parent layout based on the current window size class.
 *
 * @param compact The fraction of the width to fill when the window size class is compact. Default is `1F`.
 * @param medium The fraction of the width to fill when the window size class is medium. Default is `0.8F`.
 * @param expanded The fraction of the width to fill when the window size class is expanded. Default is `0.6F`.
 * */
@Composable
fun Modifier.fillMaxAdaptiveWidth(
    compact: Float = 1F,
    medium: Float = (compact - 0.2F).coerceAtLeast(0f),
    expanded: Float = (medium - 0.2F).coerceAtLeast(0f),
): Modifier {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val windowWidthSizeClass = windowSizeClass.windowWidthSizeClass
    val windowHeightSizeClass = windowSizeClass.windowHeightSizeClass

    val fraction = when {
        windowWidthSizeClass.isCompact || windowHeightSizeClass.isCompact -> compact
        windowWidthSizeClass.isMedium || windowHeightSizeClass.isMedium -> medium
        windowWidthSizeClass.isExpanded -> expanded
        else -> compact
    }

    return fillMaxWidth(fraction)
}