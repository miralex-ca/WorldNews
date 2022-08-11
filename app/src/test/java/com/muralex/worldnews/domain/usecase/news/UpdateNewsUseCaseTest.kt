package com.muralex.worldnews.domain.usecase.news

import com.google.common.truth.Truth.assertThat
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.domain.repository.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class UpdateNewsUseCaseTest {

    private lateinit var SUT : UpdateNewsUseCase
    private val repository = mockk<Repository>()
    private val mapper: ArticleDomainToUiMapper = mockk()
    private val expectedData: Resource<List<Article>> = mockk()

    @Before
    fun setUp() {
        SUT =  UpdateNewsUseCase(repository, mapper)
        coEvery { mapper.mapFromEntity(any()) } returns expectedData
        coEvery { repository.updateNewsArticles() } returns expectedData
    }

    @Test
    fun updateNewsUseCase_invoke_repositoryGetNews() = runTest {
        SUT.invoke()
        coVerify { repository.updateNewsArticles()}
    }

    @Test
    fun getNewsUseCase_invoke_expectedDataFromRepository() = runTest {
        val item = SUT.invoke()
        assertThat(item).isEqualTo(expectedData)
    }






}