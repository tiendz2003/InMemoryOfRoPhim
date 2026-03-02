package com.ronaldo.rophim.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized

@Composable
fun MovieInfoSection(
    state: DetailState,
    onSynopsisDetailClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        when (val async = state.movieDetailAsync) {
            is Loading, is Uninitialized -> MovieInfoShimmer()
            is Fail -> ErrorInline(
                message = "Không tải được thông tin phim",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )

            is Success -> {
                val detail = async()

                //Tiêu đề phim
                Text(
                    text = detail.title,
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )

                //Metadata
                MetadataTagRow(badges = state.metadataBadges)

                // ── Thể loại
                if (detail.genres.isNotEmpty()) {
                    GenreRow(genres = detail.genres.map { it.name })
                }

                // ── Badge trạng thái chiếu (Series only) ──────
                AiringStatusBadge(status = detail.airingStatus)

                // ── Synopsis + Chi tiết ────────────────────────
                SynopsisRow(
                    synopsis = detail.synopsis,
                    onDetailClick = onSynopsisDetailClick
                )
            }
        }
    }
}