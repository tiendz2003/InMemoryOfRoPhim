package com.manutd.ronaldo.impl.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.compose.collectAsStateWithLifecycle
import com.airbnb.mvrx.compose.mavericksViewModel
import com.manutd.ronaldo.api.HomeNavKey
import com.manutd.ronaldo.designsystem.component.RoTopAppBar
import com.manutd.ronaldo.designsystem.theme.RoTheme
import com.manutd.ronaldo.impl.HomeViewModel
import com.manutd.ronaldo.impl.utils.HomeSection
import com.manutd.ronaldo.impl.utils.maverickViewModel.mavericksNav3ViewModel
import com.manutd.ronaldo.navigation.Navigator
import com.manutd.ronaldo.network.model.Channel

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

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = rememberTopAppBarState(),
        canScroll = { true }
    )

    LaunchedEffect(Unit) {
        viewModel.fetchMovies(forceRefresh = false)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)

    ) {

        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { viewModel.fetchMovies(forceRefresh = true) },
            modifier = Modifier.fillMaxSize()
        ) {
            val sections = state.sections.invoke()

            if (!sections.isNullOrEmpty()) {
                HomeContent(
                    sections = sections,
                    onChannelClick = { channel ->
                        viewModel.selectChannel(channel.id)
                        onChannelClick(channel.id)
                    },
                    contentPadding = PaddingValues(bottom = 100.dp)
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
        RoTopAppBar(
            scrollBehavior = scrollBehavior,
            hasNotification = true,
            showMessageDialog = onLogoClick,
            onNotificationClick = onNotificationClick,
            bottomContent = {
                CategoryChipsBar()
            }
        )
    }
}

@Composable
private fun HomeContent(
    sections: List<HomeSection>,
    onChannelClick: (Channel) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {
        items(
            items = sections,
            key = { it.id },
            contentType = { it::class }
        ) { section ->
            when (section) {
                is HomeSection.Carousel -> {
                    CarouselSection(
                        channels = section.channels,
                        onChannelClick = onChannelClick,
                        autoScrollEnabled = section.autoScrollEnabled,
                        autoScrollDelayMs = section.autoScrollDelayMs
                    )
                }

                is HomeSection.HorizontalList -> {
                    /* HorizontalListSection(
                         title = section.title,
                         channels = section.channels,
                         onChannelClick = onChannelClick,
                         onSeeAllClick = if (section.showSeeAll) {
                             { *//* Navigate to see all *//* }
                        } else null,
                        modifier = Modifier.padding(top = 16.dp)
                    )*/
                }

                is HomeSection.TopRanked -> {
                    /*TopRankedSection(
                        title = section.title,
                        channels = section.channels,
                        onChannelClick = onChannelClick,
                        modifier = Modifier.padding(top = 16.dp)
                    )*/
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