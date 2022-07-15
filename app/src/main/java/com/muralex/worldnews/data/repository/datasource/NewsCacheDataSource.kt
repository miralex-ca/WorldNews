package com.muralex.worldnews.data.repository.datasource

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.utils.Resource

interface NewsCacheDataSource {
    fun getArticlesFromCache() : Resource<List<Article>>
    fun saveArticlesToCache(articles: Resource<List<Article>>)
}