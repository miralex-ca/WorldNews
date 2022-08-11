package com.muralex.worldnews.presentation.fragments.home

import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.muralex.achiever.utilities.BaseUnitTest
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.domain.usecase.news.GetNewsUseCase
import com.muralex.worldnews.domain.usecase.news.UpdateNewsUseCase
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseUnitTest() {

    private lateinit var SUT: HomeViewModel
    private val articlesList = mock<List<Article>>()
    private val expected = Resource.success(articlesList)
    private val getNewsUseCase = mockk<GetNewsUseCase>()
    private val updateNewsUseCase: UpdateNewsUseCase = mockk()
    private val expectedError = Resource.error("msg", articlesList)
    private val observer =
        mockk<Observer<HomeViewModel.ViewState>> { every { onChanged(any()) } just Runs }
    
    @Test
    fun getNews_getUseCaseInvoke() {
        mockSuccessfulCase()
        SUT.getNews(COUNTRY_CODE)
        coVerify { getNewsUseCase.invoke() }
    }

    @Test
    fun update_updateUseCaseInvoke() {
        mockSuccessfulCase()
        SUT.updateNews()
        coVerify { updateNewsUseCase() }
    }

    @Test
    fun startRefresh_onStart_isFalse() {
        mockSuccessfulCase()
        assertThat(SUT.startRefresh).isTrue()
    }

    @Test
    fun startRefresh_afterGetNews_isFalse() {
        mockSuccessfulCase()
        SUT.getNews(COUNTRY_CODE)
        assertThat(SUT.startRefresh).isFalse()
    }

    @Test
    fun startRefresh_afterUpdateNews_isFalse() {
        mockSuccessfulCase()
        SUT.updateNews()
        assertThat(SUT.startRefresh).isFalse()
    }

    @Test
    fun differentCountry_updateUseCaseInvoke() {
        mockSuccessfulCase()
        SUT.setNewsCountry("test")
        SUT.getNews(COUNTRY_CODE)
        coVerify { updateNewsUseCase() }
    }

    @Test
    fun getNews_success_ViewStateListLoaded() {
        mockSuccessfulCase()
        SUT.viewState.observeForever(observer)
        SUT.getNews(COUNTRY_CODE)

        verifySequence {
            observer.onChanged(HomeViewModel.ViewState.Loading)
            observer.onChanged(HomeViewModel.ViewState.ListLoaded(expected))
        }
    }

    @Test
    fun updateNews_success_ViewStateListRefreshed() {
        mockSuccessfulCase()
        SUT.viewState.observeForever(observer)
        SUT.updateNews()

        verifySequence {
            observer.onChanged(HomeViewModel.ViewState.Loading)
            observer.onChanged(HomeViewModel.ViewState.ListRefreshed(expected))
        }
    }

    @Test
    fun getNews_error_ViewStateListLoadFailure() {
        mockErrorCase()
        SUT.viewState.observeForever(observer)
        SUT.getNews(COUNTRY_CODE)

        verifySequence {
            observer.onChanged(HomeViewModel.ViewState.Loading)
            observer.onChanged(HomeViewModel.ViewState.ListLoadFailure(expectedError))
        }
    }

    @Test
    fun updateNews_error_ViewStateListLoadFailure() {
        mockErrorCase()
        SUT.viewState.observeForever(observer)
        SUT.updateNews()

        verifySequence {
            observer.onChanged(HomeViewModel.ViewState.Loading)
            observer.onChanged(HomeViewModel.ViewState.ListLoadFailure(expectedError))
        }
    }

    private fun mockSuccessfulCase() {
        coEvery { getNewsUseCase.invoke() } returns expected
        coEvery { updateNewsUseCase() } returns expected
        SUT = HomeViewModel(getNewsUseCase, updateNewsUseCase)
    }

    private fun mockErrorCase() {
        coEvery { getNewsUseCase() } returns expectedError
        coEvery { updateNewsUseCase() } returns expectedError
        SUT = HomeViewModel(getNewsUseCase, updateNewsUseCase)
    }

    companion object {
        const val COUNTRY_CODE = "ca"
    }

}