package com.muralex.worldnews.data.database

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.muralex.worldnews.data.model.db.FavoriteData
import com.muralex.worldnews.data.model.db.NewsDBData
import com.muralex.worldnews.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class FavoriteDaoTest  {

    @get:Rule
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineScopeRule()

    private lateinit var appDatabase: AppDatabase
    private lateinit var SUT: FavoriteDao

    @Before
    fun initDb() {
        appDatabase = Room
            .inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()

        SUT = appDatabase.favoriteDao
    }

    @After
    fun closeDb() {
        appDatabase.close()
        countingTaskExecutorRule.drainTasks(1, TimeUnit.SECONDS)
        assertThat(countingTaskExecutorRule.isIdle).isTrue()
    }

    @Test
    fun getAllFavorites_noDataInserted_isEmpty() = runTest {
        val data = SUT.getAllFavorites().first()
        assertThat(data).isEmpty()
    }

    @Test
    fun insertFavorite_getAll_lastIsTestData () = runTest {
        SUT.insertFavorite(testDataItem)
        val data = SUT.getAllFavorites().first()
        assertThat(data.last()).isEqualTo(testDataItem)
    }

    @Test
    fun insertFavorite_deleteByUrl_listNotContainsTestData () = runTest {
        SUT.insertFavorite(testDataItem)
        val dataBefore = SUT.getAllFavorites().first()
        assertThat(dataBefore.contains(testDataItem)).isTrue()

        SUT.deleteByUrl(testUrl)
        val data = SUT.getAllFavorites().first()
        assertThat(data.contains(testDataItem)).isFalse()
    }

    @Test
    fun checkFavoriteByUrl_notInserted_getGreaterThanZero () = runTest {
        val result = SUT.checkFavorite(testUrl).first()
        assertThat(result).isEqualTo(0)
    }

    @Test
    fun checkFavoriteByUrl_inserted_getGreaterThanZero () = runTest {
        SUT.insertFavorite(testDataItem)
        val result = SUT.checkFavorite(testUrl).first()
        assertThat(result).isGreaterThan(0)
    }

    companion object {
        private const val testUrl = "url"
        private val testDataItem = FavoriteData("", "", "", "",
            testUrl, "", 0)

    }

}
