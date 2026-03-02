package com.manutd.ronaldo.domain

import com.manutd.rophim.core.data.repository.MovieDetailRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: MovieDetailRepository
) {
    suspend operator fun invoke(movieId: String, current: Boolean): Boolean =
        repository.toggleFavorite(movieId, !current)
}
