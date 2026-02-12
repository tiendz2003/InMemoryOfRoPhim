package com.manutd.ronaldo.impl.screen.item

import com.manutd.ronaldo.impl.utils.RoItemConstants


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.manutd.ronaldo.designsystem.component.RoThumbnailImage
import com.manutd.ronaldo.designsystem.theme.RoTheme
import com.manutd.ronaldo.network.model.Channel

@Preview
@Composable
fun TopRankedMovieItemPreview() {
    RoTheme(

    ) {
        TopRankedMovieItem(
            channel = Channel(
                id = "top_001",
                name = "Breaking Bad",
                display = "Breaking Bad: Trở Thành Tội Phạm",
                description = "Một giáo viên hóa học được chẩn đoán mắc bệnh ung thư phổi hợp tác với một cựu học sinh để sản xuất và bán停止.",
                logoUrl = "https://image.tmdb.org/t/p/original/ztkUQFLlC19CCMYHW9o1zWhJRNq.jpg",
                streamUrl = "https://example.com/stream/breaking_bad.m3u8",
                shareUrl = "https://example.com/share/breaking_bad",
                imdb = "9.5",
                quality = "4K",
                rating = "5.0",
                year = "20",
                episode = "62 tập",
            ),
            rank = 1,
            onClick = {}

        )
    }

}


@Composable
fun TopRankedMovieItem(
    channel: Channel,
    rank: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(RoItemConstants.TopRankedWidth)

    ) {
        Box {
            val shape = RoundedCornerShape(RoItemConstants.TopRankedCornerRadius)
            Card(
                shape = shape,
                border = BorderStroke(1.dp, Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(RoItemConstants.TopRankedHeight)
                    .clip(shape)
                    .clickable(onClick = onClick)
            ) {
                Box {
                    // Poster Image
                    RoThumbnailImage(
                        model = channel.logoUrl,
                        contentDescription = channel.name,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay at bottom
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )

                    // Age Rating Badge (Top Right)
                    channel.rating?.let { rating ->
                        AgeBadge(
                            text = rating,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                        )
                    }
                }
            }

        }


        MovieTitleSection(
            title = channel.name,
            subtitle = channel.display,
            rank = rank,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RankNumber(
    rank: Int,
    modifier: Modifier = Modifier
) {
    val goldGradient = remember {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFFFD700), // Gold
                Color(0xFFFFA500)  // Orange Gold
            )
        )
    }

    Text(
        modifier = modifier,
        text = rank.toString(),
        fontSize = RoItemConstants.TopRankNumberSize,
        fontWeight = FontWeight.Black,
        style = MaterialTheme.typography.displayMedium.copy(
            brush = goldGradient,
            shadow = Shadow(
                color = Color.Black.copy(alpha = 0.5f),
                offset = Offset(4f, 4f),
                blurRadius = 8f
            )
        )
    )
}

@Composable
private fun AgeBadge(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Color.Black.copy(alpha = 0.7f),
                shape = RoundedCornerShape(RoItemConstants.BadgeCornerRadius)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MovieTitleSection(
    title: String,
    subtitle: String,
    rank: Int,
    modifier: Modifier = Modifier
) {
    // 1. Đặt tất cả vào trong cùng một Row
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Bên trái: RankNumber
        RankNumber(
            rank = rank,
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}