package com.manutd.ronaldo.impl.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.manutd.ronaldo.impl.screen.item.HorizontalMovieItem
import com.manutd.ronaldo.impl.utils.BadgeType
import com.manutd.ronaldo.impl.utils.MovieBadge
import com.manutd.ronaldo.network.model.Channel

@Composable
fun HorizontalListSection(
    title: String,
    channels: List<Channel>,
    onChannelClick: (Channel) -> Unit,
    modifier: Modifier = Modifier,
    onSeeAllClick: (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        SectionHeader(
            title = title,
            onSeeAllClick = onSeeAllClick,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal Items
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(
                items = channels,
                key = { _, channel -> channel.id }
            ) { index, channel ->
                HorizontalMovieItem(
                    channel = channel,
                    onClick = { onChannelClick(channel) },
                    badges = getBadgesForChannel(channel),
                )
            }
        }
    }
}
private fun getBadgesForChannel(channel: Channel): List<MovieBadge> {
    return buildList {
        channel.rating?.let {
            add(MovieBadge("PĐ-$it", BadgeType.AGE_RATING))
        }
        channel.episode?.let {
            if (it.contains("tập")) {
                add(MovieBadge("LT-28", BadgeType.TIME_LIMIT))
            }
        }
        channel.quality?.let {
            add(MovieBadge(it, BadgeType.QUALITY))
        }
    }
}
