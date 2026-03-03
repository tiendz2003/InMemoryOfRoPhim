package com.manutd.ronaldo.impl.screen.item


import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manutd.ronaldo.designsystem.component.RoBadgeUiModel
import com.manutd.ronaldo.designsystem.component.RoMediaCard
import com.manutd.ronaldo.designsystem.utils.BadgeType
import com.manutd.ronaldo.designsystem.utils.MovieBadge

import com.manutd.ronaldo.network.model.Channel
import com.manutd.rophim.core.data.utils.FakeDataProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.collections.map

@Preview
@Composable
fun MovieItemPreview() {
    val channel = FakeDataProvider.getHomeData().groups.first()
        .channels.first()
    MovieItem(
        channel = channel,
        onClick = {},
    )

}

@Composable
fun MovieItem(
    channel: Channel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badges: ImmutableList<MovieBadge> = persistentListOf(),
) {
    val uiBadges = remember(badges) {
        badges.map { badge ->
            val color = when (badge.type) {
                BadgeType.AGE_RATING -> Color.Black.copy(alpha = 0.7f)
                BadgeType.TIME_LIMIT -> Color(0xFF00BCD4).copy(alpha = 0.9f)
                BadgeType.QUALITY -> Color(0xFF4CAF50).copy(alpha = 0.9f) // Green
                BadgeType.TRENDING -> Color(0xFFFF5722).copy(alpha = 0.9f) // Red
            }
            RoBadgeUiModel(text = badge.text, color = color)
        }
    }
    RoMediaCard(
        title = channel.name,
        subtitle = channel.display,
        imageUrl = channel.logoUrl,
        badges = uiBadges,
        onClick = onClick,
        modifier = modifier.width(140.dp)
    )
}
