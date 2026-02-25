package com.ronaldo.rophim.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.manutd.ronaldo.navigation.Navigator
import com.ronaldo.rophim.api.MoviesDetailKey
import com.ronaldo.rophim.screen.Movie
import com.ronaldo.rophim.screen.MoviesDetailScreen

fun EntryProviderScope<NavKey>.moviesDetailEntry(navigator: Navigator) {
    entry<MoviesDetailKey> { key ->
        //todo:Truyền Id để load detail theo id trước hết ta sẽ truyền fake dữ liệu
        MoviesDetailScreen(
            movie = Movie(
                title = "Titanic",
                posterUrl = "https://www.originalfilmart.com/cdn/shop/products/titanic_1997_quad_original_film_art_a_1600x.webp?v=1674239104",
                trailerUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            ),
            onNavigateToWatch = {},
        )
    }
}