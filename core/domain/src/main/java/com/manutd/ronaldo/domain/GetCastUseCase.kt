package com.manutd.ronaldo.domain

import com.manutd.ronaldo.network.model.CastMember
import com.manutd.rophim.core.data.repository.MovieDetailRepository
import javax.inject.Inject

class GetCastUseCase @Inject constructor(
    private val repository: MovieDetailRepository
) {
    suspend operator fun invoke(movieId: String): List<CastMember> =
        repository.getCast(movieId)
}