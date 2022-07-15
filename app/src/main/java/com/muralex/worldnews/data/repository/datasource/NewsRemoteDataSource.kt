package com.muralex.worldnews.data.repository.datasource

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.db.NewsDBData
import com.muralex.worldnews.data.model.utils.Resource

interface NewsRemoteDataSource {
    suspend fun getArticlesFromApi() : Resource<List<NewsDBData>>
}