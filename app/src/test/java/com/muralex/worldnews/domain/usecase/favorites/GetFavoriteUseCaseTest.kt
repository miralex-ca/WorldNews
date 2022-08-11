package com.muralex.worldnews.domain.usecase.favorites

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.domain.repository.FavoriteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class GetFavoriteUseCaseTest {

    private lateinit var SUT : GetFavoriteUseCase
    private val repository: FavoriteRepository = mockk()
    private val expectedData: Resource<List<Article>> = mockk()

    @Before
    fun setUp() {
        SUT =  GetFavoriteUseCase(repository)
        coEvery { repository.getFavoritesList() } returns flowOf( expectedData )
    }

    @Test
    fun getFavoriteUseCase_invoke_repositoryGetFavoritesList() = runTest {
        SUT.invoke()
        coVerify { repository.getFavoritesList()}
    }

    @Test
    fun getFavoriteUseCase_invoke_getExpectedData() = runTest {
        val item = SUT.invoke().last()
        assertThat(item).isEqualTo(expectedData)
    }










}