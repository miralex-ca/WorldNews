package com.muralex.worldnews.data.repository.cachedatasource

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.app.data.Resource

interface NewsCacheDataSource {
    fun getArticlesFromCache() : Resource<List<Article>>
    fun saveArticlesToCache(articles: Resource<List<Article>>)
}
