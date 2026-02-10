package com.manutd.ronaldo.designsystem.component

import android.R.attr.translationY
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manutd.rophim.core.designsystem.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior? = null,
    hasNotification: Boolean = true,
    showMessageDialog: () -> Unit,
    onNotificationClick: () -> Unit,
    bottomContent: @Composable () -> Unit = {}
) {
    // Height of the pinned toolbar (Logo, actions)
    val pinnedHeight = 70.dp
    // Expanded height including background gradient area and categories
    // Matches XML viewTopGradient height (125dp) which roughly covers toolbar + categories
    val expandedHeight = 100.dp

    // Calculate current height based on scroll offset
    // scrollBehavior.state.heightOffset is a negative value starting from 0 to -(expanded - collapsed)
    val heightOffset = scrollBehavior?.state?.heightOffset ?: 0f

    // Current height constrained between collapsed and expanded
    val currentHeight = (expandedHeight + heightOffset.dp).coerceAtLeast(pinnedHeight)

    // Calculate alpha for collapsing content (categories)
    // 1f when expanded, 0f when approaching collapsed state
    val collapseFraction = (currentHeight - pinnedHeight) / (expandedHeight - pinnedHeight)
    val contentAlpha = collapseFraction.coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(currentHeight)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF38003C),
                        Color.Transparent
                    )
                )
            )
            // Ensure status bar padding is applied to the container
            .statusBarsPadding()
            // Clip to bounds to hide scrolling content
            .clip(androidx.compose.ui.graphics.RectangleShape)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(pinnedHeight)
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(40.dp)
                    .clip(CircleShape)
                    .clickable {
                        showMessageDialog()
                    },
                contentScale = ContentScale.Fit
            )


            // --- Right Actions ---
            Row(verticalAlignment = Alignment.CenterVertically) {

                // Nút Notification Container
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .clickable { onNotificationClick() },
                    contentAlignment = Alignment.Center // Căn giữa icon chuông
                ) {
                    // 1. Icon Chuông
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )

                    // 2. Chấm đỏ (Notification Dot)
                    if (hasNotification) {
                        Box(
                            modifier = Modifier
                                // Căn chỉnh vị trí chấm đỏ
                                .align(Alignment.TopEnd) // Đưa về góc phải trên của box 56dp
                                .padding(
                                    top = 14.dp,
                                    end = 14.dp
                                ) // Dịch vào trong một chút cho đẹp
                                .size(10.dp) // Kích thước chấm
                                .background(Color.Red, CircleShape) // Màu đỏ, hình tròn
                                .border(
                                    1.dp,
                                    Color.White,
                                    CircleShape
                                ) // (Tùy chọn) Viền trắng để tách biệt với nền
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(top = 24.dp) // Slight padding from bottom
                .graphicsLayer {
                    alpha = contentAlpha
                    // Optional: Parallax or slide effect
                    translationY = -heightOffset / 2f
                }
        ) {
            bottomContent()
        }
    }
}

private fun Modifier.notificationDot(): Modifier =
    composed {
        val tertiaryColor = MaterialTheme.colorScheme.tertiary
        drawWithContent {
            drawContent()
            drawCircle(
                tertiaryColor,
                radius = 5.dp.toPx(),
                // This is based on the dimensions of the NavigationBar's "indicator pill";
                // however, its parameters are private, so we must depend on them implicitly
                // (NavigationBarTokens.ActiveIndicatorWidth = 64.dp)
                center = center + Offset(
                    64.dp.toPx() * .45f,
                    32.dp.toPx() * -.45f - 6.dp.toPx(),
                ),
            )
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Top App Bar")
@Composable
private fun TopAppBarPreview() {
    MaterialTheme {
        RoTopAppBar(
            hasNotification = true,
            onNotificationClick = {},
            showMessageDialog = {}
        )
    }
}
