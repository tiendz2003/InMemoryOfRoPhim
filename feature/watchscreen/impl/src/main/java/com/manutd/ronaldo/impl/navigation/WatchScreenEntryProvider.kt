package com.manutd.ronaldo.impl.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.manutd.ronaldo.impl.screen.WatchScreen
import com.manutd.ronaldo.navigation.Navigator
import com.manutd.rophim.mavericksViewModel
import com.ronaldo.rophim.api.WatchScreenKey

fun EntryProviderScope<NavKey>.watchScreenEntry(navigator: Navigator) {
    entry<WatchScreenKey> { key ->
        //todo:Truyền Id để load detail theo id trước hết ta sẽ truyền fake dữ liệu
        WatchScreen(
            onNavigateBack = {
                navigator.goBack()
            },
            viewModel = mavericksViewModel(
                argsFactory = {
                    key.movieId
                    key.episode
                }
            )
        )
    }
}