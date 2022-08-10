package com.muralex.worldnews.domain.usecase.favorites

import com.muralex.worldnews.domain.repository.FavoriteRepository
import javax.inject.Inject

class GetFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository) {
    suspend operator fun invoke() = repository.getFavoritesList()
}