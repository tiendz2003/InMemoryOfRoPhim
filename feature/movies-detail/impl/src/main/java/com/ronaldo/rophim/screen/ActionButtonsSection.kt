package com.ronaldo.rophim.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Uninitialized
import com.manutd.ronaldo.designsystem.component.RoButton
import com.manutd.ronaldo.designsystem.component.ShimmerBox
import com.manutd.ronaldo.designsystem.theme.GoldGradient
import com.manutd.ronaldo.designsystem.theme.RoTheme
import com.ronaldo.rophim.R

@Composable
fun ActionButtonsSection(
    state: DetailState,
    onWatchClick: () -> Unit,
    onEpisodesClick: () -> Unit
) {
    if (state.movieDetailAsync is Loading || state.movieDetailAsync is Uninitialized) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ShimmerBox(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            if (state.showEpisodesButton) {
                ShimmerBox(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
        return
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RoButton(
            onClick = onWatchClick,
            icon = painterResource(R.drawable.ic_play),
            modifier = Modifier
                .weight(if (state.showEpisodesButton) 1.15f else 1f)
                .height(48.dp),
            gradient = GoldGradient,
            shape = RoundedCornerShape(10.dp),
            text = "Xem phim"
        )
        if (state.showEpisodesButton) {
            RoButton(
                onClick = onEpisodesClick,
                icon = painterResource(R.drawable.ic_list),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                containerColor = Color.White,
                shape = RoundedCornerShape(10.dp),
                text = "Tập phim"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonAction() {
    RoTheme(
        androidTheme = true
    ) {
        ActionButtonsSection(state = DetailState(), onWatchClick = {}, onEpisodesClick = {})
    }
}
