package com.muralex.worldnews.data.database

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.muralex.worldnews.data.model.db.NewsDBData
import com.muralex.worldnews.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class NewsDaoTest {

    @get:Rule
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineScopeRule()

    private lateinit var appDatabase: AppDatabase
    private lateinit var SUT: NewsDao


    @Before
    fun initDb() {
        appDatabase = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()

        SUT = appDatabase.newsDAO
    }

    @After
    fun closeDb() {
        appDatabase.close()
        countingTaskExecutorRule.drainTasks(1, TimeUnit.SECONDS)
        assertThat(countingTaskExecutorRule.isIdle).isTrue()
    }

    @Test
    fun getAllNews_WhenNoDataInserted() = runTest {
        val data = SUT.getAllNews()
        assertThat(data).isEmpty()
    }

    @Test
    fun insertNewsList_readLastItem_isTheSameUrl() = runTest {
        SUT.insertNewsList(testDataList)
        val data = SUT.getAllNews()
        assertThat(data.size).isEqualTo(testDataList.size)
        assertThat(data.last().url).isEqualTo(testUrlLast)
    }

    @Test
    fun deleteAllNews_afterInsertedItem_getEmptyList() = runTest {
        SUT.insertNewsList(testDataList)
        SUT.deleteAllNews()
        val data = SUT.getAllNews()
        assertThat(data).isEmpty()
    }

    companion object {
        private const val testUrl = "url"
        private const val testUrlLast = "url1"

        private val testDataItem = NewsDBData(0, "", "", "",
            0L, "", "title", testUrl, "")
        private val testDataItem1 = NewsDBData(2, "", "", "",
            0L, "", "title1", testUrlLast, "")

        private val testDataList: List<NewsDBData> = listOf(testDataItem, testDataItem1)

    }


}