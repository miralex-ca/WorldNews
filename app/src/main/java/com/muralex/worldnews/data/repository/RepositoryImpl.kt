package com.muralex.worldnews.data.repository

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.db.NewsDBData
import com.muralex.worldnews.data.model.utils.Resource
import com.muralex.worldnews.data.model.utils.Status
import com.muralex.worldnews.data.repository.datasource.NewsCacheDataSource
import com.muralex.worldnews.data.repository.datasource.NewsLocalDataSource
import com.muralex.worldnews.data.repository.datasource.NewsRemoteDataSource
import com.muralex.worldnews.domain.repository.Repository

class RepositoryImpl (
    private val newsCacheDataSource: NewsCacheDataSource,
    private val newsLocalDataSource: NewsLocalDataSource,
    private val remoteDataSource: NewsRemoteDataSource
        ): Repository {

    override suspend fun getNewsArticles() = getNewsArticlesFromCache()

    override suspend fun updateNewsArticles(): Resource<List<Article>> {

        val update = getFromAPIAndSaveToDB()

        var articles = newsLocalDataSource.getNewsFromDB()
        newsCacheDataSource.saveArticlesToCache(articles)

        if (update.status == Status.ERROR) {
            articles = Resource.error(update.message.toString(), articles.data)
        }
        return articles
    }

    private suspend fun getNewsArticlesFromApi() = remoteDataSource.getArticlesFromApi()

    private suspend fun getNewsFromDB() : Resource<List<Article>> {
        var news = newsLocalDataSource.getNewsFromDB()
        if (news.data.isNullOrEmpty()) {
            getFromAPIAndSaveToDB()
            news = newsLocalDataSource.getNewsFromDB()
        }
        return news
    }

    private suspend fun getFromAPIAndSaveToDB() :  Resource<List<NewsDBData>> {

        val updatedList = getNewsArticlesFromApi()

        if (updatedList.status == Status.SUCCESS && !updatedList.data.isNullOrEmpty()) {
            newsLocalDataSource.clearAll()
            newsLocalDataSource.saveNewsToDB(updatedList.data)
        }

        return updatedList
    }

    private suspend fun getNewsArticlesFromCache() : Resource<List<Article>> {
        var articles = newsCacheDataSource.getArticlesFromCache()
        if (articles.data.isNullOrEmpty()) {
            articles = getNewsFromDB()
            newsCacheDataSource.saveArticlesToCache(articles)
        }
        return articles
    }
}