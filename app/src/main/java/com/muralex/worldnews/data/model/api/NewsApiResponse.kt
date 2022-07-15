package com.muralex.worldnews.data.model.api

import com.google.gson.annotations.SerializedName

data class NewsApiResponse (
    @SerializedName("articles")
    val articles: List<News>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int,

    )