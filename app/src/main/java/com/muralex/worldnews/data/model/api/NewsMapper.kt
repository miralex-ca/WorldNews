package com.muralex.worldnews.data.model.api

import com.muralex.worldnews.data.model.db.NewsDBData
import com.muralex.worldnews.app.data.EntityMapper

class NewsMapper : EntityMapper<News, NewsDBData> {

    override fun mapFromEntity(data: News): NewsDBData {
        return  NewsDBData (
            id = 0,
            title = data.title ?: "",
            description = data.description ?: "",
            content = data.content?: "",
            url = data.url?: "",
            urlToImage = data.urlToImage ?: "",
            author = data.source?.name ?: "",
            source = data.source?.name ?: "",
            publishedAt = data.publishedAt ?: ""
        )
    }

    fun mapFromEntityList(entitiesList: List<News>) : List<NewsDBData> {
        return entitiesList.map { mapFromEntity(it) }
    }

}
