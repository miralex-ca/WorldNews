package com.muralex.worldnews.domain.repository

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.app.data.Resource
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun checkFavorite(article: Article) : Flow<Boolean>
    suspend fun getFavoritesList() : Flow<Resource<List<Article>>>
    suspend fun addToFavorite(article: Article)
    suspend fun removeFromFavorite(article: Article)
}