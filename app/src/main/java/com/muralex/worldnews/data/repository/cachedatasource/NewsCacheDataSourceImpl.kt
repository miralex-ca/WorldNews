package com.muralex.worldnews.data.repository.cachedatasource

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.app.data.Status
import com.muralex.worldnews.data.repository.cachedatasource.NewsCacheDataSource

class NewsCacheDataSourceImpl: NewsCacheDataSource {

    private var articles = Resource(Status.LOADING, emptyList<Article>(), "")

    override fun getArticlesFromCache() = articles

    override fun saveArticlesToCache(articles: Resource<List<Article>>) {
        this.articles = articles
    }
}