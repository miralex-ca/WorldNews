package com.muralex.worldnews.domain.repository

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.utils.Resource

interface Repository {

    suspend fun getNewsArticles() : Resource<List<Article>>
    suspend fun updateNewsArticles() : Resource<List<Article>>

}