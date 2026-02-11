package com.manutd.ronaldo.impl.screen


import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
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
        initialPage = channels.size / 2,
        pageCount = { channels.size }
    )
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = 260.dp

    // Tính toán chiều cao cho Pager:
    // Card Width = 260.dp -> Card Height (tỉ lệ 2:3) = 390.dp
    // Padding trong Item = 24.dp * 2 (trên dưới) = 48.dp
    // Padding top của Pager = 120.dp
    // => Tổng chiều cao an toàn khoảng 560.dp -> 580.dp
    val pagerHeight = 580.dp

    val horizontalPadding = (screenWidth - cardWidth) / 2

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
    ) {
        val currentChannel = channels.getOrNull(pagerState.currentPage)

        // Blur Background
        BlurBackground(
            imageUrl = currentChannel?.logoUrl,
            modifier = Modifier.matchParentSize()
        )
        //poster và thông tin phim
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            // HorizontalPager
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(
                    start = horizontalPadding,
                    end = horizontalPadding,
                    top = 120.dp // Giữ padding top để tránh TopBar
                ),
                pageSize = PageSize.Fixed(cardWidth),
                modifier = Modifier.fillMaxWidth().height(pagerHeight),
                beyondViewportPageCount = 2
            ) { page ->
                val channel = channels[page]
                CarouselItem(
                    channel = channel,
                    pagerState = pagerState,
                    page = page,
                    onClick = { onChannelClick(channel) }
                )
            }

            // Info Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
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
                    type = ShiftIndicatorType(
                        dotsGraphic = DotGraphic(
                            color = Color.White,
                            size = 8.dp,
                            borderWidth = 1.dp,
                            borderColor = Color.White.copy(0.5f)
                        )
                    ),
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
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = BorderStroke(2.dp, Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .aspectRatio(2f / 3f)
            .zIndex(if (pagerState.currentPage == page) 1f else 0f)
            .graphicsLayer {
                val pageOffset =
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                val absOffset = pageOffset.absoluteValue

                val scale = lerp(0.85f, 1f, 1f - absOffset.coerceIn(0f, 1f))
                val alphaVal = lerp(0.6f, 1f, 1f - absOffset.coerceIn(0f, 1f))


                translationX = pageOffset

                // Rotation logic
                val rotationFactor = 20f
                rotationY = -pageOffset.coerceIn(-1f, 1f) * rotationFactor

                scaleX = scale
                scaleY = scale
                alpha = alphaVal

                cameraDistance = 12 * density
                transformOrigin = TransformOrigin.Center
            }
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = channel.logoUrl,
            contentDescription = channel.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
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
                .customBlur(20.dp)
                .drawWithCache {
                    val gradient = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f),
                            Color(0xFF38003C).copy(alpha = 0.7f),
                            Color(0xFF38003C)
                        ),
                        startY = 0f,
                        endY = size.height
                    )

                    onDrawWithContent {
                        drawContent()
                        // Vẽ gradient overlay từ dưới lên
                        drawRect(brush = gradient)
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
            text = channel.display,
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

@Preview
@Composable
fun TagPreview() {
    RoTheme {
        CategoryChipsBar()
    }
}

@Composable
fun CategoryChipsBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val categories = listOf("Đề xuất", "Phim bộ", "Phim lẻ", "Thể loại")

        categories.forEachIndexed { index, category ->
            CategoryChip(
                text = category,
                isSelected = index == 0 // First item selected by default
            )
        }
    }
}

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    // Animation màu nền
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Transparent,
        animationSpec = tween(durationMillis = 200),
        label = "chipBackground"
    )

    // Animation màu chữ
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.Black else Color.White,
        animationSpec = tween(durationMillis = 200),
        label = "chipText"
    )

    // Định nghĩa hình dáng bo góc dùng chung cho clip và border
    val shape = RoundedCornerShape(24.dp)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            // 1. Cắt bo góc (Quan trọng: Phải đặt trước background và border)
            .clip(shape)
            // 2. Vẽ viền nếu không được chọn
            .then(
                if (!isSelected) {
                    Modifier.border(1.dp, Color.White.copy(0.4f), shape)
                } else Modifier
            )
            // 3. Tô màu nền
            .background(backgroundColor)
            // 4. Bắt sự kiện click (Đặt sau clip để hiệu ứng ripple gói gọn trong hình bo góc)
            .clickable { /* Handle click */ }
            // 5. Padding nội dung (Nên đặt ở Box thay vì Text để vùng click rộng hơn)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}