package com.muralex.worldnews.data.repository

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.utils.Resource
import com.muralex.worldnews.domain.Repository
import com.muralex.worldnews.presentation.utils.Constants

class RepositoryImpl (
    private val newsCacheDataSource: NewsCacheDataSource,
    private val remoteDataSource: NewsRemoteDataSource
        ): Repository {

    override suspend fun getNewsArticles() = getNewsArticlesFromCache()

    override suspend fun updateNewsArticles(): Resource<List<Article>> {
        val articles = getNewsArticlesFromApi()
        newsCacheDataSource.saveArticlesToCache(articles)
        return articles
    }

    private suspend fun getNewsArticlesFromApi() = remoteDataSource.getArticlesFromApi()

    private suspend fun getNewsArticlesFromCache() : Resource<List<Article>> {
        var articles = newsCacheDataSource.getArticlesFromCache()
        if (articles.data.isNullOrEmpty()) {
            articles = getNewsArticlesFromApi()
            newsCacheDataSource.saveArticlesToCache(articles)
        }
        return articles
    }
}