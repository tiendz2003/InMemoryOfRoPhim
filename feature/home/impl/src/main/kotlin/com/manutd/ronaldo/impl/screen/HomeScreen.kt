package com.manutd.ronaldo.impl.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.compose.collectAsStateWithLifecycle
import com.airbnb.mvrx.compose.mavericksViewModel
import com.manutd.ronaldo.impl.HomeViewModel
import com.manutd.ronaldo.impl.utils.HomeSection
import com.manutd.ronaldo.network.model.Channel

@Composable
fun HomeScreen(
    onChannelClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = mavericksViewModel()
) {
    val state by viewModel.collectAsStateWithLifecycle()
    //val pullRefreshState = rememberPullToRefreshState()
    LaunchedEffect(Unit) {
        viewModel.fetchMovies(forceRefresh = false)
    }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.Black // Nền đen theo style Netflix
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = {
                // Khi người dùng kéo, bắt buộc tải lại
                viewModel.fetchMovies(forceRefresh = true)
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            // Tùy chỉnh màu sắc indicator nếu cần
            // containerColor = Color.White,
            // contentColor = Color.Red
        ) {
            // 3. Xử lý UI State dựa trên Async<T> của Mavericks
            val groupsAsync = state.sections
            val currentGroups = groupsAsync.invoke() // Lấy giá trị hiện tại (có thể là data cũ khi đang loading)

            when {
                // CASE A: Lỗi -> Hiện màn hình lỗi
                state.hasError -> {
                    ErrorContent(
                        message = state.errorMessage ?: "Đã có lỗi xảy ra",
                        onRetry = { viewModel.retry() }
                    )
                }
                (state.isLoading || state.isUninitialized) && currentGroups.isNullOrEmpty() -> {
                    LoadingContent()
                }

                !currentGroups.isNullOrEmpty() -> {
                    HomeContent(
                        sections = currentGroups,
                        onChannelClick = { channel ->
                            viewModel.selectChannel(channel.id)
                            onChannelClick(channel.id)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        androidx.compose.material3.Text("Không có nội dung", color = Color.White)
                    }
                }
            }
        }
    }
}
@Composable
private fun HomeContent(
    sections: List<HomeSection>,
    onChannelClick: (Channel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(
            items = sections,
            key = { it.id }, // Stable key
            contentType = { it::class } // Type-based reuse
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