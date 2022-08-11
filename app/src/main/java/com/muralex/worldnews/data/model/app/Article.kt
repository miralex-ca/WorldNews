package com.muralex.worldnews.data.model.app

import java.io.Serializable

data class Article(
    val title: String,
    val description: String,
    val text: String,
    val url: String,
    val image: String,
    val source: String,
    val publishedAt: String,
    val publishedTime: Long

) : Serializable
