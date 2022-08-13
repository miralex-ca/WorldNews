package com.muralex.worldnews.data.model.api

import android.os.Build
import com.muralex.worldnews.app.data.EntityMapper
import com.muralex.worldnews.data.model.db.NewsDBData
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toInstant
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class NewsMapper : EntityMapper<News, NewsDBData> {

    override fun mapFromEntity(data: News): NewsDBData {

       // Log.d("MyTag", "date: ${data.publishedAt?.toDate()?.formatTo("dd MMM yyyy") }")

        return NewsDBData(
            id = 0,
            title = data.title ?: "",
            description = data.description?.trim() ?: "",
            content = data.content ?: "",
            url = data.url ?: "",
            urlToImage = data.urlToImage ?: "",
            author = data.source?.name ?: "",
            source = data.source?.name ?: "",
            published =  processDate(data.publishedAt)
        )
    }

    fun mapFromEntityList(entitiesList: List<News>?): List<NewsDBData> {
        return entitiesList?.map { mapFromEntity(it) } ?: emptyList()
    }

    private fun processDate(date: String?) : Long {
        var published  =  0L

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                published = getTimeFromData(date)
            } else {
                published = getTimeFromDataOlder(date)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return published
    }

    private fun getTimeFromData(date: String?) : Long {
        return date?.toInstant()?.toEpochMilliseconds() ?: 0L
    }

    private fun getTimeFromDataOlder(date: String?) : Long {
        var published  =  0L
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        date?.let {
            val parse : Date? = simpleDateFormat.parse(it)
            published = parse?.time ?: 0L
        }
        return published
    }






}
