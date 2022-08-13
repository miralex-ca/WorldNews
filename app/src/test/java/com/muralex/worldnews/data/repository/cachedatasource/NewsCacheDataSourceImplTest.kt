package com.muralex.worldnews.data.repository.cachedatasource

import com.google.common.truth.Truth.assertThat
import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.data.model.app.Article
import org.junit.Test

class NewsCacheDataSourceImplTest {

    private val SUT = NewsCacheDataSourceImpl()

    @Test
    fun saveArticles_successResource_getTheSameList () {
        val articles: Resource<List<Article>>  = Resource.success(emptyList())
        SUT.saveArticlesToCache(articles)
        assertThat(SUT.getArticlesFromCache()).isEqualTo(articles)
    }

    @Test
    fun saveArticles_errorResource_getTheSameList () {
        val articles: Resource<List<Article>>  = Resource.error("", emptyList())
        SUT.saveArticlesToCache(articles)
        assertThat(SUT.getArticlesFromCache()).isEqualTo(articles)
    }

}