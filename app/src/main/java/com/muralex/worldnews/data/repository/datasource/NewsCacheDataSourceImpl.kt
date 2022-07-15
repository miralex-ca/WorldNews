package com.muralex.worldnews.data.repository.datasource

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.utils.Resource
import com.muralex.worldnews.data.model.utils.Status
import com.muralex.worldnews.data.repository.datasource.NewsCacheDataSource

class NewsCacheDataSourceImpl: NewsCacheDataSource {

    private var articles = Resource(Status.LOADING, emptyList<Article>(), "")

    override fun getArticlesFromCache() = articles

    override fun saveArticlesToCache(articles: Resource<List<Article>>) {
        this.articles = articles
    }
}