package com.manutd.rophim.core.data.repository

import com.manutd.ronaldo.network.model.CastMember
import com.manutd.ronaldo.network.model.MovieDetail
import com.manutd.ronaldo.network.model.MovieRecommendation

interface MovieDetailRepository {
    suspend fun getMovieDetail(movieId: String): MovieDetail
    suspend fun getCast(movieId: String): List<CastMember>
    suspend fun getRecommendations(movieId: String): List<MovieRecommendation>
    suspend fun toggleFavorite(movieId: String, isFavorited: Boolean): Boolean
    suspend fun rateMovie(movieId: String, rating: Float): Float
}