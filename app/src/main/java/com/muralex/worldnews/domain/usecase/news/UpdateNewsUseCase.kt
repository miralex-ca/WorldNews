package com.muralex.worldnews.domain.usecase.news

import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.domain.repository.Repository
import javax.inject.Inject

class UpdateNewsUseCase @Inject constructor(
    private val repository: Repository,
    private val mapper: ArticleDomainToUiMapper
    ) {
    suspend operator fun invoke(): Resource<List<Article>> {
        return mapper.mapFromEntity(repository.updateNewsArticles())
    }
}