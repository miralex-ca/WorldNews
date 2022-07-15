package com.muralex.worldnews.data.api

import com.muralex.worldnews.data.model.api.NewsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("/v2/top-headlines")
    suspend fun getHeadlines(
        @Query("country")
        country: String,
//        @Query("apiKey")
//        apiKey: String,
        @Query("pageSize")
        pageSize: Int = 30
    ): Response<NewsApiResponse>


}