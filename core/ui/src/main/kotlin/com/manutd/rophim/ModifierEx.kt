package com.manutd.rophim

import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier

fun Modifier.noIndicationClickable(onClick: () -> Unit): Modifier {
    return clickable(
        indication = null,
        interactionSource = null,
        onClick = onClick,
    )
}

fun Modifier.noOpClickable() = noIndicationClickable {  }