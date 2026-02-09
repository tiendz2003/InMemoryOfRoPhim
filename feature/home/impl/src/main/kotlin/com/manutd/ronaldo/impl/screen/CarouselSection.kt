package com.manutd.ronaldo.impl.screen



import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.manutd.ronaldo.designsystem.component.RoButton
import com.manutd.ronaldo.designsystem.theme.GoldGradient
import com.manutd.ronaldo.designsystem.theme.RoTheme
import com.manutd.ronaldo.network.model.Channel
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import customBlur
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Preview(showBackground = true)
@Composable
fun HeaderPreview() {
    RoTheme(
        androidTheme = true,
        disableDynamicTheming = true
    ) {
        CarouselSection(
            channels = listOf(
                Channel(
                    id = "1",
                    name = "Channel 1",
                    display = "Display 1",
                    description = "Description 1",
                    logoUrl = "https://example.com/logo1.png",
                    streamUrl = "https://example.com/stream1.m3u8",
                    shareUrl = "https://example.com",
                    imdb = "7.0",
                    quality = "HD",
                    rating = "3.5",
                    year = "2025",
                    episode = "34"
                )
            ),
            onChannelClick = {}
        )
    }
}

@Composable
fun CarouselSection(
    channels: List<Channel>,
    onChannelClick: (Channel) -> Unit,
    modifier: Modifier = Modifier,
    autoScrollEnabled: Boolean = true,
    autoScrollDelayMs: Long = 3000L
) {
    if (channels.isEmpty()) return
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { channels.size }
    )
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    LaunchedEffect(autoScrollEnabled, channels.size) {
        if (autoScrollEnabled && channels.size > 1) {
            while (true) {
                delay(autoScrollDelayMs)
                if (!isDragged) {
                    val nextPage = (pagerState.currentPage + 1) % channels.size
                    pagerState.animateScrollToPage(
                        page = nextPage,
                        animationSpec = tween(durationMillis = 800)
                    )
                }
            }
        }
    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(650.dp)
            .background(
                MaterialTheme.colorScheme.background
            )
    ) {
        val currentChannel = channels.getOrNull(pagerState.currentPage)
        //Hiển thị blurBackground
        BlurBackground(
            imageUrl = currentChannel?.logoUrl,
            modifier = Modifier.fillMaxSize()
        )
        Column(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                pageSpacing = 16.dp,
                contentPadding = PaddingValues(horizontal = 64.dp),
                pageSize = PageSize.Fill,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 40.dp, bottom = 20.dp)
            ) { page ->
                val channel = channels[page]

                CarouselItem(
                    channel = channel,
                    pagerState = pagerState,
                    page = page,
                    onClick = { onChannelClick(channel) }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        // Gradient đen mờ dần từ dưới lên để text dễ đọc hơn
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(0.9f)),
                            startY = 0f,
                            endY = 500f // Điều chỉnh độ cao gradient
                        )
                    )
                    .padding(top = 16.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MovieInfoSection(
                    channel = currentChannel,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
                DotsIndicator(
                    modifier = Modifier.height(10.dp),
                    dotCount = pagerState.pageCount,
                    type = ShiftIndicatorType(dotsGraphic = DotGraphic(color = Color.White)),
                    pagerState = pagerState,
                )
            }
        }
    }
}

@Composable
private fun CarouselItem(
    channel: Channel,
    pagerState: PagerState,
    page: Int,
    onClick: () -> Unit
) {
    val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
    val absOffset = pageOffset.absoluteValue.coerceIn(0f, 1f)

    val scale = lerp(0.85f, 1f, 1f - absOffset)
    val alpha = lerp(0.5f, 1f, 1f - absOffset)

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (absOffset < 0.1f) 12.dp else 4.dp
        ),
        border = BorderStroke(1.dp, Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3f / 4f)

            .zIndex(-absOffset)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = channel.logoUrl,
            contentDescription = channel.name,
            // 2. Đổi sang Crop để ảnh tràn viền 16:9 đẹp hơn
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            onError = { e ->
                Log.e("CarouselItem", "Error: ${e.result.throwable.message}")
            },
            onSuccess = {
                Log.d("CarouselItem", "Success")
            }
        )
    }
}

@Composable
private fun BlurBackground(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    if (imageUrl == null) return

    Box(modifier = modifier) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .customBlur(20.dp) // Gọi hàm tiện ích blur
                .drawWithCache() {
                    // Phủ thêm 1 lớp đen mờ 40% lên toàn bộ ảnh nền để làm dịu mắt
                    onDrawWithContent {
                        drawContent()
                        drawRect(Color.Black.copy(alpha = 0.4f))
                    }
                }
        )
    }
}

@Composable
private fun MovieInfoSection(
    channel: Channel?,
    modifier: Modifier = Modifier
) {
    if (channel == null) return

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = channel.name,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            maxLines = 1,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // English title (placeholder)
        Text(
            text = "English Title",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray,
            maxLines = 1,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            // Watch button
            RoButton(
                text = "Xem",
                onClick = { /* Navigate to watch */ },
                modifier = Modifier.weight(1f),
                icon = Icons.Default.PlayArrow,
                gradient = GoldGradient,
            )

            // Info button
            RoButton(
                text = "Chi tiết",
                onClick = { /* Navigate to detail */ },
                modifier = Modifier.weight(1f),
                containerColor = Color.White,
                icon = Icons.Default.Info,
            )
        }

        // Tags (IMDB, Quality, etc.)
        MovieTags(
            channel = channel,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Description
        Text(
            text = channel.description,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            maxLines = 2,
            modifier = Modifier.padding(bottom = 12.dp)
        )
    }
}



@Composable
private fun MovieTags(
    channel: Channel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        channel.imdb?.let { Tag(text = it, type = TagType.IMDB) }
        channel.quality?.let { Tag(text = it, type = TagType.QUALITY) }
        channel.rating?.let { Tag(text = it, type = TagType.SOLID) }
        channel.year?.let { Tag(text = it, type = TagType.OUTLINE) }
        channel.episode?.let { Tag(text = it, type = TagType.OUTLINE) }
    }
}

enum class TagType { IMDB, QUALITY, SOLID, OUTLINE }

@Composable
private fun Tag(
    text: String,
    type: TagType,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (type) {
        TagType.IMDB -> Color(0xFFF5C518)
        TagType.QUALITY -> Color(0xFF00C853)
        TagType.SOLID -> Color.DarkGray
        TagType.OUTLINE -> Color.Transparent
    }

    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = if (type == TagType.OUTLINE) Color.White else Color.Black
        )
    }
}

@Composable
private fun CarouselIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(
                        width = if (index == currentPage) 32.dp else 8.dp,
                        height = 8.dp
                    )
                    .background(
                        color = if (index == currentPage) Color.White else Color.LightGray,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}