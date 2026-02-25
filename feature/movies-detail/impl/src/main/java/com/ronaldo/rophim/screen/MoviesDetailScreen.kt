package com.ronaldo.rophim.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import com.airbnb.mvrx.compose.collectAsStateWithLifecycle
import com.manutd.rophim.mavericksViewModel

data class Movie(
    val title: String,
    val posterUrl: String,
    val trailerUrl: String
)

@Composable
fun MoviesDetailScreen(
    movie: Movie,
    onNavigateToWatch: () -> Unit,
    detailViewModel: MoviesDetailViewModel = mavericksViewModel()
) {
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
    LazyColumn(state = listState) {
        item {
            TrailerPlayer(
                trailerUrl = movie.trailerUrl,
                thumbnailUrl = movie.posterUrl,
                isVisible = isTrailerVisible,
                onWatchClick = onNavigateToWatch,
                detailViewModel = detailViewModel
            )
        }
    }
}

@Composable
fun TrailerPlayer(
    trailerUrl: String,
    thumbnailUrl: String,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    onWatchClick: () -> Unit = {},
    detailViewModel: MoviesDetailViewModel
) {
    val state by detailViewModel.collectAsStateWithLifecycle()
    //Load trailer 1 lần chỉ khi nó thay đổi
    LaunchedEffect(trailerUrl) {
        detailViewModel.loadTrailer(trailerUrl)
    }
    LaunchedEffect(isVisible) {
        detailViewModel.onVisibilityChanged(isVisible)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        //đăng ký lifecycle để theo dõi trạng thái
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> detailViewModel.onVisibilityChanged(false)
                Lifecycle.Event.ON_RESUME -> detailViewModel.onVisibilityChanged(true)
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
        PlayerViewSection(player = detailViewModel.player)

        // Layer 2: Thumbnail overlay với fade animation
        ThumbnailOverlay(
            thumbnailUrl = thumbnailUrl,
            uiState = state.uiState
        )

        // Layer 3: Spinner — chỉ visible khi Buffering
        AnimatedVisibility(
            visible = state.uiState is TrailerUiState.Buffering,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(36.dp),
                color = Color.White,
                strokeWidth = 2.dp
            )
        }

        // Layer 4: Error overlay
        AnimatedVisibility(
            visible = state.uiState is TrailerUiState.Error,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200)),
            modifier = Modifier.fillMaxSize()
        ) {
            val errorMessage = (state.uiState as? TrailerUiState.Error)?.message ?: ""
            ErrorOverlay(
                message = errorMessage,
                onRetry = detailViewModel::retry
            )
        }
    }
}

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