package com.muralex.worldnews.data.model.utils

import com.muralex.worldnews.data.model.api.News
import com.muralex.worldnews.data.model.app.Article

class NewsToArticleMapper : Function1<List<News>, List<Article>> {

    override operator fun invoke(data: List<News>): List<Article> {

        return data.map {

            Article (
                title = it.title ?: "",
                description = it.description ?: "",
                text = it.content?: "",
                url = it.url?: "",
                image = it.urlToImage ?: "",
            )
        }
    }
}
