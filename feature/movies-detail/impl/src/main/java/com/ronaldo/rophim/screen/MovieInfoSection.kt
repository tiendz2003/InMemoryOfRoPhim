package com.ronaldo.rophim.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.manutd.ronaldo.designsystem.component.Tag
import com.manutd.ronaldo.designsystem.component.TagType
import com.manutd.ronaldo.network.model.AiringStatus

@Composable
fun MovieInfoSection(
    state: DetailState,
    onSynopsisDetailClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        when (val async = state.movieDetailAsync) {
            is Loading, is Uninitialized -> MovieInfoShimmer()
            is Fail -> ErrorInline(
                message = "Không tải được thông tin phim",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )

            is Success -> {
                val detail = async()

                //Tiêu đề phim
                Text(
                    text = detail.title,
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )

                //Metadata
                MetadataTagRow(badges = state.metadataBadges)

                // ── Thể loại
                if (detail.genres.isNotEmpty()) {
                    GenreRow(genres = detail.genres.map { it.name })
                }

                // ── Badge trạng thái chiếu (Series only) ──────
                AiringStatusBadge(status = detail.airingStatus)

                // ── Synopsis + Chi tiết ────────────────────────
                SynopsisRow(
                    synopsis = detail.synopsis,
                    onDetailClick = onSynopsisDetailClick
                )
            }
        }
    }
}

@Composable
private fun MetadataTagRow(
    badges: List<MetadataBadge>
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        badges.forEach { badge ->
            when (badge) {
                is MetadataBadge.Rating -> Tag(
                    text = badge.score.toString(),
                    type = TagType.IMDB
                )

                is MetadataBadge.Text -> Tag(
                    text = badge.value,
                    type = TagType.OUTLINE
                )
            }
        }
    }
}

@Composable
private fun GenreRow(
    genres: List<String>
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        genres.forEach { genre ->
            Text(
                text = genre,
                color = TextSecondary,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(end = if (genre != genres.last()) 0.dp else 0.dp)
            )
            // Dấu phân cách trừ phần tử cuối
            if (genre != genres.last()) {
                Text("·", color = TextSecondary, style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}

@Composable
private fun AiringStatusBadge(status: AiringStatus) {
    when (status) {
        is AiringStatus.OnAir -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .background(OrangeAiring.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AccessTime,
                    contentDescription = null,
                    tint = OrangeAiring,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Đang chiếu: ${status.currentEpisode}/${status.totalEpisode} tập",
                    color = OrangeAiring,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        is AiringStatus.Completed -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .background(AppGreen.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = AppGreen,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Đã hoàn thành",
                    color = AppGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        else -> Unit  // MOVIE / Upcoming → không hiện badge
    }
}

@Composable
private fun SynopsisRow(synopsis: String, onDetailClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = synopsis,
            color = TextSecondary,
            style = MaterialTheme.typography.titleSmall,
            lineHeight = 20.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(12.dp))
        // Nút Chi tiết — bên phải
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(onClick = onDetailClick)
                .padding(4.dp)
        ) {
            Text(
                text = "Chi tiết",
                color = TextPrimary,
                style = MaterialTheme.typography.titleSmall
            )
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = null,
                tint = TextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

