package com.muralex.worldnews.data.model.db

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.utils.EntityMapper
import javax.inject.Inject

class NewsDBMapper @Inject constructor(): EntityMapper<NewsDBData, Article> {

    override fun mapFromEntity(data: NewsDBData): Article {
        return  Article (
            title = data.title,
            description = data.description,
            text = data.content,
            url = data.url,
            image = data.urlToImage,
            author = data.source
        )
    }

    fun mapFromEntityList(entitiesList: List<NewsDBData>) : List<Article> {
        return entitiesList.map { mapFromEntity(it) }
    }

}
