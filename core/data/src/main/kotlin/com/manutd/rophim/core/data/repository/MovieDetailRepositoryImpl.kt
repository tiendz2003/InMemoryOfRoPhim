package com.manutd.rophim.core.data.repository

import com.manutd.ronaldo.network.model.CastMember
import com.manutd.ronaldo.network.model.MovieDetail
import com.manutd.ronaldo.network.model.MovieRecommendation
import com.manutd.rophim.core.data.utils.FakeDataProvider
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieDetailRepositoryImpl @Inject constructor() : MovieDetailRepository {
    private suspend fun simulateNetwork(ms: Long = 800) = delay(ms)

    override suspend fun getMovieDetail(movieId: String): MovieDetail {
        simulateNetwork()
        // Map fake data theo ID
        return when (movieId) {
            "mv_001" -> FakeDataProvider.seriesOnAir
            "mv_002" -> FakeDataProvider.seriesCompleted
            "mv_003" -> FakeDataProvider.singleMovie
            else -> FakeDataProvider.seriesOnAir  // fallback
        }
    }

    override suspend fun getCast(movieId: String): List<CastMember> {
        simulateNetwork(600)
        return FakeDataProvider.fakeCast
    }

    override suspend fun getRecommendations(movieId: String): List<MovieRecommendation> {
        simulateNetwork(700)
        return FakeDataProvider.fakeRecommendations
    }

    override suspend fun toggleFavorite(movieId: String, isFavorited: Boolean): Boolean {
        simulateNetwork(300)
        return isFavorited
    }

    override suspend fun rateMovie(movieId: String, rating: Float): Float {
        simulateNetwork(300)
        return rating
    }


}