package com.muralex.worldnews.data.model.db

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.app.data.EntityMapper
import com.muralex.worldnews.app.utils.formatToShortDate
import javax.inject.Inject

class FavoriteToArticleMapper @Inject constructor(): EntityMapper<FavoriteData, Article> {

    override fun mapFromEntity(data: FavoriteData): Article {
        return  Article (
            title = data.title,
            description = data.description,
            text = data.text,
            url = data.url,
            image = data.image,
            source = data.author,
            publishedAt = data.time.formatToShortDate(),
            publishedTime = data.time

        )
    }

    fun mapToEntity(data: Article): FavoriteData {
        return  FavoriteData (
            title = data.title,
            description = data.description,
            text = data.text,
            url = data.url,
            image = data.image,
            author = data.source,
            time = data.publishedTime
        )
    }

    fun mapFromEntityList(entitiesList: List<FavoriteData>) : List<Article> {
        return entitiesList.map { mapFromEntity(it) }
    }

}
