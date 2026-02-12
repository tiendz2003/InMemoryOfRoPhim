package com.manutd.ronaldo.impl.utils

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manutd.ronaldo.network.model.Actor
import com.manutd.ronaldo.network.model.Channel
import com.manutd.ronaldo.network.model.ChannelType
import com.manutd.ronaldo.network.model.Group
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlin.collections.mapNotNull

sealed class HomeSectionConfig {
    abstract val id: String

    @Immutable
    data class Poster(
        override val id: String,
        val channels: ImmutableList<Channel>,
        val autoScrollDelayMs: Long = 3000L
    ) : HomeSectionConfig()

    @Immutable
    data class HorizontalList(
        override val id: String,
        val title: String,
        val channels: ImmutableList<Channel>,
        val showSeeAll: Boolean = true
    ) : HomeSectionConfig()

    @Immutable
    data class TopRanked(
        override val id: String,
        val title: String,
        val channels: ImmutableList<Channel>
    ) : HomeSectionConfig()

    @Immutable
    data class ActorList(
        override val id: String,
        val title: String,
        val actors: ImmutableList<Actor>,
        val showSeeAll: Boolean = true
    ) : HomeSectionConfig()
}

fun List<Group>.toHomeSections(): ImmutableList<HomeSectionConfig> {
    return mapNotNull { group ->
        when (group.type) {
            ChannelType.SLIDER -> HomeSectionConfig.Poster(
                id = group.id,
                channels = group.channels
            )

            ChannelType.HORIZONTAL -> HomeSectionConfig.HorizontalList(
                id = group.id,
                title = group.name ?: "",
                channels = group.channels
            )

            ChannelType.TOP -> HomeSectionConfig.TopRanked(
                id = group.id,
                title = group.name ?: "",
                channels = group.channels
            )

            ChannelType.ACTOR -> HomeSectionConfig.ActorList(
                id = group.id,
                title = group.name ?: "",
                actors = group.actors,
                showSeeAll = true
            )

            ChannelType.UNKNOWN -> null
        }
    }.toImmutableList()
}


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