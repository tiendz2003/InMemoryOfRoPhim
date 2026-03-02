package com.manutd.ronaldo.domain

import com.manutd.ronaldo.network.model.MovieRecommendation
import com.manutd.rophim.core.data.repository.MovieDetailRepository
import javax.inject.Inject

class GetRecommendationsUseCase @Inject constructor(
    private val repository: MovieDetailRepository
) {
    suspend operator fun invoke(movieId: String): List<MovieRecommendation> =
        repository.getRecommendations(movieId)
}
