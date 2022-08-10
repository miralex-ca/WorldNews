package com.muralex.worldnews.data.repository.localdatasource

import com.muralex.worldnews.data.database.NewsDao
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.db.NewsDBData
import com.muralex.worldnews.data.model.db.NewsDBMapper
import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.data.repository.localdatasource.NewsLocalDataSource

class NewsLocalDataSourceImpl (
    private val dao: NewsDao,
    private val mapper: NewsDBMapper
    ) : NewsLocalDataSource {

    override suspend fun getNewsFromDB(): Resource<List<Article>> {
        val newsList = mapper.mapFromEntityList(dao.getAllNews())
        return Resource.success(newsList)
    }

    override suspend fun saveNewsToDB(news: List<NewsDBData>) {
            dao.insertNewsList(news)
    }

    override suspend fun clearAll() = dao.deleteAllNews()

}