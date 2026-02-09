package com.manutd.ronaldo.impl.utils

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