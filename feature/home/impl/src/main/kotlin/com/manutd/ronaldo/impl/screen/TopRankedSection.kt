package com.manutd.ronaldo.impl.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manutd.ronaldo.designsystem.component.RoIcon
import com.manutd.ronaldo.designsystem.icon.RoIcons
import com.manutd.ronaldo.designsystem.theme.RoTheme
import com.manutd.ronaldo.impl.screen.item.TopRankedMovieItem
import com.manutd.ronaldo.network.model.Channel
import com.manutd.rophim.core.data.utils.FakeDataProvider

@Preview
@Composable
fun TopRankedSectionPreview() {
    val channels = FakeDataProvider.getHomeData().groups.first().channels
    RoTheme() {
        TopRankedSection(
            title = "Top 10 Phim Được Xem Nhiều Nhất",
            channels = channels,
            onChannelClick = {},
            onSeeAllClick = {}
        )
    }
}


@Composable
fun TopRankedSection(
    title: String,
    channels: List<Channel>,
    onChannelClick: (Channel) -> Unit,
    modifier: Modifier = Modifier,
    onSeeAllClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF006994), // Xanh nước biển đậm
                        Color.Transparent
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY // Gradient chiếm toàn bộ chiều cao
                )
            )
    ) {
        SectionHeader(
            title = title,
            onSeeAllClick = onSeeAllClick,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Top Ranked Items
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(
                items = channels,
                key = { _, channel -> channel.id }
            ) { index, channel ->
                TopRankedMovieItem(
                    channel = channel,
                    rank = index + 1,
                    onClick = { onChannelClick(channel) }
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    onSeeAllClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        if (onSeeAllClick != null) {
            TextButton(onClick = onSeeAllClick) {
                RoIcon(
                    icon = RoIcons.ArrowRight,
                    contentDescription = "See all",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}