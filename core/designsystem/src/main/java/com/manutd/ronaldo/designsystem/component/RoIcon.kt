package com.manutd.ronaldo.designsystem.component

import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.manutd.ronaldo.designsystem.icon.IconType

@Composable
fun RoIcon(
    icon: IconType,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    when (icon) {
        is IconType.Vector -> {
            Icon(
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint
            )
        }

        is IconType.Drawable -> {
            Icon(
                painter = painterResource(id = icon.id),
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint
            )
        }
    }
}