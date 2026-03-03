package com.manutd.ronaldo.designsystem.utils

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
