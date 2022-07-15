package com.muralex.worldnews.presentation.ui.home

import androidx.lifecycle.Observer
import com.muralex.achiever.utilities.BaseUnitTest
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.utils.Resource
import com.muralex.worldnews.domain.usecase.news.GetNewsUseCase
import com.muralex.worldnews.domain.usecase.news.UpdateNewsUseCase
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseUnitTest() {

    private lateinit var homeViewModel: HomeViewModel
    private val getNewsUseCase: GetNewsUseCase = mock()
    private val updateNewsUseCase: UpdateNewsUseCase = mock()
    private val articlesList = mock<List<Article>>()
    private val expected = Resource.success(articlesList)
    private val expectedError = Resource.error("msg", articlesList)
    private val observer = mockk<Observer<HomeViewModel.ViewState>> { every { onChanged(any()) } just Runs }

    @Test
    fun getListsFromRepository() = runTest {
         mockSuccessfulCase()
        homeViewModel.getNews("ca")
        verify(getNewsUseCase, times(1)).invoke()
    }

    @Test
    fun emitsListFromRepository() = runTest {

        mockSuccessfulCase()
        homeViewModel.viewState.observeForever(observer)
        homeViewModel.getNews("ca")

        verifySequence {
            observer.onChanged(HomeViewModel.ViewState.Loading)
            observer.onChanged(HomeViewModel.ViewState.ListLoaded(expected))
        }

    }

    @Test
    fun errorIfReceiveError() = runTest {
        mockErrorCase()

        homeViewModel.viewState.observeForever(observer)
        homeViewModel.getNews("ca")

        verifySequence {
            observer.onChanged(HomeViewModel.ViewState.Loading)
            observer.onChanged(HomeViewModel.ViewState.ListLoadFailure(expectedError))
        }
    }

    private fun mockSuccessfulCase() = runTest {
        whenever(getNewsUseCase()).thenReturn(expected)
        whenever(updateNewsUseCase()).thenReturn(expected)
        homeViewModel = HomeViewModel(getNewsUseCase, updateNewsUseCase)
    }

    private fun mockErrorCase() = runTest  {
        whenever(getNewsUseCase()).thenReturn(expectedError)
        whenever(updateNewsUseCase()).thenReturn(expectedError)
        homeViewModel = HomeViewModel(getNewsUseCase, updateNewsUseCase)
    }


}