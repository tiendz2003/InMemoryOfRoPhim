package com.manutd.ronaldo.designsystem.utils

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object RoItemConstants {
    // Top Ranked
    val TopRankedWidth = 180.dp
    val TopRankedHeight = 240.dp
    val TopRankedCornerRadius = 12.dp
    val TopRankNumberSize = 56.sp

    // Horizontal
    val HorizontalItemWidth = 140.dp
    val HorizontalItemHeight = 200.dp
    val HorizontalCornerRadius = 8.dp

    // Common
    val BadgePadding = 6.dp
    val BadgeCornerRadius = 16.dp

    //Actor
    val ActorBadgeWidth = 100.dp
    val ActorBadgeHeight = 240.dp
}
@Immutable
data class MovieBadge(
    val text: String,
    val type: BadgeType
)

enum class BadgeType {
    AGE_RATING,      // PĐ-3, PĐ-6
    TIME_LIMIT,      // LT-28
    QUALITY,         // HD, 4K
    TRENDING         // Hot, New
}