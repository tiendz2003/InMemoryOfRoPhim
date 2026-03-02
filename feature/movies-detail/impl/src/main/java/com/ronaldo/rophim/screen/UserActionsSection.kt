package com.ronaldo.rophim.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material.icons.rounded.SentimentSatisfiedAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserActionsSection(
    state: DetailState,
    onFavoriteClick: () -> Unit
) {
    val detail = state.movieDetail
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionIconButton(
            icon = if (detail?.isFavorited == true) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
            label = "Yêu thích",
            tint = if (detail?.isFavorited == true) Color.Red else TextPrimary,
            isLoading = state.isFavoriteLoading,
            onClick = onFavoriteClick
        )
        // Thêm vào
        ActionIconButton(
            icon = Icons.Rounded.Add,
            label = "Thêm vào",
            onClick = { /* TODO */ }
        )
        // Đánh giá
        ActionIconButton(
            icon = Icons.Rounded.SentimentSatisfiedAlt,
            label = "Đánh giá",
            onClick = { /* TODO */ },
            badge = detail?.userRating?.let { "%.1f".format(it) }
        )
        // Bình luận
        ActionIconButton(
            icon = Icons.Rounded.ChatBubbleOutline,
            label = "Bình luận",
            onClick = { /* TODO */ }
        )
        // Chia sẻ
        ActionIconButton(
            icon = Icons.AutoMirrored.Rounded.Send,
            label = "Chia sẻ",
            onClick = { /* TODO */ }
        )

    }
    HorizontalDivider(
        modifier = Modifier.padding(top = 16.dp),
        color = Color.White.copy(alpha = 0.08f)
    )
}

@Composable
private fun ActionIconButton(
    icon: ImageVector,
    label: String,
    tint: Color = TextPrimary,
    isLoading: Boolean = false,
    badge: String? = null,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clickable(enabled = !isLoading, onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(26.dp),
                    color = tint,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier.size(26.dp)
                )
            }
            // Badge (ví dụ điểm đánh giá)
            badge?.let {
                Box(
                    modifier = Modifier
                        .offset(x = 8.dp, y = (-4).dp)
                        .background(AppYellow, CircleShape)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(it, color = Color.Black, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Text(label, color = TextSecondary, fontSize = 12.sp)
    }
}