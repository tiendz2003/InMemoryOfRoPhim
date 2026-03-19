package com.manutd.ronaldo.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toolingGraphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.manutd.ronaldo.designsystem.icon.IconType
import com.manutd.ronaldo.designsystem.utils.AdaptiveSizeUtil.getAdaptiveDp
import kotlin.math.max

@Composable
fun RoIcon(
    icon: IconType,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    compact: Dp? = null,
    medium: Dp? = null,
    expanded: Dp? = null,
) {
    val painter = when (icon) {
        is IconType.Vector -> rememberVectorPainter(icon.imageVector)
        is IconType.Drawable -> painterResource(id = icon.id)
    }

    RoIcon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint,
        compact = compact,
        medium = medium,
        expanded = expanded,
    )
}

/**
 * RoIcon tiện lợi hỗ trợ truyền 1 kích thước nền (dp) và tự động tăng size (increaseBy).
 */
@Composable
fun RoIcon(
    icon: IconType,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    dp: Dp? = null,
    increaseBy: Dp = DefaultIconSizeIncreaseBy,
) {
    val painter = when (icon) {
        is IconType.Vector -> rememberVectorPainter(icon.imageVector)
        is IconType.Drawable -> painterResource(id = icon.id)
    }

    RoIcon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint,
        dp = dp,
        increaseBy = increaseBy,
    )
}


@Composable
private fun RoIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    dp: Dp? = null,
    increaseBy: Dp = DefaultIconSizeIncreaseBy,
) {
    val compact = dp ?: painter.getSizeInDp() ?: DefaultIconSize

    RoIcon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint,
        compact = compact,
        medium = compact + increaseBy,
        expanded = compact + (increaseBy * 2),
    )
}

@Composable
private fun RoIcon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    compact: Dp? = null,
    medium: Dp? = null,
    expanded: Dp? = null,
) {
    val colorFilter = remember(tint) {
        if (tint == Color.Unspecified) null else ColorFilter.tint(tint)
    }

    val semantics = if (contentDescription != null) {
        Modifier.semantics {
            this.contentDescription = contentDescription
            this.role = Role.Image
        }
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .toolingGraphicsLayer()
            .defaultSizeFor(
                painter = painter,
                compact = compact,
                medium = medium,
                expanded = expanded,
            )
            .paint(
                painter = painter,
                colorFilter = colorFilter,
                contentScale = ContentScale.Fit
            )
            .then(semantics),
    )
}


@Composable
private fun Modifier.defaultSizeFor(
    painter: Painter,
    compact: Dp?,
    medium: Dp?,
    expanded: Dp?,
): Modifier {
    return if (painter.hasNoValidSize() || compact != null) {
        val baseDp = compact ?: DefaultIconSize
        val (mediumDp, expandedDp) = getMediumAndExpandedDp(
            compact = baseDp,
            medium = medium,
            expanded = expanded,
        )

        size(
            getAdaptiveDp(
                compact = baseDp,
                medium = mediumDp,
                expanded = expandedDp,
            ),
        )
    } else {
        val sizeInDp = painter.getSizeInDp()
            ?: throw NullPointerException("This Icon's painter object has no valid size.")
        val (mediumDp, expandedDp) = getMediumAndExpandedDp(
            compact = sizeInDp,
            medium = medium,
            expanded = expanded,
        )

        size(
            getAdaptiveDp(
                compact = sizeInDp,
                medium = mediumDp,
                expanded = expandedDp,
            ),
        )
    }
}

@Composable
private fun Painter.getSizeInDp(): Dp? {
    if (hasNoValidSize()) {
        return null
    }

    val painterSize = max(intrinsicSize.width, intrinsicSize.height)
    return with(LocalDensity.current) { painterSize.toDp() }
}

private fun Size.isInfinite() = width.isInfinite() && height.isInfinite()

private fun Painter.hasNoValidSize() = intrinsicSize == Size.Unspecified || intrinsicSize.isInfinite()

private fun getMediumAndExpandedDp(
    compact: Dp,
    medium: Dp?,
    expanded: Dp?,
): Pair<Dp, Dp> {
    val mediumDp = medium ?: (compact + DefaultIconSizeIncreaseBy)
    val expandedDp = expanded ?: (mediumDp + DefaultIconSizeIncreaseBy)

    return mediumDp to expandedDp
}

private val DefaultIconSize = 24.dp
private val DefaultIconSizeIncreaseBy = 6.dp