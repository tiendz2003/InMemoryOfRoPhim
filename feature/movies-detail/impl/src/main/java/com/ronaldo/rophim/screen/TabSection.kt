package com.ronaldo.rophim.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manutd.ronaldo.designsystem.theme.RoTheme
import com.manutd.ronaldo.network.model.AudioTrack
import com.manutd.ronaldo.network.model.Season
import com.ronaldo.rophim.screen.item.EpisodeItem
import kotlin.collections.first
import kotlin.collections.forEachIndexed

@Preview(showBackground = true)
@Composable
fun TabSectionPreview() {
    RoTheme() {
        TabSection(
            state = DetailState(),
            onTabSelected = {},
            onSeasonSelected = {},
            onAudioTrackSelected = {

            },
            onEpisodeClick = {}
        )
    }
}


@Composable
fun TabSection(
    state: DetailState,
    onTabSelected: (Int) -> Unit,
    onSeasonSelected: (Int) -> Unit,
    onAudioTrackSelected: (String) -> Unit,
    onEpisodeClick: (String) -> Unit
) {
    val tabs = state.availableTabs
    if (tabs.isEmpty()) return

    Column {
        HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
        SecondaryTabRow(
            modifier = Modifier
                .wrapContentWidth()
            ,
            selectedTabIndex = state.selectedTabIndex,
            containerColor = Color.Transparent,
            contentColor = AppYellow,
            // Sử dụng TabIndicatorScope mới, code ngắn gọn và dễ hiểu hơn
            indicator = {
                Box(modifier = Modifier.fillMaxSize()) {

                    // Vẽ thanh Divider xám nhạt đè sát mép TRÊN CÙNG (TopCenter)
                    HorizontalDivider(
                        modifier = Modifier.align(Alignment.TopCenter),
                        thickness = 1.dp,
                        color = Color.White.copy(alpha = 0.08f)
                    )

                    // Vẽ thanh Indicator màu vàng trượt qua lại
                    Box(
                        modifier = Modifier
                            // Hàm này (thuộc TabIndicatorScope) sẽ tính toán X và Chiều rộng của tab
                            .tabIndicatorOffset(state.selectedTabIndex)
                            // Ép Box này cao bằng TabRow để đẩy thanh vàng lên trên cùng
                            .fillMaxHeight()
                    ) {
                        // Đây chính là vạch màu vàng
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(AppYellow)
                                .align(Alignment.TopCenter) // Đẩy sát mép TRÊN CÙNG
                        )
                    }
                }
            },
            divider = {
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = state.selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = {
                        Text(
                            text = tab.title,
                            fontWeight = if (state.selectedTabIndex == index)
                                FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    },
                    selectedContentColor = AppYellow,
                    unselectedContentColor = TextSecondary
                )
            }
        }

        // ── Tab Content ────────────────────────────────────────
        val currentTab = tabs.getOrNull(state.selectedTabIndex)
        // key() đảm bảo recompose sạch khi đổi tab
        key(currentTab) {
            when (currentTab) {
                is DetailTab.Episodes -> EpisodesTabContent(
                    state = state,
                    onSeasonSelected = onSeasonSelected,
                    onAudioTrackSelected = onAudioTrackSelected,
                    onEpisodeClick = onEpisodeClick
                )

                is DetailTab.Cast -> {
                    //CastTabContent(castAsync = state.castAsync)
                }

                is DetailTab.Recommendations -> {
                    //RecommendTabContent(recommendAsync = state.recommendAsync)
                }

                null -> Unit
            }
        }
    }
}

@Composable
private fun EpisodesTabContent(
    state: DetailState,
    onSeasonSelected: (Int) -> Unit,
    onAudioTrackSelected: (String) -> Unit,
    onEpisodeClick: (String) -> Unit
) {
    val detail = state.movieDetail ?: return
    val currentSeason = state.currentSeason ?: return

    Column(modifier = Modifier.padding(top = 12.dp)) {

        // ── Header: Phần X dropdown + Audio dropdown ──────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Season dropdown
            if ((detail.totalSeasons ?: 1) > 1) {
                SeasonDropdown(
                    seasons = detail.seasons,
                    selectedIndex = state.selectedSeasonIndex,
                    onSeasonSelected = onSeasonSelected
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.List, contentDescription = null,
                        tint = TextSecondary, modifier = Modifier.size(16.dp)
                    )
                    Text(
                        currentSeason.title, color = TextPrimary,
                        fontSize = 14.sp, fontWeight = FontWeight.Medium
                    )
                }
            }

            // Audio track dropdown
            if (currentSeason.audioTracks.size > 1) {
                AudioTrackDropdown(
                    tracks = currentSeason.audioTracks,
                    selectedId = state.selectedAudioTrackId ?: currentSeason.defaultAudioTrackId,
                    onTrackSelected = onAudioTrackSelected
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // ── Episode Grid: 3 cột ───────────────────────────────
        val episodes = currentSeason.episodes
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Khoảng cách giữa các cột
            verticalArrangement = Arrangement.spacedBy(8.dp),   // Khoảng cách giữa các hàng
            maxItemsInEachRow = 3 // (Tùy chọn) Khóa tối đa 3 cột trên điện thoại
        ) {
            episodes.forEach { episode ->
                EpisodeItem(
                    episode = episode,
                    // Sức mạnh mới của FlowRow: Modifier.weight(1f) giúp chia đều không gian
                    modifier = Modifier.weight(1f),
                    onClick = { onEpisodeClick(episode.id) }
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeasonDropdown(
    seasons: List<Season>,
    selectedIndex: Int,
    onSeasonSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        Row(
            modifier = Modifier
                .menuAnchor()
                .background(SurfaceDark, RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(seasons[selectedIndex].title, color = TextPrimary, fontSize = 13.sp)
            Icon(
                Icons.Rounded.ArrowDropDown,
                null,
                tint = TextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = SurfaceDark
        ) {
            seasons.forEachIndexed { index, season ->
                DropdownMenuItem(
                    text = { Text(season.title, color = TextPrimary, fontSize = 13.sp) },
                    onClick = { onSeasonSelected(index); expanded = false },
                    colors = MenuDefaults.itemColors(textColor = TextPrimary)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AudioTrackDropdown(
    tracks: List<AudioTrack>,
    selectedId: String,
    onTrackSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedTrack = tracks.firstOrNull { it.id == selectedId } ?: tracks.first()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        Row(
            modifier = Modifier
                .menuAnchor()
                .background(SurfaceDark, RoundedCornerShape(6.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Rounded.VolumeUp,
                null,
                tint = TextSecondary,
                modifier = Modifier.size(14.dp)
            )
            Text(selectedTrack.label, color = TextPrimary, fontSize = 13.sp)
            Icon(
                Icons.Rounded.ArrowDropDown,
                null,
                tint = TextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = SurfaceDark
        ) {
            tracks.forEach { track ->
                DropdownMenuItem(
                    text = {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(track.label, color = TextPrimary, fontSize = 13.sp)
                            if (track.id == selectedId) {
                                Icon(
                                    Icons.Rounded.Check, null,
                                    tint = AppYellow, modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    },
                    onClick = { onTrackSelected(track.id); expanded = false }
                )
            }
        }
    }
}