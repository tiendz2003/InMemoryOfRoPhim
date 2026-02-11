package com.manutd.ronaldo.impl.utils

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manutd.ronaldo.network.model.Channel
import com.manutd.ronaldo.network.model.ChannelType
import com.manutd.ronaldo.network.model.Group
import kotlin.collections.mapNotNull

sealed class HomeSection {
    abstract val id: String

    data class Carousel(
        override val id: String,
        val channels: List<Channel>,
        val autoScrollEnabled: Boolean = true,
        val autoScrollDelayMs: Long = 3000L
    ) : HomeSection()

    data class HorizontalList(
        override val id: String,
        val title: String,
        val channels: List<Channel>,
        val showSeeAll: Boolean = true
    ) : HomeSection()

    data class TopRanked(
        override val id: String,
        val title: String,
        val channels: List<Channel>
    ) : HomeSection()
}

fun List<Group>.toHomeSections(): List<HomeSection> {
    return mapNotNull { group ->
        when (group.type) {
            ChannelType.SLIDER -> HomeSection.Carousel(
                id = group.id,
                channels = group.channels
            )
            ChannelType.HORIZONTAL -> HomeSection.HorizontalList(
                id = group.id,
                title = group.name ?: "",
                channels = group.channels
            )
            ChannelType.TOP -> HomeSection.TopRanked(
                id = group.id,
                title = group.name ?: "",
                channels = group.channels
            )
            ChannelType.UNKNOWN -> null
        }
    }
}


object MovieItemConstants {
    // Top Ranked
    val TopRankedWidth = 180.dp
    val TopRankedHeight = 240.dp
    val TopRankedCornerRadius = 12.dp
    val TopRankNumberSize = 72.sp

    // Horizontal
    val HorizontalItemWidth = 160.dp
    val HorizontalItemHeight = 90.dp
    val HorizontalCornerRadius = 8.dp

    // Common
    val BadgePadding = 6.dp
    val BadgeCornerRadius = 4.dp
}

// File: MovieBadge.kt
data class MovieBadge(
    val text: String,
    val type: BadgeType
)

enum class BadgeType {
    AGE_RATING,      // PĐ-3, PĐ-6
    TIME_LIMIT,      // LT-28
    PROMOTION,       // Banner khuyến mãi
    QUALITY,         // HD, 4K
    TRENDING         // Hot, New
}