package com.muralex.worldnews.data.repository.datasource

import android.util.Log
import com.muralex.worldnews.data.api.NewsApiService
import com.muralex.worldnews.data.model.api.NewsMapper
import com.muralex.worldnews.data.model.db.NewsDBData
import com.muralex.worldnews.data.model.utils.Resource
import com.muralex.worldnews.presentation.utils.NetworkHelper
import com.muralex.worldnews.presentation.utils.SettingsHelper
import java.lang.Exception

class NewsRemoteDataSourceImpl (
    private val apiService: NewsApiService,
    private val mapper: NewsMapper,
    private val settingsHelper: SettingsHelper,
    private val networkHelper: NetworkHelper
) : NewsRemoteDataSource {

    override suspend fun getArticlesFromApi() = getArticles()

    private suspend fun getArticles() : Resource<List<NewsDBData>> {

        if (!networkHelper.isNetworkConnected()) {
            return setErrorResource("Network error")
        }

        return try {
            val country = settingsHelper.getSelectedCountry()
            val response = apiService.getHeadlines(country)
            if (response.isSuccessful) {
                response.body()?.let {
                    val articles = mapper.mapFromEntityList(it.articles)
                    return@let Resource.success(articles)
                } ?: setErrorResource("No data")
            } else {
                setErrorResource("Error loading")
            }
        } catch (e: Exception) {
            setErrorResource("No data")
        }
    }

    private fun setErrorResource(message: String): Resource<Nothing> {
        Log.d("mtag", "error net")
        return Resource.error(message, null)
    }


}