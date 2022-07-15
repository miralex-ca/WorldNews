package com.muralex.worldnews.domain.usecase.news

import com.muralex.worldnews.domain.repository.Repository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.getNewsArticles()
}