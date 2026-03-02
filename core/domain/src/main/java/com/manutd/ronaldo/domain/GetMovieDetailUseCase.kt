package com.manutd.ronaldo.domain

import com.manutd.ronaldo.network.model.MovieDetail
import com.manutd.rophim.core.data.repository.MovieDetailRepository
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val repository: MovieDetailRepository
) {
    suspend operator fun invoke(movieId: String): MovieDetail =
        repository.getMovieDetail(movieId)
}