package com.muralex.worldnews.domain.usecase.news

import com.google.common.truth.Truth.assertThat
import com.muralex.achiever.utilities.BaseUnitTest
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.domain.repository.Repository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class GetNewsUseCaseTest : BaseUnitTest() {

    private lateinit var SUT: GetNewsUseCase
    private val repository = mock<Repository>()
    private val mapper: ArticleDomainToUiMapper = mock()
    private val expectedData: Resource<List<Article>> = mock()

    @Before
    fun setUp() {
        SUT =  GetNewsUseCase(repository, mapper)
    }

    @Test
    fun getNewsUseCase_invoke_repositoryGetNews() = runTest {
        SUT.invoke()
        verify(repository, times(1)).getNewsArticles()
    }

    @Test
    fun getNewsUseCase_invoke_expectedDataFromRepository() = runTest {

        whenever(mapper.mapFromEntity(any())).thenReturn(
            expectedData
        )

        whenever(repository.getNewsArticles()).thenReturn(
            expectedData
        )

        val item = SUT.invoke()
        assertThat(item).isEqualTo(expectedData)
    }


}


