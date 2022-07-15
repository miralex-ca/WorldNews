package com.muralex.worldnews.domain.usecase

import com.muralex.worldnews.domain.Repository
import javax.inject.Inject

class UpdateNewsUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke() = repository.updateNewsArticles()
}