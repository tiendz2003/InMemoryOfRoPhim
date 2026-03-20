package com.ronaldo.rophim.api

import androidx.navigation3.runtime.NavKey
import com.manutd.ronaldo.navigation.Navigator
import com.manutd.ronaldo.network.model.Episode

data class WatchScreenKey(
    val movieId: String,
    val episode: Episode? = null
) : NavKey

fun Navigator.navigateToWatch(
    movieId: String,
    episode: Episode? = null
) {
    navigate(
        WatchScreenKey(
            movieId = movieId,
            episode = episode
        )
    )
}