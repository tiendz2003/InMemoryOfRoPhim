package com.ronaldo.rophim.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.manutd.ronaldo.designsystem.component.ShimmerBox

@Composable
private fun RecommendShimmer() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        repeat(2) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(2) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ShimmerBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        ShimmerBox(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(13.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CastShimmer() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        repeat(5) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ShimmerBox(modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    ShimmerBox(modifier = Modifier
                        .width(120.dp)
                        .height(14.dp))
                    ShimmerBox(modifier = Modifier
                        .width(80.dp)
                        .height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun MovieInfoShimmer() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ShimmerBox(modifier = Modifier
            .fillMaxWidth(0.75f)
            .height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(4) {
                ShimmerBox(
                    modifier = Modifier
                        .width(52.dp)
                        .height(22.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(3) { ShimmerBox(modifier = Modifier
                .width(60.dp)
                .height(16.dp)) }
        }
        ShimmerBox(modifier = Modifier
            .fillMaxWidth()
            .height(60.dp))
    }
}
