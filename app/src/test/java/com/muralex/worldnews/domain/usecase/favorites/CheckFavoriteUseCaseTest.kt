package com.muralex.worldnews.domain.usecase.favorites

import com.google.common.truth.Truth.assertThat
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.domain.repository.FavoriteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class CheckFavoriteUseCaseTest {

    private lateinit var SUT : CheckFavoriteUseCase
    private val repository: FavoriteRepository = mockk()
    private val testArticle: Article = mockk()
    private val expectedData: Boolean = true

    @Before
    fun setUp() {
        SUT =  CheckFavoriteUseCase(repository)
        coEvery { SUT.invoke(testArticle) } returns flowOf(expectedData)
    }

    @Test
    fun updateNewsUseCase_invoke_repositoryGetNews() = runTest {
        SUT.invoke(testArticle)
        coVerify { repository.checkFavorite(testArticle)}
    }

    @Test
    fun getNewsUseCase_invoke_expectedDataFromRepository() = runTest {
        val item = SUT.invoke(testArticle).last()
        assertThat(item).isEqualTo(expectedData)
    }
}