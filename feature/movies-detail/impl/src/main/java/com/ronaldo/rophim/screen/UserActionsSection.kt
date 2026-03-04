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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manutd.ronaldo.designsystem.theme.RoTheme
import com.manutd.ronaldo.designsystem.theme.Yellow
import com.manutd.ronaldo.designsystem.theme.YellowLight
import com.ronaldo.rophim.R

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
            icon = painterResource(com.manutd.rophim.core.designsystem.R.drawable.ic_like),
            label = "Yêu thích",
            tint = if (detail?.isFavorited == true) Color.Red else TextPrimary,
            isLoading = state.isFavoriteLoading,
            onClick = onFavoriteClick
        )
        // Thêm vào
        ActionIconButton(
            icon = painterResource(R.drawable.ic_add),
            label = "Thêm vào",
            onClick = { /* TODO */ }
        )
        // Đánh giá
        ActionIconButton(
            icon = painterResource(R.drawable.ic_evaluate),
            label = "Đánh giá",
            onClick = { /* TODO */ },
            badge = detail?.userRating?.let { "%.1f".format(it) }
        )
        // Bình luận
        ActionIconButton(
            icon = painterResource(R.drawable.ic_comment),
            label = "Bình luận",

            onClick = { /* TODO */ }
        )
        // Chia sẻ
        ActionIconButton(
            icon = painterResource(R.drawable.ic_share),
            label = "Chia sẻ",
            onClick = { /* TODO */ }
        )

    }

}

@Composable
private fun ActionIconButton(
    icon: Painter,
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
                    modifier = Modifier.size(24.dp),
                    color = tint,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    painter = icon,
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier.size(24.dp).align(Alignment.Center)
                )
            }
            // Badge (ví dụ điểm đánh giá)
            badge?.let {
                Box(
                    modifier = Modifier
                        .offset(x = 16.dp, y = (-8).dp)
                        .background(
                            color = Yellow,
                            shape = CircleShape
                        )
                        .padding(horizontal = 8.dp)

                ) {
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        Text(label, fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserActions() {
    RoTheme() {
        ActionIconButton(
            icon = painterResource(R.drawable.ic_share),
            label = "Yêu thích",
            badge = "1.5",
            tint = Color.Red
        ) { }
    }
}