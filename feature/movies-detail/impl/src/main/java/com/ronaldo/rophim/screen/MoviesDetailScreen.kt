package com.ronaldo.rophim.screen

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.compose.collectAsStateWithLifecycle
import com.manutd.ronaldo.designsystem.component.ShimmerBox
import com.manutd.ronaldo.designsystem.theme.RoTheme
import com.manutd.ronaldo.network.model.Episode
import com.manutd.rophim.core.data.utils.FakeDataProvider
import com.manutd.rophim.mavericksViewModel

val AppYellow = Color(0xFFF5C518)
val AppGreen = Color(0xFF00C853)
val DarkBg = Color(0xFF0E0E0E)
val SurfaceDark = Color(0xFF1A1A1A)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFAAAAAA)
val OrangeAiring = Color(0xFFFF8C00)


@Composable
fun MoviesDetailScreen(
    onNavigateToWatch: (movieId: String?, episode: Episode?) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: MoviesDetailViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val isTrailerVisible by remember() {
        derivedStateOf {
            val firstVisible = listState.firstVisibleItemIndex
            val offset = listState.firstVisibleItemScrollOffset
            // Item 0 còn trong viewport
            if (firstVisible > 0) return@derivedStateOf false
            // Chưa scroll quá ngưỡng — dùng layoutInfo để lấy height thực
            val itemHeight = listState.layoutInfo.visibleItemsInfo
                .firstOrNull { it.index == 0 }?.size ?: Int.MAX_VALUE
            // Pause khi đã scroll qua 40% chiều cao item
            offset < (itemHeight * 0.4f).toInt()
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            // Dùng WindowInsets để tự động cách Navigation Bar (tránh bị phím ảo che)
            contentPadding = PaddingValues(
                bottom = WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding() + 32.dp
            )
        ) {
            item(key = "trailer") {
                TrailerPlayerSection(
                    state = state,
                    isTrailerVisible = isTrailerVisible,
                    player = viewModel.player,
                    onLoadTrailer = viewModel::loadTrailer,
                    onVisibilityChanged = viewModel::onTrailerVisibilityChanged,
                    onRetry = viewModel::retryTrailer,
                )
            }
            item(key = "action_buttons") {
                ActionButtonsSection(
                    state = state,
                    onWatchClick = {
                        onNavigateToWatch(
                            state.movieDetail?.id,
                            FakeDataProvider.seriesOnAir.seasons
                                .flatMap { it.episodes }.first()
                        )
                    },
                    onEpisodesClick = {
                        // Scroll đến tab tập phim
                        val episodesTabIndex = state.availableTabs
                            .indexOfFirst { it is DetailTab.Episodes }
                        if (episodesTabIndex >= 0) viewModel.onTabSelected(episodesTabIndex)
                    }
                )
            }
            // Movie Info
            item(key = "movie_info") {
                MovieInfoSection(
                    state = state,
                    onSynopsisDetailClick = viewModel::toggleSynopsisSheet
                )
            }

            // Item 3: User Actions (Yêu thích, Thêm vào...)
            item(key = "user_actions") {
                UserActionsSection(
                    state = state,
                    onFavoriteClick = viewModel::toggleFavorite
                )
            }

            // Item 4: Tab Layout + Content
            item(key = "tabs") {
                TabSection(
                    state = state,
                    onTabSelected = viewModel::onTabSelected,
                    onSeasonSelected = viewModel::onSeasonSelected,
                    onAudioTrackSelected = viewModel::onAudioTrackSelected,
                    onEpisodeClick = { episodeId ->
                        //todo:truyefn thêm episode vào callback
                        onNavigateToWatch(
                            episodeId, FakeDataProvider.seriesOnAir.seasons
                                .flatMap { it.episodes }.first()
                        )
                    }
                )
            }
        }
    }

}

@Composable
private fun TrailerPlayerSection(
    state: DetailState,
    isTrailerVisible: Boolean,
    player: ExoPlayer,
    onLoadTrailer: (String) -> Unit,
    onVisibilityChanged: (Boolean) -> Unit,
    onRetry: () -> Unit,
) {
    // Khi detail load xong → tự động load trailer
    val trailerUrl = state.movieDetail?.trailerUrl
    val thumbnailUrl = state.movieDetail?.posterUrl ?: ""

    LaunchedEffect(trailerUrl) {
        trailerUrl?.let { onLoadTrailer(it) }
    }

    // Shimmer khi detail chưa load
    if (state.movieDetailAsync is Loading || state.movieDetailAsync is Uninitialized) {
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        )
        return
    }

    TrailerPlayer(
        trailerUrl = trailerUrl ?: "",
        thumbnailUrl = thumbnailUrl,
        isVisible = isTrailerVisible,
        uiState = state.trailerUiState,
        player = player,
        onLoadTrailer = onLoadTrailer,
        onVisibilityChanged = onVisibilityChanged,
        onRetry = onRetry,
    )
}

@Composable
fun TrailerPlayer(
    trailerUrl: String,
    thumbnailUrl: String,
    isVisible: Boolean,
    uiState: TrailerUiState,
    player: ExoPlayer,
    modifier: Modifier = Modifier,
    onLoadTrailer: (String) -> Unit,
    onVisibilityChanged: (Boolean) -> Unit,
    onRetry: () -> Unit,
) {
    LaunchedEffect(trailerUrl) {
        onLoadTrailer(trailerUrl)
    }

    LaunchedEffect(isVisible) {
        onVisibilityChanged(isVisible)
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> onVisibilityChanged(false)
                Lifecycle.Event.ON_RESUME -> onVisibilityChanged(true)
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(Color.Black)
    ) {
        PlayerViewSection(player = player)

        ThumbnailOverlay(thumbnailUrl = thumbnailUrl, uiState = uiState)

        AnimatedVisibility(
            visible = uiState is TrailerUiState.Buffering,
            enter = fadeIn(tween(200)), exit = fadeOut(tween(200)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(36.dp))
        }

        AnimatedVisibility(
            visible = uiState is TrailerUiState.Error,
            enter = fadeIn(tween(200)), exit = fadeOut(tween(200)),
            modifier = Modifier.fillMaxSize()
        ) {
            val errorMessage = (uiState as? TrailerUiState.Error)?.message ?: "Đã có lỗi xảy ra"
            ErrorOverlay(message = errorMessage, onRetry = onRetry)
        }

    }
}

@OptIn(UnstableApi::class)
@Composable
private fun PlayerViewSection(player: ExoPlayer) {
    //đảm bảo androidview chỉ tạo lại khi player instance thay đổi
    key(player) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    this.player = player
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    setBackgroundColor(android.graphics.Color.BLACK)
                    setShutterBackgroundColor(android.graphics.Color.BLACK)
                }
            },
            // update block: sync lại player nếu cần, không recreate View
            update = { playerView ->
                if (playerView.player != player) {
                    playerView.player = player
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ThumbnailOverlay(
    thumbnailUrl: String,
    uiState: TrailerUiState
) {
    val showThumbnail = uiState !is TrailerUiState.Playing

    AnimatedVisibility(
        visible = showThumbnail,
        enter = fadeIn(tween(150)),
        exit = fadeOut(tween(300)),  // Fade out chậm hơn để không flash
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = thumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ErrorOverlay(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Rounded.WifiOff, contentDescription = null,
                tint = Color.White, modifier = Modifier.size(32.dp)
            )
            Text(
                message, color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(onClick = onRetry) {
                Text(
                    "Thử lại", color = Color.White,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

