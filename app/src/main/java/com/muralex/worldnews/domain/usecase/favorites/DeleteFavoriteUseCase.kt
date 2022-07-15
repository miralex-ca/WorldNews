package com.muralex.worldnews.domain.usecase.favorites

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.domain.repository.FavoriteRepository
import com.muralex.worldnews.domain.repository.Repository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(private val repository: FavoriteRepository) {
    suspend operator fun invoke(article: Article) = repository.removeFromFavorite(article)
}