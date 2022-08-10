package com.muralex.worldnews.data.repository.remotedatasource

import com.muralex.worldnews.data.model.db.NewsDBData
import com.muralex.worldnews.app.data.Resource

interface NewsRemoteDataSource {
    suspend fun getNewsDataFromApi() : Resource<List<NewsDBData>>
}