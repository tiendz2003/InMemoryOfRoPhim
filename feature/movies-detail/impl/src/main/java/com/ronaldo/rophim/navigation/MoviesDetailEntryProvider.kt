package com.ronaldo.rophim.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.manutd.ronaldo.navigation.Navigator
import com.manutd.rophim.mavericksViewModel
import com.ronaldo.rophim.api.MoviesDetailKey
import com.ronaldo.rophim.screen.MoviesDetailScreen

fun EntryProviderScope<NavKey>.moviesDetailEntry(navigator: Navigator) {
    entry<MoviesDetailKey> { key ->
        //todo:Truyền Id để load detail theo id trước hết ta sẽ truyền fake dữ liệu
        MoviesDetailScreen(
            onNavigateBack = {},
            onNavigateToWatch = {},
            viewModel = mavericksViewModel(
                argsFactory = { key.movieId }
            )
        )
    }
}