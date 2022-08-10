package com.muralex.worldnews.data.repository.localdatasource

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.db.NewsDBData
import com.muralex.worldnews.app.data.Resource

interface NewsLocalDataSource {
    suspend fun getNewsFromDB() : Resource<List<Article>>
    suspend fun saveNewsToDB(news: List<NewsDBData>)
    suspend fun clearAll()
}