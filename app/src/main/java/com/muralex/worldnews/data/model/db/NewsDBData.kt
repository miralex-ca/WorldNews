package com.muralex.worldnews.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.muralex.worldnews.data.model.api.Source
import com.squareup.moshi.Json

@Entity(tableName = "news_table")
data class NewsDBData (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "news_id")
    var id: Int,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "published")
    val published: Long,
    @ColumnInfo(name = "source")
    val source: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "urlToImage")
    val urlToImage: String
)