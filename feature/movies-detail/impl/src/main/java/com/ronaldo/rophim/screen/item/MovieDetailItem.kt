package com.ronaldo.rophim.screen.item

import android.R.attr.onClick
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manutd.ronaldo.designsystem.component.RoBadgeUiModel
import com.manutd.ronaldo.designsystem.component.RoMediaCard
import com.manutd.ronaldo.designsystem.component.RoThumbnailImage
import com.manutd.ronaldo.designsystem.theme.RoTheme
import com.manutd.ronaldo.designsystem.utils.BadgeType
import com.manutd.ronaldo.designsystem.utils.getBadges
import com.manutd.ronaldo.network.model.CastMember
import com.manutd.ronaldo.network.model.CastRole
import com.manutd.ronaldo.network.model.Episode
import com.manutd.ronaldo.network.model.MovieRecommendation
import com.ronaldo.rophim.screen.AppGreen
import com.ronaldo.rophim.screen.AppYellow
import com.ronaldo.rophim.screen.SurfaceDark
import com.ronaldo.rophim.screen.TextPrimary
import com.ronaldo.rophim.screen.TextSecondary

@Composable
fun RecommendItem(item: MovieRecommendation, modifier: Modifier = Modifier) {
    val badges =
        remember(item.id, item.rating, item.episode, item.quality) {
            getBadges(
                rating = item.rating.toString(),
                episode = item.episode,
                quality = item.quality
            )
        }
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
        title = item.title,
        subtitle = item.title,
        imageUrl = item.posterUrl,
        badges = uiBadges,
        onClick = {

        },
        modifier = modifier
    )
}

@Composable
fun EpisodeItem(
    episode: Episode,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Progress bar phía dưới nếu đang xem dở
        if (episode.watchProgress > 0f && episode.watchProgress < 1f) {
            LinearProgressIndicator(
                progress = { episode.watchProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)),
                color = AppYellow,
                trackColor = Color.Transparent
            )
        }
        Text(
            text = "Tập ${episode.episodeNumber}",
            color = if (episode.isWatched) TextSecondary else TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 8.dp)
        )
        // Checkmark nếu đã xem
        if (episode.isWatched) {
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = null,
                tint = AppGreen.copy(alpha = 0.6f),
                modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp, end = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodeItemPreview() {
    RoTheme {
        CastMemberItem(
            member = CastMember(
                id = "1",
                name = "Ronaldo",
                characterName = "Naruto",
                profileImageUrl = "TODO()",
                role = CastRole.MAIN
            ),
            onClick = {}
        )
    }

}


@Composable
fun CastMemberItem(
    member: CastMember,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically // Căn giữa theo chiều dọc
    ) {
        // 1. Avatar (Sửa fillMaxSize thành size cố định)
        RoThumbnailImage(
            model = member.profileImageUrl,
            contentDescription = member.name,
            modifier = Modifier
                .size(56.dp) // Kích thước chuẩn cho Avatar
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.3f))
        )

        Spacer(modifier = Modifier.width(14.dp))

        // 2. Cột Tên & Giới tính (Nằm ở Start)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = member.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = member.role.label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 3. Nút Thông tin (Nằm ở End)
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(16.dp), // Bo góc 16.dp theo yêu cầu
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant), // Viền màu chính
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
            modifier = Modifier.height(32.dp) // Nút nhỏ nhắn, gọn gàng
        ) {
            Text(
                text = "Thông tin",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}