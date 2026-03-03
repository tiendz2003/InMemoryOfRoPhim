package com.manutd.ronaldo.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manutd.ronaldo.designsystem.utils.RoItemConstants


@Composable
fun RoMediaCard(
    title: String,
    subtitle: String,
    imageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    // Thay vì nhận List<MovieBadge>, ta nhận List<Pair<String, Color>> hoặc một class UI Model đơn giản
    badges: List<RoBadgeUiModel> = emptyList(),
) {
    Column(
        modifier = modifier
    ) {
        val shape = RoundedCornerShape(RoItemConstants.HorizontalCornerRadius)
        Card(
            shape = shape,
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(RoItemConstants.HorizontalItemHeight)
                .clip(shape)
                .clickable(onClick = onClick)
        ) {
            Box {
                RoThumbnailImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize()
                )

                if (badges.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        badges.forEach { badge ->
                            RoBadgeItem(text = badge.text, backgroundColor = badge.color)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

data class RoBadgeUiModel(val text: String, val color: Color)

@Composable
private fun RoBadgeItem(text: String, backgroundColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(RoItemConstants.BadgeCornerRadius)
            )
            .padding(horizontal = 6.dp, vertical = 3.dp)
    ) {
        Text(text = text, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}