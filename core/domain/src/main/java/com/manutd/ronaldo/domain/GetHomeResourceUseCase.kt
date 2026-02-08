package com.manutd.ronaldo.domain

import com.manutd.rophim.core.data.repository.HomeResourceRepository
import javax.inject.Inject

class GetHomeResourceUseCase @Inject constructor(
    private val homeResourceRepository: HomeResourceRepository
) {
    suspend operator fun invoke(
        onError: (String?) -> Unit
    ) = homeResourceRepository.getHomeData(
        onError = onError
    )
}