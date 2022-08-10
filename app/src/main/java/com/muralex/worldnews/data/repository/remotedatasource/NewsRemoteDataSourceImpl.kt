package com.muralex.worldnews.data.repository.remotedatasource

import com.muralex.worldnews.data.api.NewsApiService
import com.muralex.worldnews.data.model.api.NewsMapper
import com.muralex.worldnews.data.model.db.NewsDBData
import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.app.data.ResourceInfo
import com.muralex.worldnews.app.utils.Constants.DATA_FETCH_ERROR
import com.muralex.worldnews.app.utils.NetworkHelper
import com.muralex.worldnews.app.utils.SettingsHelper
import com.muralex.worldnews.data.model.api.NewsApiResponse
import retrofit2.Response
import java.lang.Exception

class NewsRemoteDataSourceImpl(
    private val apiService: NewsApiService,
    private val mapper: NewsMapper,
    private val settingsHelper: SettingsHelper,
    private val networkHelper: NetworkHelper
) : NewsRemoteDataSource {

    override suspend fun getNewsDataFromApi() = getNewsData()

    private suspend fun getNewsData(): Resource<List<NewsDBData>> {

        val country = settingsHelper.getSelectedCountry()

        if (!networkHelper.isNetworkConnected())
            return handleConnectionError()

        return try {
            val response: Response<NewsApiResponse> = apiService.getHeadlines(country)
            if (response.isSuccessful) {
                mapResponseToData(response)
            } else {
                handleResponseError()
            }
        } catch (e: Exception) {
            handleRequestException(e)
        }
    }

    private fun mapResponseToData(response: Response<NewsApiResponse>): Resource<List<NewsDBData>> {
        return response.body()?.let {
            val articles = mapper.mapFromEntityList(it.articles)
            Resource.success(articles)
        } ?: Resource.success(emptyList())
    }

    private fun handleConnectionError(): Resource<Nothing> {
        return Resource.error(DATA_FETCH_ERROR, null).apply {
            setInfo(ResourceInfo.ConnectionError)
        }
    }

    private fun handleResponseError(): Resource<Nothing> {
        return Resource.error(DATA_FETCH_ERROR, null).apply {
            setInfo(ResourceInfo.RequestError)
        }
    }

    private fun handleRequestException(e: Exception): Resource<Nothing> {
        return Resource.error(DATA_FETCH_ERROR, null).apply {
            setInfo(ResourceInfo.ErrorException(e))
        }
    }
}