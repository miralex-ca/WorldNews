package com.muralex.worldnews.data.repository

import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.db.NewsDBData
import com.muralex.worldnews.data.repository.cachedatasource.NewsCacheDataSource
import com.muralex.worldnews.data.repository.localdatasource.NewsLocalDataSource
import com.muralex.worldnews.data.repository.remotedatasource.NewsRemoteDataSource
import com.muralex.worldnews.domain.repository.Repository

class RepositoryImpl(
    private val newsCacheDataSource: NewsCacheDataSource,
    private val newsLocalDataSource: NewsLocalDataSource,
    private val remoteDataSource: NewsRemoteDataSource,
) : Repository {

    override suspend fun getNewsArticles() = getNewsArticlesFromCache()

    override suspend fun updateNewsArticles(): Resource<List<Article>> {
        val update = getFromAPIAndSaveToDB()
        var articles = newsLocalDataSource.getNewsFromDB()
        newsCacheDataSource.saveArticlesToCache(articles)

        if (update.isError()) {
            articles = Resource.error(update.message.toString(), articles.data).apply {
                setInfo(update.getInfo())
            }
        }
        return articles
    }

    private suspend fun getNewsDataFromApi(): Resource<List<NewsDBData>> {
        return remoteDataSource.getNewsDataFromApi()
    }

    private suspend fun getArticlesFromDB(): Resource<List<Article>> {
        var news = newsLocalDataSource.getNewsFromDB()
        if (news.data.isNullOrEmpty()) {
            getFromAPIAndSaveToDB()
            news = newsLocalDataSource.getNewsFromDB()
        }
        return news
    }

    private suspend fun getFromAPIAndSaveToDB(): Resource<List<NewsDBData>> {
        val updatedList = getNewsDataFromApi()
        if (updatedList.isSuccess() && !updatedList.data.isNullOrEmpty()) {
            newsLocalDataSource.clearAll()
            newsLocalDataSource.saveNewsToDB(updatedList.data)
        }
        return updatedList
    }

    private suspend fun getNewsArticlesFromCache(): Resource<List<Article>> {
        var articles = newsCacheDataSource.getArticlesFromCache()
        if (articles.data.isNullOrEmpty()) {
            articles = getArticlesFromDB()
            newsCacheDataSource.saveArticlesToCache(articles)
        }
        return articles
    }
}