package com.muralex.worldnews.data.repository

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.utils.Resource

interface NewsRemoteDataSource {
    suspend fun getArticlesFromApi() : Resource<List<Article>>
}