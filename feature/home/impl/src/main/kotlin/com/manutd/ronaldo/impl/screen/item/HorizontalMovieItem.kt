package com.manutd.ronaldo.impl.screen.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.manutd.ronaldo.impl.utils.BadgeType
import com.manutd.ronaldo.impl.utils.MovieBadge
import com.manutd.ronaldo.impl.utils.MovieItemConstants
import com.manutd.ronaldo.network.model.Channel

@Composable
fun HorizontalMovieItem(
    channel: Channel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    badges: List<MovieBadge> = emptyList(),
) {
    Column(
        modifier = modifier
            .width(MovieItemConstants.HorizontalItemWidth)
            .clickable(onClick = onClick)
    ) {
        Card(
            shape = RoundedCornerShape(MovieItemConstants.HorizontalCornerRadius),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(MovieItemConstants.HorizontalItemHeight)
        ) {
            Box {
                // Thumbnail Image
                AsyncImage(
                    model = channel.logoUrl,
                    contentDescription = channel.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Badges Row (Top Left)
                if (badges.isNotEmpty()) {
                    BadgesRow(
                        badges = badges,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(6.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = channel.name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@Composable
private fun BadgesRow(
    badges: List<MovieBadge>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        badges.forEach { badge ->
            MovieBadgeItem(badge = badge)
        }
    }
}

@Composable
private fun MovieBadgeItem(
    badge: MovieBadge,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (badge.type) {
        BadgeType.AGE_RATING -> Color.Black.copy(alpha = 0.7f)
        BadgeType.TIME_LIMIT -> Color(0xFF00BCD4).copy(alpha = 0.9f) // Cyan
        BadgeType.QUALITY -> Color(0xFF4CAF50).copy(alpha = 0.9f) // Green
        BadgeType.TRENDING -> Color(0xFFFF5722).copy(alpha = 0.9f) // Red
        BadgeType.PROMOTION -> Color(0xFFFF9800).copy(alpha = 0.9f) // Orange
    }

    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(MovieItemConstants.BadgeCornerRadius)
            )
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(
            text = badge.text,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
