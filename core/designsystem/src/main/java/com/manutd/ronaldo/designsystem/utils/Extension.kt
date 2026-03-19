package com.manutd.ronaldo.designsystem.utils

import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

fun getBadges(
    rating: String,
    episode: String,
    quality: String
): List<MovieBadge> {
    return buildList {
        add(MovieBadge("PĐ-${rating}", BadgeType.AGE_RATING))
        episode.let {
            if (it.contains("tập")) {
                add(MovieBadge("LT-28", BadgeType.TIME_LIMIT))
            }
        }
        add(MovieBadge(quality, BadgeType.QUALITY))
    }
}
val WindowWidthSizeClass.isCompact get() = this == WindowWidthSizeClass.COMPACT
val WindowWidthSizeClass.isMedium get() = this == WindowWidthSizeClass.MEDIUM
val WindowWidthSizeClass.isExpanded get() = this == WindowWidthSizeClass.EXPANDED

val WindowHeightSizeClass.isCompact get() = this == WindowHeightSizeClass.COMPACT
val WindowHeightSizeClass.isMedium get() = this == WindowHeightSizeClass.MEDIUM
val WindowHeightSizeClass.isExpanded get() = this == WindowHeightSizeClass.EXPANDED
