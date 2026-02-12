package com.manutd.ronaldo.impl.screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsStateWithLifecycle
import com.manutd.ronaldo.designsystem.component.RoTopAppBar
import com.manutd.ronaldo.designsystem.theme.RoTheme
import com.manutd.ronaldo.impl.HomeViewModel
import com.manutd.ronaldo.impl.utils.HomeSectionConfig
import com.manutd.ronaldo.impl.utils.mavericksNav3ViewModel
import com.manutd.ronaldo.network.model.Channel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RoTheme(
        androidTheme = true,
        disableDynamicTheming = true
    ) {
        HomeScreen(
            onChannelClick = {},
            onNotificationClick = {},
            onLogoClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onChannelClick: (String) -> Unit,
    onNotificationClick: () -> Unit,
    onLogoClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = mavericksNav3ViewModel()
) {
    val state by viewModel.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 100
        }
    }
    val topBarAlpha by animateFloatAsState(
        targetValue = if (isScrolled) 0.95f else 0f,
        label = "TopBarAlpha",
        animationSpec = tween(300)
    )
    LaunchedEffect(Unit) {
        viewModel.fetchMovies(forceRefresh = false)
    }

    Box(
        modifier = modifier
            .fillMaxSize()


    ) {
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { viewModel.fetchMovies(forceRefresh = true) },
            modifier = Modifier.fillMaxSize()
        ) {
            val sections = state.sections.invoke()

            if (!sections.isNullOrEmpty()) {
                HomeContent(
                    sections = sections.toImmutableList(),
                    listState = listState,
                    onChannelClick = { channel ->
                        viewModel.selectChannel(channel.id)
                        onChannelClick(channel.id)
                    },
                    contentPadding = PaddingValues(bottom = 40.dp)
                )
            } else if (state.isLoading) {
                LoadingContent()
            } else if (state.hasError) {
                ErrorContent(
                    message = state.errorMessage ?: "Lỗi",
                    onRetry = { viewModel.retry() }
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
        ) {
            RoTopAppBar(
                hasNotification = true,
                showMessageDialog = onLogoClick,
                onNotificationClick = onNotificationClick,
            )
            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = !isScrolled, // Ẩn khi đã scroll
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                CategoryChipsBar()
            }
        }

    }
}

@Composable
private fun HomeContent(
    sections: ImmutableList<HomeSectionConfig>,
    listState: LazyListState,
    onChannelClick: (Channel) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = contentPadding,
        state = listState,
    ) {
        items(
            items = sections,
            key = { it.id },
            contentType = { it::class }
        ) { section ->
            when (section) {
                is HomeSectionConfig.Poster -> {
                    PosterSection(
                        modifier = Modifier,
                        channels = section.channels.toImmutableList(),
                        onChannelClick = onChannelClick,
                        autoScrollDelayMs = section.autoScrollDelayMs
                    )
                }

                is HomeSectionConfig.HorizontalList -> {
                    HorizontalListSection(
                        title = section.title,
                        channels = section.channels.toImmutableList(),
                        onChannelClick = onChannelClick,
                        onSeeAllClick = if (section.showSeeAll) {
                            { /* Navigate to see all */ }
                        } else null,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }

                is HomeSectionConfig.TopRanked -> {
                    Log.d("HomeScreen", "TopRankedSection:${section.channels.size}")
                    TopRankedSection(
                        title = section.title,
                        channels = section.channels.toImmutableList(),
                        onChannelClick = onChannelClick,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }

                is HomeSectionConfig.ActorList -> {
                    ActorSection(
                        title = section.title,
                        actors = section.actors.toImmutableList(),
                        onActorClick = { actor ->
                            Log.d("HomeScreen", "Actor clicked: ${actor.name}")
                        },
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }

            }
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color.White
        )
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error: $message",
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}