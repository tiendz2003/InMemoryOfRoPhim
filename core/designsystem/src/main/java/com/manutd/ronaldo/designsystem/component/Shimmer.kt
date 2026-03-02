package com.manutd.ronaldo.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerBox(modifier: Modifier = Modifier) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    Box(
        modifier = modifier
            .shimmer(shimmer)
            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(4.dp))
    )
}