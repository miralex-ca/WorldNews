package com.muralex.worldnews.data.repository.localdatasource

import com.muralex.worldnews.data.database.NewsDao
import com.muralex.worldnews.data.model.db.NewsDBMapper
import com.muralex.worldnews.data.repository.remotedatasource.NewsRemoteDataSourceImpl
import com.muralex.worldnews.data.repository.remotedatasource.NewsRemoteDataSourceImplTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
class NewsLocalDataSourceImplTest {

    lateinit var SUT: NewsLocalDataSource
    private val dao: NewsDao = mockk()
    private val mapper: NewsDBMapper = mockk()

    @Before
    fun setUp() {
        coEvery { dao.getAllNews() } returns emptyList()
        coEvery { dao.insertNewsList(any()) } returns Unit
        coEvery { dao.deleteAllNews() } returns Unit
        coEvery { mapper.mapFromEntityList(any()) } returns emptyList()
        SUT = NewsLocalDataSourceImpl(dao, mapper)
    }

    @Test
    fun getNewsFromDB_invoke_daoGetNews() = runTest {
        SUT.getNewsFromDB()
        coVerify {  dao.getAllNews() }
    }

    @Test
    fun saveNewsToDB_invoke_daoInsertNews() = runTest  {
        SUT.saveNewsToDB(any())
        coVerify {  dao.insertNewsList(any()) }
    }

    @Test
    fun clearAll_invoke_daoDelete() = runTest {
        SUT.clearAll()
        coVerify {  dao.deleteAllNews() }
    }

}