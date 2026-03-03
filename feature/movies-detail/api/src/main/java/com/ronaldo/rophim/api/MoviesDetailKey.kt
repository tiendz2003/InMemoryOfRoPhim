package com.ronaldo.rophim.api

import android.os.Parcelable
import androidx.navigation3.runtime.NavKey
import com.manutd.ronaldo.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
data class MoviesDetailKey(val movieId: String) : NavKey

fun Navigator.navigateToDetail(movieId: String) {
    navigate(
        MoviesDetailKey(
            movieId = movieId
        )
    )
}