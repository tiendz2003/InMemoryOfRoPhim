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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.manutd.ronaldo.designsystem.utils.getBadges
import com.manutd.ronaldo.impl.screen.item.MovieItem
import com.manutd.ronaldo.network.model.Channel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun HorizontalListSection(
    title: String,
    channels: ImmutableList<Channel>,
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
                val badges =
                    remember(channel.id, channel.rating, channel.episode, channel.quality) {
                        getBadges(
                            rating = channel.rating,
                            episode = channel.episode,
                            quality = channel.quality
                        )
                    }
                MovieItem(
                    channel = channel,
                    onClick = { onChannelClick(channel) },
                    badges = badges.toImmutableList(),
                )
            }
        }
    }
}
