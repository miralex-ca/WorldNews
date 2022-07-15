package com.muralex.worldnews.data.repository

import com.muralex.worldnews.data.api.NewsApiService
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.utils.NewsToArticleMapper
import com.muralex.worldnews.data.model.utils.Resource
import com.muralex.worldnews.presentation.utils.SettingsHelper
import java.lang.Exception

class NewsRemoteDataSourceImpl (
    private val apiService: NewsApiService,
    private val mapper: NewsToArticleMapper,
    private val settingsHelper: SettingsHelper
) : NewsRemoteDataSource {

    override suspend fun getArticlesFromApi() = getArticles()

    private suspend fun getArticles() : Resource<List<Article>> {
        return try {
            val country = settingsHelper.getSelectedCountry()
            val response = apiService.getHeadlines(country)
            if (response.isSuccessful) {
                response.body()?.let {
                    val articles = mapper(it.articles)
                    return@let Resource.success(articles)
                } ?: Resource.error("No data", null)
            } else {
                Resource.error("Error loading", null)
            }
        } catch (e: Exception) {
            Resource.error("No data", null)
        }
    }

}