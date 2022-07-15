package com.muralex.worldnews.data.repository

import com.muralex.worldnews.data.database.FavoriteDao
import com.muralex.worldnews.data.database.NewsDao
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.db.FavoriteMapper
import com.muralex.worldnews.data.model.utils.Resource
import com.muralex.worldnews.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.lang.Exception

class FavoriteRepositoryImpl (
    private val dao: FavoriteDao,
    private val mapper: FavoriteMapper
) : FavoriteRepository {

    override suspend fun checkFavorite(article: Article): Flow<Boolean> {
        return  dao.checkFavorite(article.url).map { it > 0 }
    }

    override suspend fun getFavoritesList(): Flow<Resource<List<Article>>> {
        return  dao.getAllFavorites().map {
                Resource.success(
                    it.map { favoriteData ->
                        mapper.mapFromEntity(favoriteData)
                    }.asReversed()
                )
            }
    }

    override suspend fun addToFavorite(article: Article) {

        dao.insertFavorite(mapper.mapToEntity(article))
    }

    override suspend fun removeFromFavorite(article: Article) {

        dao.deleteByUrl(article.url)
    }
}