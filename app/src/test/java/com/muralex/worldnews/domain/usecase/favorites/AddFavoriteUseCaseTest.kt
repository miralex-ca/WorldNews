package com.muralex.worldnews.domain.usecase.favorites

import com.google.common.truth.Truth.assertThat
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.domain.repository.FavoriteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AddFavoriteUseCaseTest  {

    private lateinit var SUT : AddFavoriteUseCase
    private val repository: FavoriteRepository = mockk()
    private val testArticle: Article = mockk()
    private val expectedData = Unit

    @Before
    fun setUp() {
        SUT =  AddFavoriteUseCase(repository)
        coEvery { repository.addToFavorite(testArticle) } returns Unit
    }

    @Test
    fun addFavoriteUseCase_invoke_repositoryGetNews() = runTest {
        SUT.invoke(testArticle)
        coVerify { repository.addToFavorite(testArticle)}
    }

    @Test
    fun getNewsUseCase_invoke_expectedDataFromRepository() = runTest {
        val item = SUT.invoke(testArticle)
        assertThat(item).isEqualTo(expectedData)
    }
}