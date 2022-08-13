package com.muralex.worldnews.data.repository

import com.google.common.truth.Truth.assertThat
import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.app.data.Status
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.db.NewsDBData
import com.muralex.worldnews.data.repository.cachedatasource.NewsCacheDataSource
import com.muralex.worldnews.data.repository.localdatasource.NewsLocalDataSource
import com.muralex.worldnews.data.repository.remotedatasource.NewsRemoteDataSource
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test


@ExperimentalCoroutinesApi
class RepositoryImplTest {

    private lateinit var SUT: RepositoryImpl
    private val newsCacheDataSource: NewsCacheDataSource = mockk()
    private val newsLocalDataSource: NewsLocalDataSource = mockk()
    private val remoteDataSource: NewsRemoteDataSource = mockk()
    private val testData: Resource<List<Article>> = mockk()
    private val testArticle: Article = mockk()
    private val testArticleList = listOf(testArticle)
    private val testDBData: Resource<List<NewsDBData>> = mockk()
    private val testDBDataItem: NewsDBData = mockk()


    @Test
    fun getNewsArticles_emptyData_invokeAllDataSources() = runTest {
        mockEmptyListSuccess()
        SUT.getNewsArticles()
        coVerify { newsCacheDataSource.getArticlesFromCache() }
        coVerify { remoteDataSource.getNewsDataFromApi() }
        coVerify { newsLocalDataSource.getNewsFromDB() }
    }

    @Test
    fun getNewsArticles_notEmptyCacheData_invokeOnlyCache() = runTest {
        mockNotEmptyCache()
        SUT.getNewsArticles()
        coVerify { newsCacheDataSource.getArticlesFromCache() }
        coVerify { newsLocalDataSource.getNewsFromDB() wasNot Called }
    }

    @Test
    fun getNewsArticles_emptySuccessData_getSuccess() = runTest {
        mockEmptyListSuccess()
        val result = SUT.getNewsArticles()
        assertThat(result.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun getNewsArticles_emptyError_getError() = runTest {
        mockEmptyListError()
        val result = SUT.getNewsArticles()
        assertThat(result.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun updateArticles_invokeRemoteDataAndSaveCache() = runTest {
        mockEmptyListSuccess()
        SUT.updateNewsArticles()
        coVerify { remoteDataSource.getNewsDataFromApi() }
        coVerify { newsCacheDataSource.saveArticlesToCache(any()) }
    }

    private fun mockEmptyList() {
        coEvery { newsCacheDataSource.saveArticlesToCache(any()) } returns Unit
        coEvery { newsLocalDataSource.saveNewsToDB(any()) } returns Unit
        coEvery { newsCacheDataSource.saveArticlesToCache(testData) } returns Unit
        coEvery { testDBData.data } returns emptyList()
        coEvery { testDBData.isSuccess() } returns true
        coEvery { testDBData.isError() } returns false
        coEvery { testData.data } returns emptyList()
        coEvery { remoteDataSource.getNewsDataFromApi() } returns testDBData
        coEvery { newsLocalDataSource.getNewsFromDB() } returns testData
        coEvery { newsCacheDataSource.getArticlesFromCache() } returns testData
    }

    private fun mockEmptyListSuccess() {
        mockEmptyList()
        coEvery { testData.status } returns Status.SUCCESS
        SUT = RepositoryImpl(newsCacheDataSource,  newsLocalDataSource, remoteDataSource)
    }

    private fun mockEmptyListError() {
        mockEmptyList()
        coEvery { testData.status } returns Status.ERROR
        SUT = RepositoryImpl(newsCacheDataSource,  newsLocalDataSource, remoteDataSource)
    }

    private fun mockNotEmptyCache() {
        mockEmptyList()
        coEvery { testData.data } returns testArticleList
        coEvery { testData.status } returns Status.SUCCESS
        SUT = RepositoryImpl(newsCacheDataSource,  newsLocalDataSource, remoteDataSource)
    }


}