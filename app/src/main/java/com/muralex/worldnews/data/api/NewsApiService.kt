package com.muralex.worldnews.data.api

import com.muralex.worldnews.app.utils.Constants.NEWS_LIST_SIZE
import com.muralex.worldnews.data.model.api.NewsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("/v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        country: String,
        @Query("pageSize")
        pageSize: Int = NEWS_LIST_SIZE
    ): Response<NewsApiResponse>

}